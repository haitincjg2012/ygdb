/**
* Created by lrx on 2017/12/13.
* 交收单管理
*/
<template>
    <div class="certificate">
        <my-header v-on:initialPage="initialPage" :firstTitle="firsTit" :secondTitle="secondTit" :thirdTitle="thirdTit" :breadFlag="breadflag"></my-header>
        <!--搜索-->
        <div class="mysearch" v-if="showFlag">
            <el-form :inline="true" class="search clearfix">
                <el-form-item label="用户昵称：">
                    <el-input v-model="name" placeholder="请输入用户昵称"></el-input>
                </el-form-item>
                <el-button type="primary" @click="retrieve(1)">搜索</el-button>
                <el-button @click="toClear">重置</el-button>
                <el-button type="primary" @click="introduction">等级升级说明</el-button>
            </el-form>
        </div>
        <!--表格列表-->
        <div class="tableList" v-if="showFlag" style="margin-top: 20px;">
            <el-table :data="dataList" border stripe v-loading.body="loadCircle" header-row-class-name="headerRow"  >
            	<el-table-column prop="id" label="客户编号"></el-table-column>
            	<el-table-column prop="name" label="客户昵称"></el-table-column>
            	<el-table-column prop="mobile" label="电话"></el-table-column>
            	<el-table-column prop="userType" label="身份">
            		<template slot-scope="scope">
            			{{filters.userType(scope.row.userType)}}
            		</template>
            	</el-table-column>
            	<el-table-column prop="availablePoints" label="积分"></el-table-column>
            	<el-table-column prop="userLevelKey" label="等级"></el-table-column>
                <el-table-column label="操作" fixed="right" width="180">
                    <template slot-scope="scope">
                		<el-button type="text" @click="goDetail(scope.row)">查看流水</el-button>
                		<el-button type="text" @click="goEdit(scope.row)">修改积分</el-button>
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
        	<el-row v-if="isDetail">
        		<h1 style="margin: 10px; font-size: 16px;">客户名：{{detail.row.name}} &nbsp;&nbsp;&nbsp;&nbsp;编号：{{detail.row.id}}</h1>
        		<el-table :data="detail.list" border stripe v-loading.body="loadCircle" header-row-class-name="headerRow"  >
	            	<el-table-column prop="pointsChanged" label="积分值" width="200">
	            		<template slot-scope="scope">
	            			{{scope.row.pointsChanged>0?'+'+scope.row.pointsChanged:scope.row.pointsChanged}}
	            		</template>
	            	</el-table-column>
	            	<el-table-column prop="createDate" label="日期" width="150">
	            		<template slot-scope="scope">
	            			{{filters.formatDate(scope.row.createDate)}}
	            		</template>
	            	</el-table-column>
	            	<el-table-column prop="remark" label="备注"></el-table-column>
	            </el-table>
	            <!--分页-->
				<div class="pagination-wrapper" style="text-align: right;">
					<el-pagination layout="jumper, sizes, prev, pager, next, total" :page-count="pageCount2" :currentPage="currentNo2" :page-sizes="[10, 20, 30, 40]" :page-size="pageSize2" :total="total2" @size-change="sizeChange2" @current-change="getDetail"></el-pagination>
				</div>
			  	<el-row class="text-center">
	                <el-button @click="backTablePage">返回</el-button>
			  	</el-row>
        	</el-row>
        </div>
        
        <el-dialog class="dialogTable" width="450px" title="修改积分" :visible.sync="isEditing">
            <el-form :inline="true" label-width="100px">
                <el-form-item label="选择理由：">
                    <el-select v-model="edit.pointRuleType" style="width: 250px;" @change="reasonChange" clearable>
                    	<el-option v-for="(item, index) in editIntegral"
                    		:key="index"
                    		:value="item.pointRuleType"
                    		:label="item.pointRuleName"
                    	></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="积分值：">
                    <el-input v-model="edit.pointsChanged" @input="numberOnly" style="width: 250px;" :disabled="edit.disabled" />
                </el-form-item>
				<el-row style="color: #eb9e05; padding-left: 100px; margin: -20px 0 10px 0;">说明：正数加积分，负数减积分</el-row>
				
                <el-form-item label="备注：">
                    <el-input type="textarea" v-model="edit.remark" style="width: 250px;"/>
                </el-form-item>
            </el-form>
            <div class="text-center">
                <el-button @click="editCancel">取消</el-button>
                <el-button type="primary" @click="editConfirm">保存</el-button>
            </div>
        </el-dialog>
        
        <el-dialog class="dialogTable" width="800px" title="等级升级说明" :visible.sync="isIntroduction">
            <el-row>
				<h1>会员等级：</h1>
				<ul class="introduction-ul">
					<li>1.铜牌阶段:  0—10级，每级50分，累计500分。</li>
					<li>2.银牌阶段：11—20级，每级100分，累计1000分。</li>
					<li>3.金牌阶段：21—30级，每级150分，累计1500分。</li>
					<li>4.铂金阶段：31—40级，每级200分，累计2000分。</li>
					<li>5.钻石阶段：41—49级，每级250分，累计2250分。</li>
					<li>6.大师阶段：50级，每级2750分，累计2750分。</li>
					<li>合计：等级50级，分数10000分。  </li>
				</ul>
				                      
				<h1>“+”分项</h1>
				<ul class="introduction-ul">
					<li>1.  实名认证（+300分）</li>
					<li>2．交收单达到一定数量（每填写100吨+50分）</li>
					<li>3.  发布供求信息（每发布一条+2分）</li>
					<li>4.  好评累积（每1条好评+2分）</li>
					<li>5.  在线时长（一小时+1分，不足一小时按一小时计算）</li>
					<li>6.每日首次登陆（1分）</li>
					<li>7.签到抽积分（1   2  5   8   10   12  其中5概率最大40%，2占30%，1占20%，其他合占10%）</li>
					<li>备注：积分可用于年底抽奖写进积分规则，具体规则产品用一段时间再给出（预测200积分抽一次，暂定）</li>
				</ul>
				
				<h1>“-”分项</h1>
				<ul class="introduction-ul">
					<li>1.  货物品质与价格描述跟事实严重不符（后台人工判定，1次警告，后每次1次-20分）</li>
					<li>2.  盗用其他用户资质，货品介绍，货品图片等图片，视频信息（后台人工判定，1次警告，后每次次-20分）</li>
					<li>3.  恶意诋毁他人货品（后台人工判定，1次警告，2次-50分）</li>
					<li>4.  差评累积（每达到50条-50分）</li>
					<li>5.  举报（累计3条不同ID举报-50分）</li>
					<li>6.  恶意发布无关信息、虚假信息、虚假单据等（后台人工判定，一次警告，两次-4,3次-16,4次-64,以此类推，直至积分归零，情节严重者考虑注销号码）</li>
				</ul>
            </el-row>
            <div class="text-center">
                <el-button @click="IntroductionCancel">关闭</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import header from '~/components/header/header.vue'
    import moment from 'moment'
    
    //新增构造函数
    function Detail(){
    	this.row = '';
    	this.list = [];
    }
    function Edit(){
    	this.pointRuleType = '';
    	this.userIds = [];
    	this.pointsChanged = '';
    	this.pointsChangedType = '';//REDUCTION
    	this.remark = '';
    	
    	//删除
    	this.disabled = false;
    }
    
    let vm;
    export default{
        data(){
            return {
            	api: this.apiUrl.customerIntegral,
            	name: '',//用户昵称
                //breadcrumb
                firsTit:'积分',
                secondTit:'客户积分',
                thirdTit:'',
                breadflag:false,
                /*header参数*/
                viewFlag:false,
                showFlag:true, //true 显示table页; false 显示form页
                loadCircle:false,//加载显示
                dataList:[],
                
                //查看流水
                isDetail: false,
                detail: new Detail(),
                
                //等级升级说明
                isIntroduction: false,
                
                //修改积分
                isEditing: false,
                edit: new Edit(),
                editIntegral: [],

//				分页容器
				currentNo: 1,
				pageCount: 1,
				pageSize: 10,
				total: 0,
				currentNo2: 1,
				pageCount2: 1,
				pageSize2: 10,
				total2: 0,
            }
        },
        activated(){
            vm = this;
            vm.retrieve(1);
            vm.getRulesList();
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
			sizeChange2(size) {
				vm.getDetail(vm.currentNo2, size);
			},
			introduction(){
				vm.isIntroduction = true;
			},
			IntroductionCancel(){
				vm.isIntroduction = false;
			},
			//详情
			goDetail(row){
				vm.showFlag = false;
				vm.isDetail = false;
				vm.detail = new Detail();
				vm.detail.row = row;
				vm.isDetail = true;
				vm.getDetail();
			},
			getDetail(currentNo, size) {
				if (size) {
					vm.pageSize = size;
				}
				vm.currentNo2 = currentNo;
                let params={
                    url:vm.api.records,
                    data:{
                    	id: vm.detail.row.userId,
                        pageNumber: currentNo,//页码
                        pageSize: vm.pageSize
                    }
                }
	            let callback = function(data){
	                vm.detail.list = data.data.rows;
					vm.pageCount2 = data.data.pageCount;
					vm.total2 = data.data.totalElements;
	            }
                vm.ax.post(params, callback, vm);
			},
			//编辑
			goEdit(row){
                vm.breadflag=true;
                vm.isEditing = true;
                vm.edit = new Edit();
                vm.edit.userIds.push(row.userId);
			},
			editCancel(){
                vm.isEditing = false;
			},
			//获取积分规则列表
			getRulesList(){
                let params={
                    url:vm.api.pointRules,
                    data:{
                    }
                }
	            let callback = function(data){
	                vm.editIntegral = data.data;
	            }
                vm.ax.post(params, callback, vm);
			},
			editConfirm(){
				if(!vm.edit.pointsChanged&&!vm.edit.pointRuleType){
					vm.$message.error("请输入积分或选择理由");
				}else{
					var editCopy = vm.objCopy(vm.edit);
					if(vm.edit.pointsChanged){
						editCopy.pointsChangedType = vm.edit.pointsChanged<0?'REDUCTION':'PLUS';
						editCopy.pointsChanged = vm.edit.pointsChanged.replace(/\-/,"");
					}
					delete editCopy.disabled;
	                let params={
	                    url:vm.api.update,
	                    data: editCopy
	                }
		            let callback = function(data){
		                vm.$message.success("操作成功");
                		vm.isEditing = false;
                		vm.retrieve(vm.currentNo);
		            }
	                vm.ax.post(params, callback);
				}
			},
			reasonChange(row){
				vm.edit.disabled = vm.edit.pointRuleType?true:false;
				if(vm.edit.pointRuleType){
					vm.editIntegral.map(function(item){
						if(item.pointRuleType == vm.edit.pointRuleType){
							vm.edit.pointsChangedType = item.pointsChangedType;
							vm.edit.pointsChanged = item.pointsChangedType=='PLUS'?item.pointsChanged:'-'+item.pointsChanged;
						}
					})
				}
			},
            //back to tablePage
            backTablePage(){
                vm.showFlag=true;
                vm.isAdding = false;
                vm.isEditting = false;
                vm.initialPage();//初始化页面标志
                console.log(vm.currentNo)
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
				console.log(currentNo)
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
