<template>
    <div class="certificate">
        <my-header v-on:initialPage="initialPage" :firstTitle="firsTit" :secondTitle="secondTit" :thirdTitle="thirdTit" :breadFlag="breadflag"></my-header>
        <!--搜索-->
        <div class="mysearch" v-if="showFlag">
            <!--<el-form :inline="true" class="search clearfix">
                <el-form-item label="举报人姓名：">
                    <el-input v-model="query.informantUser" placeholder="请输入"></el-input>
                </el-form-item>
                <el-form-item label="被举报人姓名：">
                    <el-input v-model="query.reportedUser" placeholder="请输入"></el-input>
                </el-form-item>
                <el-form-item label="关联数据来源：">
	                <el-select v-model="query.realm">
	                	<el-option
	                		v-for="(item, index) in realmList"
	                		:key="index"
	                		:value="item.value"
	                		:label="item.name"
	                	></el-option>
	                </el-select>
                </el-form-item>
                <el-button type="primary" @click="retrieve(1)">搜索</el-button>
                <el-button @click="toClear">重置</el-button>
            </el-form>-->
        </div>
        <!--表格列表-->
        <div class="tableList" v-if="showFlag" style="margin-top: 20px;">
            <el-table :data="dataList" border stripe v-loading.body="loadCircle" header-row-class-name="headerRow"  >
            	<el-table-column prop="id" label="排序"></el-table-column>
            	<el-table-column prop="userLevelName" label="等级头衔"></el-table-column>
            	<el-table-column label="图片">
            		<template slot-scope="scope">
            			<img :src="scope.row.url" v-if="scope.row.url"/>
            		</template>
            	</el-table-column>
            	<el-table-column prop="point" label="积分">
            	</el-table-column>
            	<el-table-column prop="frezzing" label="状态">
            		<template slot-scope="scope">
            			{{scope.row.frezzing?"禁用":"启用"}}
            		</template>
            	</el-table-column>
            	<el-table-column prop="remark" label="备注"></el-table-column>
                <el-table-column label="操作" fixed="right" width="100">
                    <template slot-scope="scope">
                		<el-button type="text" @click="goDetail(scope.row)">编辑</el-button>
                		<el-button type="text" v-if="scope.row.frezzing" @click="forbidden(scope.row)">启用</el-button>
                		<el-button type="text" v-else @click="forbidden(scope.row, 'true')">禁用</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页-->
			<div class="pagination-wrapper" style="text-align: right;">
				<el-pagination layout="jumper, sizes, prev, pager, next, total" :page-count="pageCount" :currentPage="currentNo" :page-sizes="[10, 20, 30, 40]" :page-size="pageSize" :total="total" @size-change="sizeChange" @current-change="retrieve"></el-pagination>
			</div>
        </div>

        <!-- Page-->
        <div v-if="!showFlag">
        	<el-row v-if="isDetail">
	            <el-form :inline="true" label-width="100px" style="width: 700px; padding: 20px;">
	            	<div class="settlement-div">
		                <el-form-item label="头衔：">
		                    <el-input style="width: 461px;" v-model="detail.userLevelName" disabled></el-input>
		                </el-form-item>
		                <el-form-item label="图片路径：">
		                    <el-input style="width: 461px;" v-model="detail.url"></el-input>
		                </el-form-item>
		                <el-form-item label="要求积分：">
						    <el-input style="width: 461px;" v-model="detail.point" @input="numberOnly"></el-input>
		                </el-form-item>
		                <el-form-item label="备注：">
							<el-input style="width: 461px;" type="textarea" v-model="detail.remark"></el-input>
		                </el-form-item>
		                <el-form-item label="是否启用：">
		                    <el-switch
							  v-model="detail.frezzing"
							  active-text="否"
							  inactive-text="是"
							  active-color="#ff4949"
							  inactive-color="#13ce66">
							</el-switch>
		                </el-form-item>
	                </div>
				  	<el-row class="text-center">
	                	<el-button @click="update">保存</el-button>
	                    <el-button @click="backTablePage">返回</el-button>
				  	</el-row>
	            </el-form>
        	</el-row>
        </div>
        
    </div>
</template>

<script>
    import header from '~/components/header/header.vue'
    import moment from 'moment'
    
    //查询构造函数
    function Query(){
    	this.informantUser = '';
    	this.reportedUser = '';
    	this.realm = '';
    }
    //详情构造函数
    function Detail(){
    	this.id = '';
    	this.list = [];
    }
    
    let vm;
    export default{
        data(){
            return {
            	api: this.apiUrl.level,
                //breadcrumb
                firsTit:'积分',
                secondTit:'等级设置',
                thirdTit:'',
                breadflag:false,
                /*header参数*/
                viewFlag:false,
                showFlag:true, //true 显示table页; false 显示form页
                loadCircle:false,//加载显示
                dataList:[],

				//查询
                isDetail: false,
                detail: new Detail(),

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
			goDetail(row){
				vm.showFlag = false;
				vm.detail = row;
				vm.isDetail = true;
			},
			update() {
                let params={
                    url:vm.api.update,
                    data: vm.detail
                }
	            let callback = function(data){
					vm.showFlag = true;
					vm.isDetail = false;
					vm.$message.success("操作成功");
					vm.retrieve(vm.currentNo);
	            }
                vm.ax.post(params, callback);
			},
			//删除
			forbidden(row, bool){
				var callback = function(data){
					vm.$message.success("操作成功");
					vm.retrieve(vm.currentNo);
				}
				bool = bool=='true'?true:false;
                //确认删除框
                vm.$confirm('确认执行吗？','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(()=>{
                    //删除方法
                    let params={
                        url:vm.api.update,
                        data:{
                            id: row.id, //用户id
                            frezzing: bool
                        }
                    };
	                vm.ax.post(params,callback);
	            }).catch(()=>{
	                    console.log("已取消执行");
	            });
			},
            //back to tablePage
            backTablePage(){
                vm.showFlag=true;
                vm.isAdding = false;
                vm.isEditting = false;
                vm.initialPage();//初始化页面标志
                vm.retrieve(vm.currentNo);//获取table数据
            },
            //初始化页面
            initialPage(){
                vm.showFlag=true;//显示列表，隐藏表单
                vm.breadflag=false;
                vm.title='交收单管理';
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
