/**
* Created by wq on 2017/09/27.
* 交收单管理
*/
<template>
    <div class="certificate">
        <my-header v-on:initialPage="initialPage" :firstTitle="firsTit" :secondTitle="secondTit" :thirdTitle="thirdTit" :breadFlag="breadflag"></my-header>
        <!--搜索-->
        <div class="mysearch" v-if="showFlag">
            <el-form :inline="true" class="search clearfix" label-width="100px">
                <el-form-item label="上传人：">
                    <el-input v-model="name" placeholder="请输入上传人"></el-input>
                </el-form-item>
                <el-form-item label="起始日期：">
										<el-date-picker type="date" placeholder="起始日期" v-model="s_start"></el-date-picker>
								</el-form-item>
                <el-form-item label="终止日期：">
                		<el-date-picker type="date" placeholder="终止日期" v-model="s_end"></el-date-picker>
                </el-form-item>
				<el-form-item label="状态：">
					<el-select v-model="s_auditState" placeholder="请选择状态">
				      <el-option label="未审核" value="0"></el-option>
				      <el-option label="审核通过" value="Y"></el-option>
				      <el-option label="审核驳回" value="N"></el-option>
				    </el-select>
				</el-form-item>
                <el-button type="primary" @click="onSearch">搜索</el-button>
                <el-button type="primary" @click="goStatistic">统计报表</el-button>
                <el-button @click="onReset">重置</el-button>
                <el-button @click="goCreate">新增</el-button>
            </el-form>
        </div>
        <el-row style="margin: 10px 0;" v-if="showFlag">
	        <el-button disabled style="color: #fa5555;">总数量：{{statisticData.totalNumber.toFixed(2)}}</el-button>
	        <el-button disabled style="color: #fa5555;">总金额：{{statisticData.totalAmount.toFixed(2)}}</el-button>
        </el-row>
        <!--表格列表-->
        <div class="tableList" v-if="showFlag">
            <el-table :data="dataList" border stripe v-loading.body="loadCircle" header-row-class-name="headerRow"  >
                <el-table-column prop="createDate" label="上传日期">
                		<template slot-scope="scope">
	                		 	{{scope.row.createDate|timeFilter}}
	                	</template>
                </el-table-column>
                <el-table-column prop="shipWarehouse" label="卖货方"></el-table-column>
                <el-table-column prop="saleMarket" label="买货方"></el-table-column>
                <el-table-column prop="name" label="买货姓名"></el-table-column>
                <el-table-column prop="totalNumber" label="总重量(斤)"></el-table-column>
                <el-table-column prop="totalAmount" label="合计金额(元)"></el-table-column>
                <el-table-column prop="deliveryTime" label="交收日期"></el-table-column>
                <el-table-column prop="auditState" label="状态"></el-table-column>
				<el-table-column prop="mobile" label="手机号"></el-table-column>
                <el-table-column prop="userName" label="上传人">
					<template slot-scope="scope">
            			<a style="text-decoration: underline; cursor: pointer;color: #20A0FF;" @click="goto(scope.row, 'settlement')">{{scope.row.userName}}</a>
					</template>
                </el-table-column>
                <el-table-column label="操作" fixed="right" width="200">
                    <template slot-scope="scope">
                        <el-button type="text" @click="goDetail(scope.row)">详情</el-button>
                        <el-button type="text" @click="deleteRow(scope.row)">删除</el-button>
                        <el-button type="text" v-if="scope.row.auditState == '未审核'" @click="approve(scope.row, 'Y')">审核</el-button>
                        <el-button type="text" v-if="scope.row.auditState == '未审核'" @click="approve(scope.row, 'N')">驳回</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页-->
            <el-pagination class="pageNation" @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pNum"
            :page-sizes="[10,15,20,25,30]" :page-size="pSize" layout="total,sizes,prev,pager,next,jumper"
            :total="pageData.totalElements" v-show="pageData.totalElements>10"></el-pagination>
        </div>

        <!--企业form Page-->
        <div v-if="!showFlag&&!isAdding">
            <el-form :inline="true" ref="settlementForm" label-width="100px" style="width: 800px; padding: 20px;" :class="{formBorder:viewFlag}">
            	<div class="settlement-div">
            		<h1>卖货方</h1>
	                <!--<el-form-item label="卖货方：">
	                    <el-input v-model="settlementForm.shipWarehouse" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="买货方：">
	                    <el-input v-model="settlementForm.saleMarket" :disabled="viewFlag"></el-input>
	                </el-form-item>-->
	                <el-form-item label="货源：">
	                    <el-input v-bind:value="settlementForm.type | typeFormatter" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="地址：">
	                    <el-input v-model="settlementForm.address" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="出货冷库：">
	                    <el-input v-model="settlementForm.shipWarehouse" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <h1>买货方</h1>
	                <el-form-item label="销售市场：">
	                    <el-input v-model="settlementForm.saleMarket" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="买方姓名：">
	                    <el-input v-model="settlementForm.name" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="交收日期：">
	                    <el-input v-model="settlementForm.deliveryTime" :disabled="true"></el-input>
	                </el-form-item>
                </div>
                <el-table   :data="detailTable" stripe border class="goods-table" style="width: 800px;margin-left: 30px;margin-bottom: 30px;">
							    <el-table-column prop="skuName" label="商品"></el-table-column>
							    <el-table-column prop="number" label="重量(斤)"></el-table-column>
							    <el-table-column prop="amount" label="单价(元/斤)"></el-table-column>
							    <el-table-column prop="totalAmount" label="金额(元)"></el-table-column>
						  	</el-table>
                <el-form-item class="btnGroup">
                	<el-row class="text-center" style="width: 800px;">
                		<el-button @click="backTablePage">返回</el-button>
                	</el-row>
                </el-form-item>
            </el-form>
        </div>
        
        <!--新增-->
        <div v-if="!showFlag&&isAdding">
            <el-form :inline="true" label-width="100px" style="width: 800px; padding: 20px;" :class="{formBorder:viewFlag}">
            	<div class="settlement-div">
            		<h1>卖货方</h1>
	                <el-form-item label="货源：">
						<el-select v-model="add.type" placeholder="请选择货源" filterable>
							<el-option v-for="(item, index) in typeList" :key="index" :label="item.name" :value="item.value"></el-option>
						</el-select>
	                </el-form-item>
	                <el-form-item label="地址：">
	                    <el-cascader
							id="address"
						    :options="options"
						    v-model="add.address"
						    :props="props"
						    @active-item-change="handleItemChange"
						    @change="handleItemClick"
							expand-trigger="hover"
						>
						</el-cascader>
	                </el-form-item>
	                <el-form-item label="出货冷库：">
	                    <el-input v-model="add.shipWarehouse"></el-input>
	                </el-form-item>
	                <h1>买货方</h1>
	                <el-form-item label="销售市场：">
	                    <el-input v-model="add.saleMarket"></el-input>
	                </el-form-item>
	                <el-form-item label="买方姓名：">
	                    <el-input v-model="add.name"></el-input>
	                </el-form-item>
	                <el-form-item label="交收日期：">
						<el-date-picker type="date" v-model="add.deliveryTime"></el-date-picker>
	                </el-form-item>
	                <el-form-item label="上传人：">
						<el-select v-model="add.userId" placeholder="请选择上传人" filterable>
							<el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
						</el-select>
	                </el-form-item>
                </div>
                <el-row style="text-align: right;width: 830px;">
                	<el-button type="text" @click="goAddSku">新增</el-button>
                	<el-button type="text" @click="deleteSku" style="color: #ef3535;">删除</el-button>
                </el-row>
                <el-table :data="add.voucherGoodsVO" ref="multipleTable" @selection-change="handleSelectionChange" stripe border class="goods-table" style="width: 800px;margin-left: 30px;margin-bottom: 30px;">
					<el-table-column type="selection" width="55" fixed="left"></el-table-column>
				    <el-table-column prop="skuName" label="商品"></el-table-column>
				    <el-table-column prop="number" label="重量(斤)"></el-table-column>
				    <el-table-column prop="amount" label="单价(元/斤)"></el-table-column>
				    <el-table-column prop="totalAmount" label="金额(元)"></el-table-column>
			  	</el-table>
			  	<el-row class="text-center">
                	<el-button @click="createSubmit">提交</el-button>
                    <el-button @click="backTablePage">返回</el-button>
			  	</el-row>
            </el-form>
        </div>
        
		<el-dialog title="新增商品" :visible.sync="add.addsku" width="500px">
			<el-form :inline="true" label-width="130px">
                <el-form-item label="选择商品">
					{{add.newSku.skuName}}
					<el-button type="warning" @click="showAttrDialog">选择规格</el-button>
                </el-form-item>
                <el-form-item label="数量（斤）">
                    <el-input v-model="add.newSku.number" style="width: 280px;" v-on:input="calAmount"></el-input>
                </el-form-item>
                <el-form-item label="单价（元/斤）">
                    <el-input v-model="add.newSku.amount" style="width: 280px;" v-on:input="calAmount"></el-input>
                </el-form-item>
                <el-form-item label="金额（元）">
                    <el-input v-model="add.newSku.totalAmount" style="width: 280px;" disabled></el-input>
                </el-form-item>
                <div class="text-center">
                	<el-button type="primary" @click="addskuConfirm">提交</el-button>
                	<el-button @click="addskuCancel">关闭</el-button>
                </div>
            </el-form>
		</el-dialog>
		<el-dialog title="选择规格" :visible.sync="add.choose">
			<ul>
				<li v-for="attr in attrList">
					<div class="attr-title">{{attr.goodsAttrName}}<span>(必选项)</span></div>
					<div>
						<el-radio-group v-model="radioForm[attr.attrId]">
							<el-radio-button v-for="value in attr.attributeValueVOS" :key="value.id" :label="value.id">{{value.attrValue}}</el-radio-button>
						</el-radio-group>
					</div>
				</li>
			</ul>
			<div class="text-center" style="margin-top: 15px;">
				<el-button type="primary" @click="setAttr()">保 存</el-button>
				<el-button @click="chooseSpecCancel">取 消</el-button>
			</div>
		</el-dialog>
    </div>
</template>

<script>
    import header from '~/components/header/header.vue'
    
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
    	this.newSku = new Sku();
    	this.selectVal = [];//选中的商品
    }
    //新增商品
    function Sku(){
    	this.id = '';
    	this.skuName = '';
    	this.skuId = '';
    	this.number = '';
    	this.amount = '';
    	this.totalAmount = '';
    }
    let vm;
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
                add: new Add(),
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
        activated(){
            vm = this;
            vm.getTableList();
            vm.initAttr();
            vm.initUser();
			setTimeout(function(){
				vm.checkFromWhistleBlowing(vm,'goDetail','voucherId');//检查是否从交收单过来
			},1)
        },
        deactivated(){
            var vm=this;
            vm.loadCircle=false;//避免超时闪现加载图标
        },
        filters: {
        	typeFormatter:function(val){
        		return val == "KS"?"客商":"果农";	
        	}
        },

        methods: {
        	//查看详情
    		goDetail(row){
                vm.viewFlag=true;
                vm.thirdTit='详情';
                vm.breadflag=true;
                let params={
                    url:vm.apiUrl.settlement.tableDetailUrl,
                    data:{
                        voucherId: row.voucherId
                    }
                }
                vm.ax.post(params,vm.tableDetailCb);
    		},
    		goStatistic(){
    			this.$router.push('statistic');
    		},
    		deleteRow(row){
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
          		}).catch(()=>{});
    		},
    		//审核与驳回
    		approve(row, val){
				this.$confirm('确定进行该操作?', {
		          confirmButtonText: '确定',
		          cancelButtonText: '取消',
		          type: 'warning'
		       }).then(() => {
    				let params={
		                url: vm.apiUrl.settlement.approve,
		                data:{
		                    id: row.voucherId,
		                    auditState: val
		                }
	               	};
	               	console.log(row)
	               	let callback = function(){
			          	vm.$message.success('操作成功');
			          	vm.getTableList();
	               	}
	               	vm.ax.post(params,callback);
          		}).catch(()=>{});
    		},
    		//新增
    		goCreate(){
                vm.thirdTit='新增';
                vm.breadflag=true;
	            vm.showFlag=false;//显示form 隐藏table
                vm.isAdding = true;
                vm.add = new Add();
    		},
    		//新增提交
    		createSubmit(){
    			if(vm.add.addressIds.length>1&&vm.add.deliveryTime){
    				vm.add.cityId = vm.add.addressIds[0];
    				vm.add.countyId = vm.add.addressIds[1];
    				vm.add.townId = vm.add.addressIds[2]?vm.add.addressIds[2]:"";//部分地址只有二级
    				
	    			var addCopy = vm.objCopy(vm.add);
	    			delete addCopy.address;
	    			delete addCopy.addressIds;
	    			delete addCopy.voucherGoodsVOObj;
	    			delete addCopy.addsku;
	    			delete addCopy.choose;
	    			delete addCopy.newSku;
	    			delete addCopy.selectVal;
	    			
	    			console.log(addCopy)
	    			console.log(vm.add)
	    			addCopy.deliveryTime = vm.add.deliveryTime;//需要赋值，否则copy不到
			        var formData = new FormData();
			        var data = JSON.stringify(addCopy);
			        formData.append(data,'');
	                let params={
	                    url:vm.apiUrl.settlement.create,
	                    data: formData
	                }
	                let callback = function(data){
	    				vm.$message.success("新增成功");
	    				vm.showFlag=true;//显示form 隐藏table
                		vm.isAdding = false;
                		vm.thirdTit = '',
                		vm.title = '交收单管理'
			            vm.getTableList();
	                }
	                
	                try {
			            vm.ax.postImg(params).then((res)=>{
			              var item = res.data;
				          if (item.succeed) {
				            callback();
				          } else {
				          	vm.$message.error(item.errorMsg);
				          }
			        	}).catch((error)=>{
			            	console.log(error)
			        	})
			        } catch (error) {
			          console.log(error)
			        }
    			}else if(!vm.add.deliveryTime){
    				vm.$message.error("请选择时间");
    			}else if(vm.add.addressIds.length<1){
    				vm.$message.error("请选择地址");
    			}
    		},
    		//新增商品
    		goAddSku(){
    			if(vm.add.userId){
	    			vm.add.addsku = true;
	    			vm.add.newSku = new Sku();//重置商品
    			}else{
    				vm.$message.error("请先选择上传人");
    			}
    		},
    		//新增商品确认
    		addskuConfirm(){
    			if(!vm.add.newSku.skuId||!vm.add.newSku.skuName){
    				vm.$message.error("请选择规格");
    			}else if(!vm.add.newSku.number.match(/(^[1-9](\d+)?(\.\d{1,2})?$)|(^(0){1}$)|(^\d\.\d{1,2}?$)/g)){
    				vm.$message.error("请正确填写数量");
    			}else if(!vm.add.newSku.amount.match(/(^[1-9](\d+)?(\.\d{1,2})?$)|(^(0){1}$)|(^\d\.\d{1,2}?$)/g)){
    				vm.$message.error("请正确填写单价");
    			}else {
    				vm.add.addsku = false;
    				let newId = JSON.stringify(new Date().getTime());
    				vm.add.newSku.id = newId;
    				vm.add.voucherGoodsVOObj[newId] = vm.add.newSku;
    				vm.add.voucherGoodsVO.push(vm.add.newSku);
    			}
    		},
    		//新增商品取消
    		addskuCancel(){
    			vm.add.addsku = false;
    		},
    		//删除选中商品
    		deleteSku(){
    			var selections = vm.add.selectVal;
    			if(selections.length==0){
    				vm.$message.error("请先选择一个");
    			}else{
    				var voucherGoodsVOObj = vm.add.voucherGoodsVOObj;
    				selections.map(function(item){
    					delete voucherGoodsVOObj[item.id];
    				});
    				vm.add.voucherGoodsVO = [];
    				for(var i in voucherGoodsVOObj){
    					vm.add.voucherGoodsVO.push(voucherGoodsVOObj[i]);
    				}
    				vm.add.selectVal = [];//清空选中
    			}
    		},
    		//计算规格
    		calAmount(){
    			if(vm.add.newSku.amount&&vm.add.newSku.number){
    				if(vm.add.newSku.amount.match(/(^[1-9](\d+)?(\.\d{1,2})?$)|(^(0){1}$)|(^\d\.\d{1,2}?$)/g) && 
    				vm.add.newSku.number.match(/(^[1-9](\d+)?(\.\d{1,2})?$)|(^(0){1}$)|(^\d\.\d{1,2}?$)/g)){
    					vm.add.newSku.totalAmount = vm.add.newSku.amount*vm.add.newSku.number;
    				}
    			}
    		},
    		//弹出选择规格
			showAttrDialog(){
				//选中规格的第一个；
//  			vm.attrList.map(function(item){
//  				item.attributeValueVOS.map(function(item2, index){
//  					if(index==0){
//  						vm.radioForm[item.attrId] = item2.id;
//  					}
//  				});
//  			});
				vm.add.choose = true;
			},
			//关闭选择规格框
			chooseSpecCancel(){
				vm.add.choose = false;
			},
			//初始化规格
			initAttr() {
				var vm = this;
				let params = {
					url: vm.apiUrl.supplyDemand.getAttrUrl,
					data: {}
				}
				vm.ax.post(params, function(data) {
					var list = [];
					var colorItem;
					data.data.goodsAttrVOList.map(function(item){
						if(item.goodsAttrName == "商品"||item.goodsAttrName == "规格"||item.goodsAttrName == "等级"){
							list.push(item);
						}
						if(item.goodsAttrName == "颜色"){
							colorItem = item;
						}
					})
					list.push(colorItem);
					
					vm.attrList = list;
				});
			},
			//获取客户列表
			initUser() {
				var vm = this;
				let params = {
					url: vm.apiUrl.supplyDemand.getUserUrl,
					data: {}
				}
				vm.ax.post(params, function(data) {
					vm.userList = data.data;
				});
			},
			setAttr() {
				var vm = this;
				var productAttrs = [];
				var oneAttr = {};
				var thisI = 0;
				console.log(vm.radioForm)
				for(var key in vm.radioForm) { //循环选中属性绑定
					thisI++;
					if(vm.radioForm[key]) { //如果该属性有选中属性值
						for(var i in vm.attrList) { //循环属性列表
							if(vm.attrList[i].attrId == key) { //找到属性id相同的一条
								oneAttr = {};
								oneAttr.attrId = key;
								oneAttr.attrValueId = vm.radioForm[key];
								oneAttr.attrName = vm.attrList[i].goodsAttrName;
								oneAttr.attributeType = vm.attrList[i].attributeType;
								oneAttr.attributeShowLevel = vm.attrList[i].attributeShowLevel;
								oneAttr.sort = vm.attrList[i].sort;
								var attrValueList = vm.attrList[i].attributeValueVOS;
								for(var j in attrValueList) { //循环属性值列表
									if(vm.radioForm[key] == attrValueList[j].id) { //找到属性值id与选中属性值相等的一条
										oneAttr.attrValue = attrValueList[j].attrValue;
									}
								};
							}
						};
						productAttrs.push(oneAttr);
					}
				}
				let params = {
					url: vm.apiUrl.supplyDemand.setAttrUrl,
					data: {
						userId: vm.add.userId,
						productAttrs: productAttrs
					}
				};
				if(thisI<4){
					vm.$message.error("请选择所有项目");
				}else{
					vm.ax.post(params, function(data) {
						if(data.succeed) {
							vm.add.choose = false;
							vm.add.newSku.skuName = data.data[0].skuName;
							vm.add.newSku.skuId = data.data[0].skuId;
							
						}
					});
				}
			},
			//改变复选框选中状况
		    handleSelectionChange(val) {
	    		vm.add.selectVal = val;
		    },
			//级联选择器改变 拥有下一级才触发
			handleItemChange(value){
				let index = value.length-1;//0 1 2分别表示省，市，区
				let nowId = value[index];
                let params={
                    url:vm.apiUrl.member.getRegion,
                    data:{
                        parentId: nowId
                    }
                };
				let callback = function (data) {
					if(data.data){
						data.data.map(function (item) {
							vm.$set(item, 'cities', [])
						});	
					}
					vm.i = 0;
					vm.fn(vm.options, value, data.data)
				};
				vm.ax.post(params, callback);
			},
			handleItemClick(val){
				vm.add.addressIds = val;
			},
			// 为级联选择器添加子数据
			fn(list, deptNos, data) {
				vm.i ++;
				if (vm.i < deptNos.length) {
					list.map(function (item) {
						if (item.code == deptNos[vm.i - 1]) {
							vm.fn(item.cities, deptNos, data)
						}
					})
				} else {
					if (data.length==0) {
						list.map(function (item) {
							if (item.code == deptNos[vm.i - 1]) {
								// 使用原生delete方法有延迟，使用vue.delete可以触发视图更新
								vm.$delete(item, 'cities');
							}
						})
					} else {
						list.map(function (item) {
							if (item.code == deptNos[vm.i - 1]) {
								vm.$set(item, 'cities', data)
							}
						})
					}
				}
			},
		    
		    
    		deleteOk(){
    			this.$message({
		          message: '删除成功！',
		          type: 'success'
		        });
    			vm.getTableList();
    		},
    		tableDetailCb(data){
    			let vm = this;
            	if(!data.data){
            		vm.$message.error("数据不存在");
            	}else{
		            vm.showFlag=false;//显示form 隐藏table
		            vm.settlementForm.shipWarehouse=data.data.shipWarehouse;
		            vm.settlementForm.saleMarket=data.data.saleMarket;
		            vm.settlementForm.deliveryTime=data.data.deliveryTime;
		            vm.settlementForm.name=data.data.name;
		            vm.settlementForm.address = data.data.address;
		            vm.detailTable = data.data.voucherGoodsVVO;
		        }
            },
            //back to tablePage
            backTablePage(){
                vm.showFlag=true;
                vm.isAdding = false;
                vm.initialPage();//初始化页面标志
                vm.getTableList();//获取table数据
            },
            //初始化页面
            initialPage(){
                vm.viewFlag=false;//表单只读标识
                vm.showFlag=true;//显示列表，隐藏表单
                vm.breadflag=false;
                vm.title='交收单管理';
            },
            //页码改变
            handleCurrentChange(val){
                vm.pNum=val;
                vm.getTableList();
            },
            //pageSize改变
            handleSizeChange(val){
                vm.pSize=val;
                vm.getTableList();
            },
            //获取列表数据
            getTableList(){
                let params={
                    url:vm.apiUrl.settlement.tableUrl,
                    data:{
                    		userName:vm.name,
                    		startDate:vm.s_start,
                    		endDate:vm.s_end,
                    		auditState: vm.s_auditState,
                        pageNumber:vm.pNum,//页码
                        pageSize:vm.pSize
                    }
                }
                vm.ax.post(params,vm.tableListCb,vm);
                vm.count();
            },
            tableListCb(data){
                vm.pageData=data.data;
                vm.dataList=data.data.rows;
            },
            //获取统计数据
            count(){
                let params={
                    url:vm.apiUrl.settlement.statistic,
                    data:{
	            		userName:vm.name,
	            		startDate:vm.s_start,
	            		endDate:vm.s_end,
	            		auditState: vm.s_auditState
                    }
                }
                let callback = function(data){
                	vm.statisticData = data.data;
                }
                vm.ax.post(params, callback);
            },
            //搜索
            onSearch(){
                vm.getTableList();
            },
            //重置
            onReset(){
                vm.name=null;
                vm.s_start=null;
                vm.s_end=null;
                vm.s_auditState = null;
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
