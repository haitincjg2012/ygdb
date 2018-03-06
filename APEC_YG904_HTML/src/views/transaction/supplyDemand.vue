/**
* Created by wq on 2017/09/28.
* 供求管理
*/
<template>
	<div class="certificate">
		<my-header v-on:initialPage="initialPage" :firstTitle="firsTit" :secondTitle="secondTit" :thirdTitle="thirdTit" :breadFlag="breadflag"></my-header>
		<!--搜索-->
		<div class="mysearch" v-if="showFlag">
			<el-form :inline="true" class="search" label-width="100px">
				<el-form-item label="标题：">
					<el-input v-model="s_skuName" placeholder="请输入标题"></el-input>
				</el-form-item>
				<el-form-item label="地址：">
					<el-input v-model="s_address" placeholder="请输入地址"></el-input>
				</el-form-item>
				<el-form-item label="供求：">
					<el-select v-model="s_orderType" placeholder="请选择供求类型" @change="getTableList">
				      <el-option label="供应" value="SALE"></el-option>
				      <el-option label="求购" value="BUY"></el-option>
				    </el-select>
				</el-form-item>
				<el-form-item label="发布人：">
					<el-input v-model="s_userName" placeholder="请输入发布人"></el-input>
				</el-form-item>
				<el-form-item label="开始时间：">
                    <el-date-picker type="date" placeholder="开始时间" format="yyyy-MM-dd" v-model="s_startDate"></el-date-picker>
				</el-form-item>
				<el-form-item label="结束时间：">
                    <el-date-picker type="date" placeholder="结束时间" format="yyyy-MM-dd" v-model="s_endDate"></el-date-picker>
				</el-form-item>
				<el-form-item label="状态：">
					<el-select v-model="s_underCarriage" placeholder="请选择供求类型" @change="getTableList">
				      <el-option label="上架中" value="false"></el-option>
				      <el-option label="已下架" value="true"></el-option>
				    </el-select>
				</el-form-item>
				<el-button type="primary" @click="onSearch">搜索</el-button>
				<el-button type="primary" @click="goAdd">发布</el-button>
				<form id="form" method="post" style="display: none;" action="" enctype="multipart/form-data"></form>
                <el-button type="primary" @click="goExport">导出</el-button>
				<el-button @click="onReset">重置</el-button>
			</el-form>
		</div>
		<!--表格列表-->
		<div class="tableList" v-if="showFlag" style="margin-top: 20px;">
			<el-table :data="dataList" border stripe v-loading.body="loadCircle" header-row-class-name="headerRow"  style="width:100%;">
				<el-table-column prop="productTypeName" label="供求"></el-table-column>
				<el-table-column prop="id" label="编号"></el-table-column>
				<!-- <el-table-column prop="skuName" label="标题"> -->
				<el-table-column label="标题">
					<template slot-scope="scope">
						<a>{{scope.row.skuName}}</a>
					</template>
				</el-table-column>
				<!--<el-table-column prop="showSecondInfo" label="详细规格">
					<template slot-scope="scope">
						<span v-for="item in scope.row.showSecondInfo">{{item}} </span>
					</template>
				</el-table-column>-->
				<el-table-column label="供应量">
					<template slot-scope="scope">
							<span>{{scope.row.weight}} {{scope.row.weightUnit}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="amount" label="单价(元/斤)">
					<template slot-scope="scope">
					  <span v-if="scope.row.productTypeName =='供应'">{{scope.row.amount}}</span>
					  <span v-if="scope.row.productTypeName =='求购'">{{scope.row.startAmount}}~{{scope.row.endAmount}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="address" label="区域"></el-table-column>
				<el-table-column prop="userTypeName" label="身份"></el-table-column>
				<el-table-column prop="createDate" label="发布日期">
					<template slot-scope="scope">
						{{scope.row.createDate|timeFilter}}
					</template>
				</el-table-column>
                <el-table-column prop="productTags" label="标签" width="170">
	                	<template slot-scope="scope">
	                		  <button v-for="item in scope.row.productTags" class="tag">{{item.tagName}}</button>
	                	</template>
                </el-table-column>
				<el-table-column prop="timeout" label="下架剩余天数"></el-table-column>
				<el-table-column label="状态">
					<template slot-scope="scope">
							{{scope.row.timeout>0?"已上架":"已下架"}}
					</template>
				</el-table-column>
				<el-table-column prop="mobile" label="手机号"></el-table-column>
				<el-table-column prop="showUserName" label="发布人">
					<template slot-scope="scope">
            			<a style="text-decoration: underline; cursor: pointer;color: #20A0FF;" @click="goto(scope.row, 'supplyDemand')">{{scope.row.showUserName}}</a>
					</template>
				</el-table-column>
				<el-table-column label="操作" fixed="right" width="250">
					<template slot-scope="scope">
						<el-button type="text" @click="goDetail(scope.row)">详情</el-button>
						<el-button type="text" @click="openTag(scope.row)">设置标签</el-button>
						<el-button type="text" v-if="scope.row.timeout>0" @click="deleteRow(scope.row.elasticId)">下架</el-button>
						<el-button type="text" v-if="scope.row.orderWeight <= 0" @click="stickTop(scope.row)">置顶</el-button>
						<el-button type="text" v-else @click="cancelStickTop(scope.row)">取消置顶</el-button>
					</template>
				</el-table-column>
			</el-table>
			<!--分页-->
			 <el-pagination class="pageNation" @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pNum"
                               :page-sizes="[10,15,20,25,30]" :page-size="pSize" layout="total,sizes,prev,pager,next,jumper"
                               :total="pageData.totalElements" v-show="pageData.totalElements>10"></el-pagination>
		</div>

		<!--详情form Page-->
		<div id="formPage" v-if="viewFlag">
			<el-form ref="supplyForm" class="myformDetail long-form" :class="{formBorder:viewFlag}">
				<el-form-item label="编号：">
					<el-input v-model="supplyForm.id" :disabled="viewFlag"></el-input>
				</el-form-item>
				<el-form-item label="供求：">
					<el-input v-model="supplyForm.productTypeName" :disabled="viewFlag"></el-input>
				</el-form-item>
				<el-form-item label="发布人：">
					<el-input v-model="supplyForm.showUserName" :disabled="true"></el-input>
				</el-form-item>
				<el-form-item label="身份：">
					<el-input v-model="supplyForm.userTypeName" :disabled="true"></el-input>
				</el-form-item>
				<el-form-item label="标题：">
					<el-input v-model="supplyForm.skuName" :disabled="true"></el-input>
				</el-form-item>
				<el-form-item label="详细规格：">
					<span v-for="item in supplyForm.productAttrs" class="attr-span">{{item.attrName+':'+item.attrValue}}</span>
				</el-form-item>
				<el-form-item label="供应量：">
					<!-- <el-input v-model="supplyForm.weight" :disabled="true"></el-input> -->
					<span class="detailP">{{supplyForm.weight}} {{supplyForm.weightUnit}}</span>
				</el-form-item>
				<el-form-item label="单价(元/斤)：">
					<el-input v-model="supplyForm.amount" :disabled="true" v-if="supplyForm.productTypeName =='供应'"></el-input>
					<p class="detailP" v-if="supplyForm.productTypeName =='求购'">{{supplyForm.startAmount}}~{{supplyForm.endAmount}}</p>
				</el-form-item>
				<el-form-item label="区域：">
					<el-input v-model="supplyForm.address" :disabled="true"></el-input>
				</el-form-item>
				<el-form-item label="发布日期：">
					<!-- <el-input v-model="supplyForm.createDate" :disabled="true"></el-input> -->
					<p class="detailP" >{{supplyForm.createDate|timeFilter}}</p>
				</el-form-item>
				<el-form-item label="下架剩余天数：">
					<el-input v-model="supplyForm.timeout" :disabled="true"></el-input>
				</el-form-item>
				<el-form-item label="描述：">
					<el-input v-model="supplyForm.remark" :disabled="true"></el-input>
				</el-form-item>
				<el-form-item label="图片：">
					<img v-for="item in supplyForm.productImages" style="margin: 5px;" :src="item.imageUrl+'?x-oss-process=style/_list'" />
				</el-form-item>
				<el-form-item class="btnGroup">
					<el-button @click="backTablePage">返回</el-button>
				</el-form-item>
			</el-form>
		</div>
		
            <el-dialog title="选择标签" :visible.sync="tagDialogVisible" class="template-dialog">
            		<div>
            			<button v-for="(item,index) in tagList" :class="[item.chosen ? 'tag' : 'tag-gry']" @click="toggleTag(index)">{{item.tagName}}</button>
            		</div>
				  <div slot="footer" class="dialog-footer">
				  		<el-button type="primary" @click="setTag()">确 定</el-button>
				    	<el-button @click="initialPage()">取 消</el-button>
				  </div>
			</el-dialog>
		<!--新增页面-->
		<div v-if="addFlag">
			<el-form ref="addForm" class="myformDetail" :model="addForm">
				<el-form-item label="供求类型：" prop="productType">
					<el-select v-model="addForm.productType" placeholder="请选择供求类型" @change="toggleProductType">
						<el-option value="SALE" label="供应"></el-option>
						<el-option value="BUY" label="求购"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="客户：" prop="userId">
					<el-select v-model="addForm.userId" placeholder="请选择代发客户" filterable>
						<el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="规格：" prop="skuName">
					<span class="title" v-if="addForm.skuName">{{addForm.skuName}}</span>
					<el-button type="warning" @click="showAttrDialog()">选择规格</el-button>
				</el-form-item>
				<el-form-item label="供应量：" :inline="true" v-if="isSale" prop="weight">
					<el-input v-model="addForm.weight" style="width: 270px;float: left;" placeholder="请输入供应量"></el-input>
					<el-select v-model="addForm.weightUnit" style="width: 100px;float: right;">
						<el-option value="万斤"></el-option>
						<el-option value="桶"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="求购量：" :inline="true" v-if="isBuy" prop="weight">
					<el-input v-model="addForm.weight" style="width: 270px;float: left;" placeholder="请输入求购量"></el-input>
					<el-select v-model="addForm.weightUnit" style="width: 100px;float: right;">
						<el-option value="万斤"></el-option>
						<el-option value="桶"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="供应单价：" v-if="isSale" prop="amount">
					<el-input v-model="addForm.amount" style="width: 270px;float: left;" placeholder="请输入供应单价"></el-input>
					<el-select v-model="addForm.priceUnit" style="width: 100px;float: right;">
						<el-option value="元/斤"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="求购单价：" v-if="isBuy" prop="startAmount">
					<el-input v-model="addForm.startAmount" style="width: 120px;float: left;" placeholder="请输入最低单价"></el-input>
					<span style="margin:0 7px;float: left;">一</span>
					<el-input v-model="addForm.endAmount" style="width: 120px;float: left;" placeholder="请输入最高单价"></el-input>
					<el-select v-model="addForm.priceUnit" style="width: 100px;float: right;">
						<el-option value="元/斤"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="货源区域：" :inline="true" prop="cityId">
					<!--修改市-->
					<el-select v-model="addForm.cityId" class="selRegion" @change="changeCity(addForm.cityId)" placeholder="请选择市">
						<el-option :label="item.name" :value="item.code" v-for="item in cityData" :key="item.id"></el-option>
					</el-select>
					<!--修改区-->
					<el-select v-model="addForm.areaId" class="selRegion" @change="changeArea(addForm.areaId)" placeholder="请选择区">
						<el-option :label="item.name" :value="item.code" v-for="item in areaData" :key="item.id"></el-option>
					</el-select>
					<!--修改街道-->
					<el-select v-model="addForm.townId" class="selRegion" placeholder="请选择街道">
						<el-option :label="item.name" :value="item.code" v-for="item in townData" :key="item.id"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="上架有效期：" prop="timeout">
					<el-select v-model="addForm.timeout" placeholder="请选择上架有效期">
						<el-option value="7" label="七天"></el-option>
						<el-option value="15" label="十五天"></el-option>
						<el-option value="30" label="三十天"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="描述：" prop="remark">
					<el-input style="width:390px;" :autosize="{minRows:5}" type="textarea" v-model="addForm.remark" placeholder="请输入描述"></el-input>
				</el-form-item>
				<el-form-item label="图片：" v-if="isSale">
					<!--:on-success="uploadPic"-->
					<el-upload
						:header="{'token':token}"
						action="noUse"
						:on-remove = "deletPic"
						:before-upload="beforeLoad"
						:http-request="submitImg"
						list-type="picture-card">
					  <i class="el-icon-plus"></i>
					</el-upload>

					<!--wq-->
					<!--<el-upload :on-success="uploadPic"
						:on-remove = "deletPic"
					  action="/common/uploadImg.apec"
					  list-type="picture-card">
					  <i class="el-icon-plus"></i>
					</el-upload>-->
				</el-form-item>
				<el-form-item class="btnGroup">
					<el-button type="primary" @click="submitAdd()">提交</el-button>
					<el-button @click="backTablePage">返回</el-button>
				</el-form-item>
			</el-form>

			<el-dialog title="选择规格" :visible.sync="attrDialogVisible">
				<ul>
					<li v-for="attr in attrList">
						<div class="attr-title">{{attr.goodsAttrName}}<span v-if="attr.mustRequired">(必选项)</span></div>
						<div>
							<el-radio-group v-model="radioForm[attr.attrId]">
								<el-radio-button v-for="value in attr.attributeValueVOS" :key="value.id" :label="value.id">{{value.attrValue}}</el-radio-button>
							</el-radio-group>
						</div>
					</li>
				</ul>
				<div slot="footer" class="dialog-footer">
					<el-button type="primary" @click="setAttr()">保 存</el-button>
					<el-button @click="attrDialogVisible=false">取 消</el-button>
				</div>
			</el-dialog>
		</div>
	</div>
</template>

<script>
	import header from '~/components/header/header.vue'
	import encrypt from '~/assets/js/oss/encrypt'

	export default {
		data() {
			return {
				//breadcrumb
				firsTit: '交易',
				secondTit: '供求管理',
				thirdTit: '',
				breadflag: false,
				/*header参数*/
				viewFlag: false, //详情
				addFlag: false, //表单新增标识
				showFlag: true, //true 显示table页; false 显示form页
				productType: null,
				s_skuName: null,
				s_address: null,
				s_underCarriage: null,
				s_userName: null,
				s_startDate: null,
				s_endDate: null,
				s_orderType:null,
				s_phone:null,
				loadCircle: false, //加载显示
				dataList: [],
				detailTable: [], //详细表格数据
				pageData: [], //分页数据
				pSize: 10, //页容量
				pNum: 1, //页码
				clearRegion: true,
				supplyForm: {}, //详情
				cityData: [], //市
				areaData: [], //区
				townData: [], //镇
				//header token
				token:this.$store.state.authToken || this.commonjs.getValue("authToken"),
				noUse:"",//因为ele-upload,action必传参数
				imgfile:null,//文件系统
				recordUpLoad:[],
				//新增
				addForm: {
					userId: '',
					productType: '',
					skuId: '',
					skuName: '',
					weight: '',
					amount: '',
					startAmount: '',
					endAmount: '',
					firstImageUrl: '',
					timeout: '',
					address: '',
					weightUnit: '万斤',
					priceUnit: '元/斤',
//					productImages: '',
					imageUrl: '',
					sort: '',
					cityId: '',
					areaId: '',
					townId: '',
					remark: '',
					productImages: []
				},
				attrDialogVisible: false, //选择规格弹框显示
				userList: [], //所有客户列表，供选择用
				attrList: [], //属性列表
				radioForm: {}, //选中属性绑定
				isSale:false,//是否为供应类型
				isBuy:false,
				//设置标签
                tagDialogVisible:false,//选择标签显示
                tagList:[
                	{id:null,tagName:'易果推荐',chosen:false,className:'YGTJ'}
                ],
                setTagId:'',
			}
		},
		activated() {
			var vm = this;
			encrypt.getAuthorise();//请求授权aly上传图片信息
			vm.getTableList();
			vm.initUser();
			vm.initAttr();
			vm.getCityList();
			setTimeout(function(){
				vm.checkFromWhistleBlowing(vm,'goDetail','elasticId');//检查是否从交收单过来
			},1)
		},
		deactivated() {
			var vm = this;
			vm.loadCircle = false; //避免超时闪现加载图标
		},

		methods: {
			//上传图片aly
			submitImg(data){
//				console.log(arguments);
				var vm = this;
//				console.log(data);
				//gsy
				return encrypt.ossUpload(data.file,function(result){
//					console.log("img-result",result);
					vm.$message.success('上传图片成功!');
					var imgPath=vm.apiUrl.common.alyserverUrl+result.name;
					vm.addForm.productImages.push({"imageUrl":imgPath});
                    vm.recordUpLoad.push(data.file.name);//将图片名存在recordUpLoad
					return true;
				},function(err){
					console.log("upload image failed by oss,"+ err);
					vm.deletPic();//删除上传失败的图片
					vm.$message.error('上传图片失败!');
					return false;
				});
			},

            //弹出标签选择框
            openTag(row){
            		var vm = this;
            		vm.tagList = [
	                	{tagName:'易果推荐',chosen:false,className:'YGTJ'}
	                ];
            		for(let i in row.productTags){
            			for(let j in vm.tagList){
            				if(row.productTags[i].tagName==vm.tagList[j].tagName){
            					vm.tagList[j].chosen = true;
            				}
                  /*  else{
                      vm.tagList[j].chosen = false;
            					vm.tagList[j].id = null;
                    }*/
            			}
            		}
            		vm.tagDialogVisible = true;
            		vm.setTagId = row.elasticId;

            },
            toggleTag(index){
            		var vm = this;
            		vm.tagList[index].chosen = vm.tagList[index].chosen?false:true;
            },
            stickTop(row){
				let vm = this;
				let params = {
					url: vm.apiUrl.supplyDemand.stickTop,
					data: {
						id: row.id
					}
				}
				let callback = function(){
					vm.$message.success("置顶成功")
					vm.getTableList()
				}
				vm.ax.post(params, callback);
            },
            cancelStickTop(row){
				let vm = this;
				let params = {
					url: vm.apiUrl.supplyDemand.cancelStickTop,
					data: {
						id: row.id
					}
				}
				let callback = function(){
					vm.$message.success("取消置顶成功")
					vm.getTableList()
				}
				vm.ax.post(params, callback);
            },
            setTag(){
        		var vm = this;
        		var productTags = [];
        		vm.tagList.forEach(function(e){
        			if(e.chosen){
        				productTags.push({tagName:e.tagName,className:e.className});
        			}
        		});
        		let params={
                    url:vm.apiUrl.supplyDemand.setTag,
                    data:{
                        elasticId:vm.setTagId,
                        productTags:productTags
                    }
                }
                vm.ax.post(params,vm.setTagOk);
            },
            setTagOk(){
            		this.initialPage();
            		this.getTableList();
            },
			//校验图片格式
			beforeLoad(){
				var vm=this;
				if(!vm.validateImg) {//图片格式校验通过
					return false;
				}
			},
			//图片格式校验
			validateImg(){
				var vm=this;
				var type=file.type;
				var imgPattern=/image\/(png|jpeg)/g;
				if(!type){
					vm.$message.warning('您上传的图片格式不对，请您重新上传图片!');
					return false;
				}
				if(!imgPattern.test(type)){
					vm.$message.warning('上传图片只能是JPG/PNG格式！');
					return false;
				}
				return true;
			},
			//查看详情
			goDetail(row) {
				let vm = this;
				vm.viewFlag = true;
				vm.thirdTit = '详情';
				vm.breadflag = true;
				let params = {
					url: vm.apiUrl.supplyDemand.tableDetailUrl,
					data: {
						elasticId: row.elasticId
					}
				}
				vm.ax.post(params, vm.tableDetailCb);
			},
			tableDetailCb(data) {
				let vm = this;
            	if(!data.data){
            		vm.$message.error("数据不存在");
            	}else{
					vm.showFlag = false; //显示form 隐藏table
					vm.supplyForm = data.data;
				}
			},
			goAdd() {
				var vm = this;
				vm.addFlag = true;
				vm.showFlag = false;
			},
			deleteRow(id) {
				var vm = this;
				this.$confirm('此操作将永久下架该供求, 是否继续?', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					let params = {
						url: vm.apiUrl.supplyDemand.offSellUrl,
						data: {
							elasticId: id,
						}
					};
					vm.ax.post(params, vm.deleteOk);
				}).catch(() => {});
			},
			deleteOk() {
				var vm = this;
				vm.$message({
					message: '下架成功！',
					type: 'success'
				});
				vm.getTableList();
			},
			showAttrDialog(){//弹出选择规格
				var vm = this;
				if(vm.addForm.userId){
					vm.attrDialogVisible = true;
				}else{
					vm.$message.error("请先选择代发客户!");
				}
			},
			toggleProductType(type){//切换供求类型
				var vm = this;
				if(type=='SALE'){
					vm.isSale = true;
					vm.isBuy = false;
				}else{
					vm.isBuy = true;
					vm.isSale = false;
				}
			},
			setAttr() {
				var vm = this;
				var productAttrs = [];
				var oneAttr = {};
				for(var key in vm.radioForm) { //循环选中属性绑定
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
						userId: vm.addForm.userId,
						productAttrs: productAttrs
					}
				};
				vm.ax.post(params, function(data) {
					if(data.succeed) {
						vm.attrDialogVisible = false;
						vm.addForm.skuName = data.data[0].skuName;
						vm.addForm.skuId = data.data[0].skuId;
					}
				});
			},
			uploadPic(res,file){//图片上传成功后
				console.log(file, 8888888888);
				console.log(res);
				this.addForm.productImages.push({"imageUrl":res.data[0][0].imagePath});
				file.url = res.data[0][0].imagePath;//赋值，移除时可判断
 			},
			deletPic(file, fileList){//移除图片后
				var vm=this;
				var name = file.name;
				var i = vm.recordUpLoad.indexOf(name);
				vm.addForm.productImages.splice(i,1);//删除表单中图片地址
//				console.log("vm.addForm.productImages:",vm.addForm.productImages);
				//	console.dir(new Array);
	            //	console.log( i);

				/*	wq
				var vm=this;
				for(let i in vm.addForm.productImages){
					if(file.url==vm.addForm.productImages[i].imageUrl){
						vm.addForm.productImages.splice(i,1);

					}
				}*/

			},
			//新增提交
			submitAdd() {
				var vm = this;
				var city = vm.commonJs.getArrayVal(vm.cityData, vm.addForm.cityId, 'code', 'name');
				var area = vm.commonJs.getArrayVal(vm.areaData, vm.addForm.areaId, 'code', 'name');
				var town = vm.commonJs.getArrayVal(vm.townData, vm.addForm.townId, 'code', 'name');
				vm.addForm.address = city + area + town;
				if(vm.addForm.productImages.length>0){
					vm.addForm.firstImageUrl = vm.addForm.productImages[0].imageUrl;
				}
				let params = {
					url: vm.apiUrl.supplyDemand.addUrl,
					data: vm.addForm
				};
				vm.ax.post(params, function(data) {
					if(data.succeed) {
						vm.$message({
		          message: '发布成功！',
		          type: 'success'
		        });
						vm.backTablePage();
						for(var i in vm.addForm){//清空表单
							if(i!="weightUnit" || i!="priceUnit" || i!="productImages"){
								vm.addForm[i]="";
							}
						}
						vm.addForm.weightUnit='万斤';
						vm.addForm.priceUnit='元/斤';
						vm.addForm.productImages=[];
					}
				});
			},
			//back to tablePage
			backTablePage() {
				var vm = this;
				vm.showFlag = true;
				vm.initialPage(); //初始化页面标志
				vm.getTableList(); //获取table数据
			},
			//初始化页面
			initialPage() {
				var vm = this;
				vm.viewFlag = false; //表单只读标识
				vm.addFlag = false; //表单只读标识
				vm.showFlag = true; //显示列表，隐藏表单
				vm.breadflag = false;
				vm.tagDialogVisible = false;
				vm.title = '供求管理';
			},
			//页码改变
			handleCurrentChange(val) {
				var vm = this;
				vm.pNum = val;
				vm.getTableList();
			},
			//pageSize改变
			handleSizeChange(val) {
				var vm = this;
				vm.pSize = val;
				vm.getTableList();
			},
			//获取列表数据
			getTableList() {
				var vm = this;
				let params = {
					url: vm.apiUrl.supplyDemand.tableUrl,
					data: {
						skuName: vm.s_skuName,
						address: vm.s_address,
						userName: vm.s_userName,
						startDate: vm.s_startDate,
						endDate: vm.s_endDate,
						underCarriage: vm.s_underCarriage,
						productType: vm.s_orderType,
						pageNumber: vm.pNum, //页码
						pageSize: vm.pSize
					}
				}
				vm.ax.post(params, vm.tableListCb, vm);
			},
			tableListCb(data) {
				var vm = this;
				vm.pageData = data.data;
				vm.dataList = data.data.rows;
			},
            //导出
            goExport(){
            	let vm = this;
                var data = {
						skuName: vm.s_skuName,
						address: vm.s_address,
						userName: vm.s_userName,
						startDate: vm.moment(vm.s_startDate).format('YYYY-MM-DD'),
						endDate: vm.moment(vm.s_endDate).format('YYYY-MM-DD'),
						underCarriage: vm.s_underCarriage,
						productType: vm.s_orderType
                };
                var form = document.getElementById("form");
                var innerHtml = '';
                for(var i in data){
                	innerHtml += '<input name="' + i + '" class="forminput' + i + '"/>';
                }
                form.innerHTML = innerHtml;
                for(var i in data){
                	document.querySelector(".forminput"+i).value = data[i];
                }
                form.setAttribute("action", vm.apiUrl.supplyDemand.export);
                form.submit();
            },
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
			initAttr() {
				var vm = this;
				let params = {
					url: vm.apiUrl.supplyDemand.getAttrUrl,
					data: {}
				}
				vm.ax.post(params, function(data) {
					vm.attrList = data.data.goodsAttrVOList;
					vm.attrList.forEach(function(e) {
						vm.$set(vm.radioForm, e.attrId, "");
					});
				});
			},
			changeCity(id, type) {
				var vm = this;
				if(vm.clearRegion) {
					vm.addForm.areaId = "";
					vm.addForm.townId = "";
				}
				vm.getAreaList(id); //区
			},
			//区改变（暂不要）
			changeArea(id) {
				var vm = this;
				if(vm.clearRegion) {
					vm.addForm.townId = "";
				}
				if(!id){
					vm.townData = "";//如果区没选，将镇置空
					return
				}else{
					vm.getTownList(id); //镇
				}
			},
			//获取市信息
			getCityList() {
				var vm = this;
				let params = {
					url: vm.apiUrl.member.regionUrl,
					data: {
						/*parentId:"",
						name:""*/
					}
				}
				vm.ax.post(params, vm.cityListCb);
			},
			cityListCb(data) {
				var vm = this;
				vm.cityData = data.data;
//				console.log("市长度：" + data.data.length);
			},
			//获取区信息（暂不要）
			getAreaList(cityid) {
				var vm = this;
				let params = {
					url: vm.apiUrl.member.regionUrl,
					data: {
						parentId: cityid
					}
				}
				vm.ax.post(params, vm.areaListCb);
			},
			areaListCb(data) {
				var vm = this;
				vm.areaData = data.data;
				console.log("区名：" + vm.areaData[0].name)

			},
			//获取镇信息（暂不要）
			getTownList(areaid) {
				var vm = this;
				let params = {
					url: vm.apiUrl.member.regionUrl,
					data: {
						parentId: areaid
					}
				}
				vm.ax.post(params, vm.townListCb);
			},
			townListCb(data) {
				var vm = this;
				vm.townData = data.data;
			},
			//搜索
			onSearch() {
				var vm = this;
				vm.getTableList();
			},
			//重置
			onReset() {
				var vm = this;
				vm.s_skuName = null;
				vm.s_address = null;
				vm.s_userName = null;
				vm.s_startDate = null;
				vm.s_endDate = null;
				vm.s_underCarriage = null;
				vm.s_orderType = null;
				vm.getTableList();
			}
		},
		components: {
			'my-header': header
		}
	}
</script>
<style scoped="scoped">
    .tag {
    	border: 1px solid rgba(32,160,255,.2);
    	padding: 3px 5px;
    	margin-bottom: 5px;
    	border-radius: 4px;
    	background-color: rgba(32,160,255,.1);
    	color: #20a0ff;
    	margin-right: 5px;
    }
    .tag-gry {
    	border: 1px solid transparent;
    	padding: 3px 5px;
    	border-radius: 4px;
    	margin-bottom: 5px;
    	background-color: #D9D9D9;
    	color: #888;
    	margin-right: 5px;
    }
	.attr-span {
		color: #000;
		margin-left: 9px;
		/*white-space:nowrap;*/
	}

	.org-img {
		max-width: 300px;
		max-height: 300px;
		margin-right: 10px;
		vertical-align: top;
	}

	.attr-title {
		line-height: 30px;
		margin: 10px 0;
		font-size: 16px;
		color: #000000;
	}

	.title {
		color: #000000;
		margin-right: 20px;
	}
</style>
<style>
	.unit-span {
		position: absolute;
		left: 0;
	}

	.long-form .el-form-item .el-form-item__content {
		position: relative;
		width: calc(100% - 130px) !important;
	}
	.long-form .detailP{
		padding:0 15px;
		color:#000;
	}
</style>
