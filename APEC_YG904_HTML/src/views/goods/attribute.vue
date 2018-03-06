/**
* Created by lrx on 2017/12/26.
* 交收单管理
*/
<template>
    <div class="certificate">
        <my-header v-on:initialPage="initialPage" :firstTitle="firsTit" :secondTitle="secondTit" :thirdTitle="thirdTit" :breadFlag="breadflag"></my-header>
        <!--搜索-->
        <div class="mysearch" v-if="showFlag">
            <el-form :inline="true" class="search clearfix">
                <el-button class="fr" @click="goDetail">新增属性</el-button>
            </el-form>
        </div>
        <!--表格列表-->
        <div class="tableList" v-if="showFlag" style="margin-top: 20px;">
            <el-table :data="dataList" border stripe v-loading.body="loadCircle" header-row-class-name="headerRow"  >
            	<el-table-column prop="id" label="排序"></el-table-column>
            	<el-table-column prop="attrName" label="属性名"></el-table-column>
            	<el-table-column prop="attributeValueVOS" label="属性值" width="250">
            		<template slot-scope="scope">
            			<span v-for="item in scope.row.attributeValueVOS">
            				<span style="margin: 0 3px;">{{item.attrValue}}</span>
            			</span>
            		</template>
            	</el-table-column>
            	<el-table-column prop="createDate" label="创建时间">
            		<template slot-scope="scope">
            			{{filters.formatDatetime(scope.row.createDate)}}
            		</template>
            	</el-table-column>
            	<el-table-column prop="lastUpdateDate" label="修改时间">
            		<template slot-scope="scope">
            			{{filters.formatDatetime(scope.row.lastUpdateDate)}}
            		</template>
            	</el-table-column>
                <el-table-column label="操作" fixed="right" width="140">
                    <template slot-scope="scope">
                		<el-button type="text" @click="goDetail(scope.row, 'createAttrVal')">新增</el-button>
                		<el-button type="text" @click="goDetail(scope.row, 'edit')">修改</el-button>
                		<el-button type="text" @click="del(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页-->
			<div class="pagination-wrapper" style="text-align: right;">
				<el-pagination layout="jumper, sizes, prev, pager, next, total" :page-count="pageCount" :currentPage="currentNo" :page-sizes="[10, 20, 30, 40]" :page-size="pageSize" :total="total" @size-change="sizeChange" @current-change="retrieve"></el-pagination>
			</div>
        </div>

        <!--详情-->
        <div v-if="!showFlag">
        	<el-row v-if="isDetail" style="width: 700px; padding: 20px;">
        		<div v-if="isEditing || isCreateAttrVal">
		            <el-form :inline="true" label-width="100px" v-if="isCreateAttrVal">
		            	<div class="settlement-div">
			                <el-form-item label="属性值：">
			                    <el-input style="width: 461px;" v-model="attrCreate.attrValue"></el-input>
			                </el-form-item>
			                <el-form-item label="顺序号：">
							    <el-input style="width: 461px;" v-model="attrCreate.sort" @input="numberOnly"></el-input>
			                </el-form-item>
		                </div>
		            </el-form>
		            <div v-else>
			            <el-table :data="edit.attributeValueVOS" border stripe v-loading.body="loadCircle" header-row-class-name="headerRow"  >
			            	<el-table-column prop="attrValue" label="属性值">
			            		<template slot-scope="scope">
			            			<el-input v-model="scope.row.attrValue"></el-input>
			            		</template>
			            	</el-table-column>
			            	<el-table-column prop="sort" label="顺序号">
			            		<template slot-scope="scope">
			            			<el-input v-model="scope.row.sort"></el-input>
			            		</template>
			            	</el-table-column>
			            	<el-table-column label="操作">
			            		<template slot-scope="scope">
                					<el-button type="text" @click="attrValueDel(scope.row)">删除</el-button>
			            		</template>
			            	</el-table-column>
			            </el-table>
		            </div>
        		</div>
	            <el-form :inline="true" label-width="100px" v-else>
	            	<div class="settlement-div">
		                <el-form-item label="属性名：">
		                    <el-input style="width: 461px;" v-model="detail.attrName"></el-input>
		                </el-form-item>
	                </div>
	            </el-form>
			  	<el-row class="text-center" style="margin-top: 20px;">
                	<el-button @click="update">保存</el-button>
                    <el-button @click="backTablePage">返回</el-button>
			  	</el-row>
        	</el-row>
        </div>
        
    </div>
</template>

<script>
    import header from '~/components/header/header.vue'
    
    //新增构造函数
    function Detail(){
    	this.attrName = '';
    	this.attributeValueVOS = [
    		{sort: ''}
    	];
    }
    
    function AttrCreate(){
    	this.attrValue = '';
    	this.sort = '';
    	this.attributeNameId = '';
    }

    function Edit(){
    	this.pointRuleType = '';
    	this.userIds = [];
    	this.pointsChanged = '';
    	this.pointsChangedType = '';//REDUCTION
    	this.remark = '';
    }
    
    let vm;
    export default{
        data(){
            return {
            	api: this.apiUrl.attribute,
            	name: '',//用户昵称
                //breadcrumb
                firsTit:'商品',
                secondTit:'商品属性',
                thirdTit:'',
                breadflag:false,
                /*header参数*/
                viewFlag:false,
                showFlag:true, //true 显示table页; false 显示form页
                loadCircle:false,//加载显示
                dataList:[],
                
                //详情
                isDetail: false,
                detail: new Detail(),
                
                //属性值新增
                attrCreate: new AttrCreate(),
                isCreateAttrVal: false,
                
                //修改积分
                isEditing: false,
                edit: new Edit(),

//				分页容器
				currentNo: 1,
				pageCount: 1,
				pageSize: 10,
				total: 0,
            }
        },
        activated(){
            vm = this;
            vm.retrieve(1);
        },
        deactivated(){
            var vm=this;
            vm.loadCircle=false;//避免超时闪现加载图标
        },
        methods: {
        	toClear(){
        		vm.name = '';
        		vm.retrieve(1);
        	},
//			每页数据条数变化
			sizeChange(size) {
				vm.retrieve(vm.currentNo, size);
			},
			//详情
			goDetail(row, name){
				vm.showFlag = false;
				vm.detail = new Detail();
				vm.isDetail = true;
				if(name == 'createAttrVal'){
					vm.attrCreate = new AttrCreate();
					vm.isCreateAttrVal = true;
					vm.attrCreate.attributeNameId = row.id;
				}
				if(name == 'edit'){
		            vm.getAttrList(row);
				}
			},
			//获取属性列表
			getAttrList(row){
	            let callback = function(data){
	            	vm.edit = data.data;
	            	vm.isEditing = true;
					vm.isDetail = true;
	            }
	            let params={
	                url:vm.api.detail,
	                data: {id: row.id}
	            }
	            vm.ax.post(params,callback);
			},
			//删除
			del(row){
	            let callback = function(data){
	                vm.$message.success('删除成功!');
	                vm.retrieve(vm.currentNo);
	            }
                //确认删除框
                vm.$confirm('确认删除吗？','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(()=>{
	                let params={
	                    url:vm.api.del,
	                    data: {id: row.id}
	                }
	                vm.ax.post(params,callback);
	            }).catch(()=>{
	                console.log("已取消删除");
	            });
			},
			attrValueDel(row){
	            let callback = function(data){
	                vm.$message.success('删除成功!');
	                vm.getAttrList(vm.edit);
	            }
                let params={
                    url:vm.api.attrValueDel,
                    data: {id: row.id}
                }
                vm.ax.post(params,callback);
			},
			update() {
                let params={
                    url: vm.api.create,
                    data: vm.detail
                }
                if(vm.isCreateAttrVal){
                	params.url = vm.api.attrCreate;
                	params.data = vm.attrCreate;
                }else if(vm.isEditing){
                	params.url = vm.api.update;
                	params.data = vm.edit;
                }
	            let callback = function(data){
	                vm.$message.success("操作成功");
            		vm.backTablePage();
	            }
                vm.ax.post(params, callback, vm);
			},
            //back to tablePage
            backTablePage(){
                vm.showFlag=true;
                vm.isEditing = false;
                vm.isCreateAttrVal = false;
                vm.isDetail = true;
                vm.initialPage();//初始化页面标志
                vm.retrieve(vm.currentNo);//获取table数据
            },
            //初始化页面
            initialPage(){
                vm.showFlag=true;//显示列表，隐藏表单
                vm.breadflag=false;
            },
            //获取列表数据
			retrieve(currentNo, size) {
				if (size) {
					vm.pageSize = size;
				}
				vm.currentNo = currentNo;
                let params={
                    url:vm.api.retrieve,
                    data:{
                    	name: vm.name,
                        pageNumber: currentNo,//页码
                        pageSize: vm.pageSize
                    }
                }
	            let callback = function(data){
	                vm.dataList=data.data.rows;
					vm.pageCount = data.data.pageCount;
					vm.total = data.data.totalElements;
	            }
                vm.ax.post(params, callback, vm);
           }
        },
        components: {
            'my-header':header
        }
    }
</script>

<style scoped>
	h1 {
		font-weight: bold;
	}
	.introduction-ul {
		padding-left: 30px;
	}
</style>
