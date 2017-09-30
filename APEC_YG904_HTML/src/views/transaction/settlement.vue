/**
* Created by wq on 2017/09/27.
* 交收单管理
*/
<template>
    <div class="certificate">
        <my-header v-on:initialPage="initialPage" :firstTitle="firsTit" :secondTitle="secondTit" :thirdTitle="thirdTit" :breadFlag="breadflag"></my-header>
        <!--搜索-->
        <div class="mysearch" v-if="showFlag">
            <el-form :inline="true" class="search">
                <el-form-item label="上传人：">
                    <el-input v-model="name" placeholder="请输入上传人"></el-input>
                </el-form-item>
                <el-form-item label="起始日期：">
										<el-date-picker type="date" placeholder="起始日期" v-model="s_start"></el-date-picker>
								</el-form-item>
                <el-form-item label="终止日期：">
                		<el-date-picker type="date" placeholder="终止日期" v-model="s_end"></el-date-picker>
                </el-form-item>
                <el-button type="primary" @click="onSearch">搜索</el-button>
                <el-button @click="onReset">重置</el-button>
            </el-form>
        </div>
        <!--表格列表-->
        <div class="tableList" v-if="showFlag" style="margin-top: 20px;">
            <el-table :data="dataList" border stripe v-loading.body="loadCircle" style="width:100%;">
                <el-table-column prop="createDate" label="上传日期">
                		<template scope="scope">
	                		 	{{scope.row.createDate|timeFilter}}
	                	</template>
                </el-table-column>
                <el-table-column prop="shipWarehouse" label="卖货方"></el-table-column>
                <el-table-column prop="saleMarket" label="买货方"></el-table-column>
                <el-table-column prop="totalNumber" label="总重量(斤)"></el-table-column>
                <el-table-column prop="totalAmount" label="合计金额(元)"></el-table-column>
                <el-table-column prop="deliveryTime" label="交收日期"></el-table-column>
                <el-table-column prop="userName" label="上传人"></el-table-column>	
                <el-table-column label="操作">
                    <template scope="scope">
                        <el-button type="text" @click="goDetail(scope.row.voucherId)">详情</el-button>
                        <el-button type="text" @click="deleteRow(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页-->
            <el-pagination class="pageNation" @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="1"
            :page-sizes="[10,15,20,25,30]" :page-size="pSize" layout="total,sizes,prev,pager,next,jumper"
            :total="pageData.totalElements" v-show="pageData.totalElements>10"></el-pagination>
        </div>
        
        <!--企业form Page-->
        <div id="formPage" v-if="!showFlag">
            <el-form ref="settlementForm" class="myformDetail" :class="{formBorder:viewFlag}">
                <el-form-item label="卖货方：">
                    <el-input v-model="settlementForm.shipWarehouse" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="买货方：">
                    <el-input v-model="settlementForm.saleMarket" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="交收日期：">
                    <el-input v-model="settlementForm.deliveryTime" :disabled="true"></el-input>
                </el-form-item>
                <el-table :data="detailTable" stripe border class="goods-table" style="width: 800px;margin-left: 30px;margin-bottom: 30px;">
							    <el-table-column prop="skuName" label="商品"></el-table-column>
							    <el-table-column prop="number" label="重量(斤)"></el-table-column>
							    <el-table-column prop="amount" label="单价(元/斤)"></el-table-column>
							    <el-table-column prop="totalAmount" label="金额(元)"></el-table-column>
						  	</el-table>
                <el-form-item class="btnGroup">
                    <el-button @click="backTablePage">返回</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    import header from '~/components/header/header.vue'
    export default{
        data(){
            return {
                //breadcrumb
                firsTit:'交易',
                secondTit:'交收单管理',
                thirdTit:'',
                breadflag:false,
                /*header参数*/
                viewFlag:false,
//              editFlag:false,//表单编辑标识
//              addFlag:false,//表单新增标识
                showFlag:true, //true 显示table页; false 显示form页
                name:null,
                s_start:null,
                s_end:null,
                loadCircle:false,//加载显示
                dataList:[],
                detailTable:[],//详细表格数据
                pageData:[],//分页数据
                pSize:10 ,//页容量
                pNum:1, //页码
                settlementForm:{},
            }
        },
        activated(){
            var vm=this;
            vm.getTableList();
        },
        deactivated(){
            var vm=this;
            vm.loadCircle=false;//避免超时闪现加载图标
        },

        methods: {
        	//查看详情
        		goDetail(id){
        				var vm=this;
                vm.viewFlag=true;
                vm.thirdTit='详情';
                vm.breadflag=true;
                let params={
                    url:vm.apiUrl.settlement.tableDetailUrl,
                    data:{
                        voucherId:id 
                    }
                }
                vm.ax.post(params,vm.tableDetailCb);
        		},
        		deleteRow(row){
        				var vm=this;
        				this.$confirm('此操作将永久取消该交收单, 是否继续?', {
				          confirmButtonText: '确定',
				          cancelButtonText: '取消',
				          type: 'warning'
				        }).then(() => {
	        				let params={
	                    url:vm.apiUrl.settlement.deletUrl,
	                    data:{
	                        voucherId:row.voucherId,
	                        userId:row.userId
	                    }
	               	};
	               	vm.ax.post(params,vm.deleteOk);
              	})
        		},
        		deleteOk(){
        			var vm = this;
        			this.$message({
			          message: '删除成功！',
			          type: 'success'
			        });
        			vm.getTableList;
        		},
        		tableDetailCb(data){
		            var vm=this;
		            vm.showFlag=false;//显示form 隐藏table
		            vm.settlementForm.shipWarehouse=data.data.shipWarehouse;
		            vm.settlementForm.saleMarket=data.data.saleMarket;
		            vm.settlementForm.deliveryTime=data.data.deliveryTime;
		            vm.detailTable = data.data.voucherGoodsVVO;
            },
            //back to tablePage
            backTablePage(){
                var vm=this;
                vm.showFlag=true;
                vm.initialPage();//初始化页面标志
                vm.getTableList();//获取table数据
            },
            //初始化页面
            initialPage(){
                var vm=this;
                vm.viewFlag=false;//表单只读标识
                vm.showFlag=true;//显示列表，隐藏表单
                vm.breadflag=false;
                vm.title='交收单管理';
            },
            //页码改变
            handleCurrentChange(val){
                var vm=this;
                vm.pNum=val;
                vm.getTableList();
            },
            //pageSize改变
            handleSizeChange(val){
                var vm=this;
                vm.pSize=val;
                vm.getTableList();
            },
            //获取列表数据
            getTableList(){
                var vm=this;
                let params={
                    url:vm.apiUrl.settlement.tableUrl,
                    data:{
                    		userName:vm.name,
                    		startDate:vm.s_start,
                    		endDate:vm.s_end,
                        pageNumber:vm.pNum,//页码
                        pageSize:vm.pSize
                    }
                }
                vm.ax.post(params,vm.tableListCb,vm);
            },
            tableListCb(data){
                var vm=this;
                vm.pageData=data.data;
                vm.dataList=data.data.rows;
            },
            //搜索
            onSearch(){
                var vm=this;
                vm.getTableList();
            },
            //重置
            onReset(){
                var vm=this;
                vm.name=null;
                vm.s_start=null;
                vm.s_end=null;
                vm.getTableList();
            }
        },
        components: {
            'my-header':header
        }
    }
</script>

<style scoped>
</style>
