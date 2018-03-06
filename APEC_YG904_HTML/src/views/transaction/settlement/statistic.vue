/**
* Created by wq on 2017/09/27.
* 交收单管理
*/
<template>
    <div class="certificate">
        <my-header v-on:initialPage="initialPage" :firstTitle="firsTit" :secondTitle="secondTit" :thirdTitle="thirdTit" :breadFlag="breadflag"></my-header>
        <!--搜索-->
        <div class="mysearch" v-if="showFlag">
            <el-form :inline="true" class="search clearfix">
                <el-form-item label="上传人：">
                    <el-input v-model="name" placeholder="请输入上传人"></el-input>
                </el-form-item>
                <el-form-item label="起始日期：">
										<el-date-picker type="date" placeholder="起始日期" v-model="s_start"></el-date-picker>
								</el-form-item>
                <el-form-item label="终止日期：">
                		<el-date-picker type="date" placeholder="终止日期" v-model="s_end"></el-date-picker>
                </el-form-item>
				<!--<el-form-item label="状态：">
					<el-select v-model="s_auditState" placeholder="请选择状态">
				      <el-option label="未审核" value="0"></el-option>
				      <el-option label="审核通过" value="Y"></el-option>
				      <el-option label="审核驳回" value="N"></el-option>
				    </el-select>
				</el-form-item>-->
                <el-button type="primary" @click="onSearch">搜索</el-button>
                <el-button @click="goBack">返回</el-button>
                <el-button @click="onReset">重置</el-button>
            </el-form>
        </div>
        <!--表格列表-->
        <div class="tableList" v-if="showFlag">
            <el-table :data="dataList" border stripe v-loading.body="loadCircle" header-row-class-name="headerRow"  >
            	<el-table-column prop="userId" label="用户id"></el-table-column>
            	<el-table-column prop="userName" label="用户名称"></el-table-column>
                <el-table-column prop="createDate" label="上传日期">
                </el-table-column>
                <el-table-column prop="mobile" label="手机号"></el-table-column>
                <el-table-column prop="sumCount" label="总条数"></el-table-column>
                <!--<el-table-column prop="totalNumber" label="总重量(斤)"></el-table-column>-->
                <el-table-column prop="totalNumber" label="总数量"></el-table-column>
                <el-table-column prop="totalAmount" label="合计金额(元)"></el-table-column>
				<el-table-column prop="userType" label="用户身份">
            		<template slot-scope="scope">
            		 	{{scope.row.userType|userTypeFilter}}
                	</template>
				</el-table-column>
            </el-table>
            <fe-page @retrieve="getTableList" :data="pageData"></fe-page>
            
        </div>
        
    </div>
</template>

<script>
    import header from '~/components/header/header.vue'
    import FePage from '~/components/pagination.vue'
    
    //新增构造函数
    function Add(){
    	this.cityId = '';
    	this.countyId = '';
    	this.townId = '';
    	this.type = '';
    	this.shipWarehouse = '';
    	this.saleMarket = '';
    	this.name = '';
    	this.deliveryTime = '';
    	this.voucherUrl = '';
    	this.voucherGoodsVO = [];
    	this.userId = '';
    	this.isSystem = true;
    	//需删除属性
    	this.address = [];
    	this.addressIds = '';
    	this.voucherGoodsVOObj = {};
    	
    	this.addsku = false;
    	this.choose = false;
    }
    let vm;
    export default{
        data(){
            return {
                //breadcrumb
                firsTit:'交易',
                secondTit:'统计报表',
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
                s_auditState: null,
                loadCircle:false,//加载显示
                dataList:[],
                detailTable:[],//详细表格数据
                pageData:[],//分页数据
                pSize:10 ,//页容量
                pNum:1, //页码
                settlementForm:{},
                
                //新增部分
                isAdding: false,
                attrList: [], //规格列表
                userList: [], //客户列表
                typeList: this.keys.transaction.settlement.typeList,
                radioForm: {},//选中属性绑定
                //级联选择器需要的属性
                options: [{
		          name: '烟台市',
		          code: 3706,
		          cities: []
		        },{
		          name: '威海市',
		          code: 3710,
		          cities: []
		        }],
                props: {
		            value: 'code',
		            label: 'name',
		            children: 'cities'
		        },
		        isWriteAddress: false,
		        //统计数据
		        statisticData: {
		        	totalAmount: 0,
		        	totalNumber: 0
		        }
            }
        },
        created(){
            vm = this;
        },
        deactivated(){
            vm.loadCircle=false;//避免超时闪现加载图标
        },
        filters: {
        	userTypeFilter:function(val){
        		return val == "KS"?"客商":"果农";
        	}
        },

        methods: {
    		//新增
    		goBack(){
                this.$router.push("settlement");
    		},
            //back to tablePage
            backTablePage(){
                vm.showFlag=true;
                vm.isAdding = false;
                vm.initialPage();//初始化页面标志
                vm.getTableList(1);//获取table数据
            },
            //初始化页面
            initialPage(){
                vm.viewFlag=false;//表单只读标识
                vm.showFlag=true;//显示列表，隐藏表单
                vm.breadflag=false;
                vm.title='统计报表';
            },
            //获取列表数据
            getTableList(pNum, pSize){
            	if(pSize) {
            		vm.pSize = pSize;
            	}
                let params={
                    url: vm.apiUrl.settlement.statisticReport,
                    data:{
                    		userName:vm.name,
                    		startDate:vm.s_start,
                    		endDate:vm.s_end,
                        pageNumber: pNum,//页码
                        pageSize: vm.pSize
                    }
                }
                vm.ax.post(params,vm.tableListCb,vm);
            },
            tableListCb(data){
                vm.pageData=data.data;
                vm.dataList=data.data.rows;
            },
            //搜索
            onSearch(){
                vm.getTableList(1);
            },
            //重置
            onReset(){
                vm.name=null;
                vm.s_start=null;
                vm.s_end=null;
                vm.s_auditState = null;
                vm.getTableList(1);
            }
        },
        components: {
            'my-header':header,
            'fe-page': FePage
        }
    }
</script>

<style scoped>
</style>
