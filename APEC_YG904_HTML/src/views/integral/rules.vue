<template>
    <div class="certificate">
        <my-header v-on:initialPage="initialPage" :firstTitle="firsTit" :secondTitle="secondTit" :thirdTitle="thirdTit" :breadFlag="breadflag"></my-header>
        <!--搜索-->
        <!--<div class="mysearch" v-if="showFlag">
            <el-form :inline="true" class="search clearfix">
                <el-button class="fr" @click="">新增</el-button>
            </el-form>
        </div>-->
        <!--表格列表-->
        <div class="tableList" v-if="showFlag" style="margin-top: 20px;">
            <el-table :data="dataList" border stripe v-loading.body="loadCircle" header-row-class-name="headerRow"  >
            	<el-table-column prop="id" label="序号"></el-table-column>
            	<el-table-column prop="pointRuleName" label="名称"></el-table-column>
            	<el-table-column prop="pointsChanged" label="分值"></el-table-column>
            	<el-table-column prop="dailyNum" label="每日上线次数"></el-table-column>
            	<el-table-column prop="firstReward" label="首次奖励"></el-table-column>
            	<el-table-column prop="remark" label="备注"></el-table-column>
                <el-table-column label="操作" fixed="right" width="100">
                    <template slot-scope="scope">
                		<el-button type="text" @click="goDetail(scope.row)">编辑</el-button>
                		<el-button type="text" @click="del(scope.row)">删除</el-button>
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
	            <el-form :inline="true" label-width="120px" style="width: 700px; padding: 20px;">
	            	<div class="settlement-div">
		                <el-form-item label="名称：">
		                    <el-input style="width: 461px;" v-model="detail.pointRuleName" disabled></el-input>
		                </el-form-item>
		                <el-form-item label="分值：">
		                    <el-input style="width: 461px;" v-model="detail.pointsChanged"></el-input>
		                </el-form-item>
		                <el-form-item label="每日上线次数：">
		                    <el-input style="width: 461px;" @input="numberOnly" v-model="detail.dailyNum"></el-input>
		                </el-form-item>
		                <el-form-item label="首次奖励：">
		                    <el-input style="width: 461px;" @input="numberOnly" v-model="detail.firstReward"></el-input>
		                </el-form-item>
		                <el-form-item label="备注：">
							<el-input style="width: 461px;" type="textarea" v-model="detail.remark"></el-input>
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
    	this.pointsChanged= '';
    	this.pointsChangedType = '';
    	this.dailyNum = '';
    	this.firstReward = '';
    }
    let vm;
    export default{
        data(){
            return {
            	api: this.apiUrl.rules,
                //breadcrumb
                firsTit:'积分',
                secondTit:'积分规则',
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
				let detailCopy = vm.objCopy(vm.detail);
				detailCopy.pointsChangedType = 'PLUS';
				if(vm.detail.pointsChanged < 0){
					detailCopy.pointsChanged = vm.detail.pointsChanged.replace(/\-/,"");
					detailCopy.pointsChangedType = 'REDUCTION';
				}
                let params={
                    url:vm.api.update,
                    data: detailCopy
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
			del(row){
				var callback = function(data){
					vm.$message.success("操作成功");
					vm.retrieve(vm.currentNo);
				}
                //确认删除框
                vm.$confirm('确认删除吗？','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(()=>{
                    //删除方法
                    let params={
                        url:vm.api.del,
                        data:{
                            id: row.id
                        }
                    };
	                vm.ax.post(params,callback);
	            }).catch(()=>{
	                    console.log("已取消删除");
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
	                vm.dataList.map(function(item){
	                	item.pointsChanged = item.pointsChangedType == 'REDUCTION'?'-'+item.pointsChanged:'+'+item.pointsChanged;
	                })
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
