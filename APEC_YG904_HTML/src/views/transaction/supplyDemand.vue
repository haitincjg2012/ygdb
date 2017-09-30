/** * Created by wq on 2017/09/28. * 供求管理 */
<template>
	<div class="certificate">
		<my-header v-on:initialPage="initialPage" :firstTitle="firsTit" :secondTitle="secondTit" :thirdTitle="thirdTit" :breadFlag="breadflag"></my-header>
		<!--搜索-->
		<div class="mysearch" v-if="showFlag">
			<el-form :inline="true" class="search">
				<el-form-item label="编号：">
					<el-input v-model="s_id" placeholder="请输入编号"></el-input>
				</el-form-item>
				<el-form-item label="标题：">
					<el-input v-model="s_skuName" placeholder="请输入标题"></el-input>
				</el-form-item>
				<el-form-item label="发布人：">
					<el-input v-model="s_showUserName" placeholder="请输入发布人"></el-input>
				</el-form-item>
				<el-button type="primary" @click="onSearch">搜索</el-button>
				<el-button type="primary" @click="goAdd">新增</el-button>
				<el-button @click="onReset">重置</el-button>
			</el-form>
		</div>
		<!--表格列表-->
		<div class="tableList" v-if="showFlag" style="margin-top: 20px;">
			<el-table :data="dataList" border stripe v-loading.body="loadCircle" style="width:100%;">
				<el-table-column prop="productTypeName" label="供求"></el-table-column>
				<el-table-column prop="id" label="编号"></el-table-column>
				<el-table-column prop="skuName" label="标题">
					<template scope="scope">
						<a>{{scope.row.skuName}}</a>
					</template>
				</el-table-column>
				<el-table-column prop="showSecondInfo" label="详细规格">
					<template scope="scope">
						<span v-for="item in scope.row.showSecondInfo">{{item}} </span>
					</template>
				</el-table-column>
				<el-table-column prop="weight" label="数量(斤)"></el-table-column>
				<el-table-column prop="amount" label="单价(元/斤)"></el-table-column>
				<el-table-column prop="address" label="区域"></el-table-column>
				<el-table-column prop="showUserName" label="发布人"></el-table-column>
				<el-table-column prop="userTypeName" label="身份"></el-table-column>
				<el-table-column prop="createDate" label="发布日期">
					<template scope="scope">
						{{scope.row.createDate|timeFilter}}
					</template>
				</el-table-column>
				<el-table-column prop="timeout" label="下架剩余天数"></el-table-column>
				<el-table-column label="操作">
					<template scope="scope">
						<el-button type="text" @click="goDetail(scope.row.id)">详情</el-button>
						<el-button type="text" @click="deleteRow(scope.row.id)">下架</el-button>
					</template>
				</el-table-column>
			</el-table>
			<!--分页-->
			<el-pagination class="pageNation" @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="1" :page-sizes="[10,15,20,25,30]" :page-size="pSize" layout="total,sizes,prev,pager,next,jumper" :total="pageData.totalElements" v-show="pageData.totalElements>10"></el-pagination>
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
				<el-form-item label="数量(斤)：">
					<el-input v-model="supplyForm.weight" :disabled="true"></el-input>
				</el-form-item>
				<el-form-item label="单价(元/斤)：">
					<el-input v-model="supplyForm.amount" :disabled="true"></el-input>
				</el-form-item>
				<el-form-item label="区域：">
					<el-input v-model="supplyForm.address" :disabled="true"></el-input>
				</el-form-item>
				<el-form-item label="发布日期：">
					<el-input v-model="supplyForm.createDate" :disabled="true"></el-input>
				</el-form-item>
				<el-form-item label="下架剩余天数：">
					<el-input v-model="supplyForm.timeout" :disabled="true"></el-input>
				</el-form-item>
				<el-form-item label="描述：">
					<el-input v-model="supplyForm.remark" :disabled="true"></el-input>
				</el-form-item>
				<el-form-item label="图片：">
					<img v-for="item in supplyForm.productImages" :src="item.imageUrl" class="org-img" />
				</el-form-item>
				<el-form-item class="btnGroup">
					<el-button @click="backTablePage">返回</el-button>
				</el-form-item>
			</el-form>
		</div>
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
					<el-select v-model="addForm.userId" placeholder="请选择代发客户">
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
					<el-upload :on-success="uploadPic"
						:on-remove = "deletPic"
					  action="/common/uploadImg.apec"
					  list-type="picture-card">
					  <i class="el-icon-plus"></i>
					</el-upload>
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
								<el-radio-button v-for="value in attr.attributeValueVOS" :label="value.id">{{value.attrValue}}</el-radio-button>
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
				s_id: null,
				s_showUserName: null,
				s_skuName: null,
				loadCircle: false, //加载显示
				dataList: [],
				detailTable: [], //详细表格数据
				pageData: [], //分页数据
				pSize: 10, //页容量
				pNum: 1, //页码
				clearRegion: false,
				supplyForm: {}, //详情
				cityData: [], //市
				areaData: [], //区
				townData: [], //镇
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
					productImages: '',
					imageUrl: '',
					sort: '',
					cityId: '',
					areaId: '',
					townId: '',
					remark: '',
					productImages: [],
				},
				attrDialogVisible: false, //选择规格弹框显示
				userList: [], //所有客户列表，供选择用
				attrList: [], //属性列表
				radioForm: {}, //选中属性绑定
				isSale:false,//是否为供应类型
				isBuy:false,
			}
		},
		activated() {
			var vm = this;
			vm.getTableList();
			vm.initUser();
			vm.initAttr();
			vm.getCityList();
		},
		deactivated() {
			var vm = this;
			vm.loadCircle = false; //避免超时闪现加载图标
		},

		methods: {
			//查看详情
			goDetail(id) {
				var vm = this;
				vm.viewFlag = true;
				vm.thirdTit = '详情';
				vm.breadflag = true;
				let params = {
					url: vm.apiUrl.supplyDemand.tableDetailUrl,
					data: {
						elasticId: id
					}
				}
				vm.ax.post(params, vm.tableDetailCb);
			},
			tableDetailCb(data) {
				var vm = this;
				vm.showFlag = false; //显示form 隐藏table
				vm.supplyForm = data.data;
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
				})
			},
			deleteOk() {
				var vm = this;
				this.$message({
					message: '删除成功！',
					type: 'success'
				});
				vm.getTableList;
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
				this.addForm.productImages.push({"imageUrl":res.data[0][0].imagePath});
				file.url = res.data[0][0].imagePath;//赋值，移除时可判断
 			},
			deletPic(file, fileList){//移除图片后
				console.log(file.url);
				console.log(this.addForm.productImages);
				for(let i in this.addForm.productImages){
					if(file.url==this.addForm.productImages[i].imageUrl){
						this.addForm.productImages.splice(i,1);
						console.log(i);
						console.log(this.addForm.productImages);
					}
				}
			},
			//新增提交
			submitAdd() {
				var vm = this;
				var city = vm.commonJs.getArrayVal(vm.cityData, vm.addForm.cityId, 'code', 'name');
				var area = vm.commonJs.getArrayVal(vm.areaData, vm.addForm.areaId, 'code', 'name');
				var town = vm.commonJs.getArrayVal(vm.townData, vm.addForm.townId, 'code', 'name');
				vm.addForm.address = city + area + town;
				vm.addForm.firstImageUrl = vm.addForm.productImages[0].imageUrl;
				let params = {
					url: vm.apiUrl.supplyDemand.addUrl,
					data: vm.addForm
				};
				vm.ax.post(params, function(data) {
					if(data.succeed) {
						vm.attrDialogVisible = false;
						vm.addForm.skuName = data.data[0].skuName;
						vm.addForm.skuId = data.data[0].skuId;
						for(var i in vm.addForm){//清空表单
		            vm.addForm[i]="";
		            vm.addForm.weightUnit='万斤';
		            vm.addForm.priceUnit='元/斤';
		        }
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
						id: vm.s_id,
						showUserName: vm.s_showUserName,
						skuName: vm.s_skuName,
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
				vm.getTownList(id); //镇
			},
			//获取市信息（暂不要）
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
				console.log("市长度：" + data.data.length);
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
				vm.s_id = null;
				vm.s_showUserName = null;
				vm.s_skuName = null;
				vm.getTableList();
			}
		},
		components: {
			'my-header': header
		}
	}
</script>
<style scoped="scoped">
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
</style>