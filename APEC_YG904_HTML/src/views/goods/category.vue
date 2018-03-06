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
                <el-button class="fr" @click="goDetail">新增</el-button>
            </el-form>
        </div>
        <!--表格列表-->
        <div class="tableList" v-if="showFlag" style="margin-top: 20px;">
            <el-table :data="dataList" border stripe v-loading.body="loadCircle" header-row-class-name="headerRow">
            	<el-table-column prop="id" label="编号"></el-table-column>
            	<el-table-column prop="goodsName" label="品类名称"></el-table-column>
            	<el-table-column prop="createDate" label="创建时间">
            		<template slot-scope="scope">
            			{{filters.formatDatetime(scope.row.createDate)}}
            		</template>
            	</el-table-column>
            	<el-table-column prop="remark" label="备注"></el-table-column>
                <el-table-column label="操作" fixed="right" width="140">
                    <template slot-scope="scope">
                    	<el-button type="text" @click="goDetail(scope.row, 'detail')">详情</el-button>
                		<el-button type="text" @click="goDetail(scope.row, 'edit')">编辑</el-button>
                		<el-button type="text" @click="del(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
			<div class="pagination-wrapper" style="text-align: right;">
				<el-pagination layout="jumper, sizes, prev, pager, next, total" :page-count="pageCount" :currentPage="currentNo" :page-sizes="[10, 20, 30, 40]" :page-size="pageSize" :total="total" @size-change="sizeChange" @current-change="retrieve"></el-pagination>
			</div>
        </div>

        <!--详情-->
        <div v-if="!showFlag">
        	<el-row v-if="isCreating">
	            <el-form :inline="true" v-if="isDetail" label-width="100px" class="wrap-form" style="width: 1000px; padding: 20px;">
	            	<div class="settlement-div">
		                <el-form-item label="编号：" v-if="create.id">
		                    {{create.id}}
		                </el-form-item>
		                <el-form-item label="品类名称：">
		                	{{create.goodsName}}
		                </el-form-item>
		                <el-form-item label="备注信息：">
		                	{{create.remark}}
		                </el-form-item>
		                <el-form-item label="属性：">
		                	<div v-for="(item, index) in create.goodsAttrVOList">
	            				<span style="margin: 0 3px;">{{item.goodsAttrName}}</span>
	            				<i class="el-icon-sort-up" v-if="index != 0" @click="sortUp(item)"></i>
	            				<i class="el-icon-sort-down" v-if="index != create.goodsAttrVOList.length-1" @click="sortDown(item)"></i>
	            			</div>
		                </el-form-item>
	                </div>
				  	<el-row class="text-center">
	                	<el-button @click="update('edit')" v-if="isEditing">确认</el-button>
	                	<el-button @click="update" v-else>保存</el-button>
	                    <el-button @click="backTablePage">返回</el-button>
				  	</el-row>
	            </el-form>
	            <el-form :inline="true" v-else label-width="100px" style="width: 1000px; padding: 20px;">
	            	<div class="settlement-div">
		                <el-form-item label="编号：" v-if="create.id">
		                    <el-input style="width: 461px;" v-model="create.id"></el-input>
		                </el-form-item>
		                <el-form-item label="品类名称：">
		                    <el-input style="width: 461px;" v-model="create.goodsName"></el-input>
		                </el-form-item>
		                <el-form-item label="备注信息：">
						    <el-input style="width: 461px;" type="textarea" v-model="create.remark"></el-input>
		                </el-form-item>
		                <el-form-item label="属性：">
							<div v-for="item in create.goodsAttrVOList">
								<el-checkbox v-model="item.checked" :value="item.id">{{item.attrName}}</el-checkbox>
								<span style="margin-left: 10px;" v-for="item2 in item.attributeValueVOS">
									<span style="margin: 0 3px;">{{item2.attrValue}}</span>
								</span>
								<p>属性展示等级：
				                    <el-select v-model="item.attributeShowLevel" style="width: 250px;" clearable>
				                    	<el-option v-for="(item3, index) in attributeShowLevel"
				                    		:key="index"
				                    		:value="item3.value"
				                    		:label="item3.name"
				                    	></el-option>
				                    </el-select>
								</p>
							</div>
		                </el-form-item>
	                </div>
				  	<el-row class="text-center">
	                	<el-button @click="update('edit')" v-if="isEditing">确认</el-button>
	                	<el-button @click="update" v-else>保存</el-button>
	                    <el-button @click="backTablePage">返回</el-button>
				  	</el-row>
	            </el-form>
        	</el-row>
        </div>
        
    </div>
</template>

<script>
    import header from '~/components/header/header.vue'
    
    //新增构造函数
    function Create(){
    	this.goodsName = '';
    	this.remark = '';
    	this.goodsAttrVOList = [];
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
            	api: this.apiUrl.category,
            	name: '',//用户昵称
                //breadcrumb
                firsTit:'商品',
                secondTit:'商品品类',
                thirdTit:'',
                breadflag:false,
                /*header参数*/
                viewFlag:false,
                showFlag:true, //true 显示table页; false 显示form页
                loadCircle:false,//加载显示
                dataList:[],
                
                //新增
                isCreating: false,
                create: new Create(),
                attrList: [],
                attributeShowLevel: this.keys.goods.category.attributeShowLevel,
                
                //修改积分
                isEditing: false,
                edit: new Edit(),
                
                //详情
                isDetail: false,

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
            vm.getAttrList();
        },
        deactivated(){
            var vm=this;
            vm.loadCircle=false;//避免超时闪现加载图标
        },
        methods: {
//			每页数据条数变化
			sizeChange(size) {
				vm.retrieve(vm.currentNo, size);
			},
			getAttrList(){
                let params={
                    url: vm.api.attrList,
                    data:{
                        pageNumber: 1,//页码
                        pageSize: 100
                    }
                }
	            let callback = function(data){
	                vm.attrList = data.data.rows;
	                vm.attrList.map(function(item){
	                	item.checked = false;
	                })
	            }
                vm.ax.post(params, callback);
			},
			//详情 编辑
			goDetail(row, name){
				vm.showFlag = false;
				vm.create = new Create();
				vm.isCreating = true;
				if(name=='detail'){
					vm.isDetail = true;
					vm.create = row;
				}else{
					//给属性数组赋值
					let goodsAttrVOList = [];
					vm.attrList.map(function(item){
						goodsAttrVOList.push(vm.objCopy(item));
					})
				
					if(name=='edit'){
						vm.isEditing = true;
						vm.create = row;
						goodsAttrVOList.map(function(item){
							vm.create.goodsAttrVOList.map(function(item2){
								if(item.id == item2.attrId){
									item.checked = true;
									item.attributeShowLevel = item2.attributeShowLevel;
									item.attrId = item2.id;
								}
							})
						});
					}
					vm.create.goodsAttrVOList = goodsAttrVOList;
				}
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
			//排序
			sortDown(row){
                let params={
                    url: vm.api.desc,
                    data:{
                    	id: row.id
                    }
                }
	            let callback = function(data){
	                vm.getDetail();
	            }
                vm.ax.post(params, callback);
			},
			sortUp(row){
                let params={
                    url: vm.api.asc,
                    data:{
                    	id: row.id
                    }
                }
	            let callback = function(data){
	                vm.getDetail();
	            }
                vm.ax.post(params, callback);
			},
			//获取详情
			getDetail(){
                let params={
                    url: vm.api.detail,
                    data:{
                    	id: vm.create.id
                    }
                }
	            let callback = function(data){
	                vm.create = data.data;
	            }
                vm.ax.post(params, callback);
			},
			update(name) {
				//处理上传数据
				let dataCopy = vm.objCopy(vm.create);
				let goodsAttrVOList = [];
				vm.create.goodsAttrVOList.map(function(item){
					if(item.checked){
						goodsAttrVOList.push({id: item.attrId, attrId: item.id, attributeShowLevel: item.attributeShowLevel, sort: item.sort});
					}
				})
				dataCopy.goodsAttrVOList = goodsAttrVOList;
				
                let params={
                    url:vm.api.create,
                    data: dataCopy
                }
                if(name == 'edit'){
                	//新增增加排序
                	let sort = 0;
                	dataCopy.goodsAttrVOList.map(function(item){
                		item.sort > 0;
                		sort = item.sort;
                	})
                	dataCopy.goodsAttrVOList.map(function(item){
                		if(!item.sort){
                			sort++;
                			item.sort = sort;
                		}
                	})
                	params.url = vm.api.update;
                }else{
                	//新增增加排序
                	let i = 0;
                	dataCopy.goodsAttrVOList.map(function(item){
                		item.sort = i;
                		i++;
                	})
                }
	            let callback = function(data){
	                vm.$message.success("操作成功");
					vm.showFlag = true;
            		vm.isCreating = false;
            		vm.retrieve(vm.currentNo);
	            }
                vm.ax.post(params, callback, vm);
			},
            //back to tablePage
            backTablePage(){
                vm.showFlag=true;
                vm.isAdding = false;
                vm.isEditting = false;
                vm.isDetail = false;
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
	.el-icon-sort-up, .el-icon-sort-down {
		cursor: pointer;
	}
</style>
