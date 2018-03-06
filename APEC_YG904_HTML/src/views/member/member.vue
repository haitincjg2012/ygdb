/**
* Created by gsy on 2017/8/19.
* 客户列表
*/
<template>
    <div class="memberList">
        <my-header v-on:initialPage="initialPage" :firstTitle="firsTit" :secondTitle="secondTit" :thirdTitle="thirdTit" :breadFlag="breadflag"></my-header>
        <!--<my-header v-on:initialPage="initialPage" :fatherTitle="fatitle" :headTitle="title"></my-header>-->
        <!--table Page-->
        <div id="tablePage" v-if="showFlag">
            <div class="mysearch">
            	<!--<el-form :inline="true" class="search" label-width="110px">
            		<el-form-item label="是否认证">
                        <el-select v-model="pushFlag" @change="pushFlagChange">
                            <el-option label="已认证" value="Y"></el-option>
                            <el-option label="未认证" value="N"></el-option>
                        </el-select>
                    </el-form-item>
                </el-form>-->
                <el-form :inline="true" class="search clearfix" label-width="110px">
                   <!-- <el-form-item label="区域">
                        <el-select v-model="sear_cityid" placeholder="请选择市" @change="changeCity(sear_cityid,'sear')">
                            <el-option :label="item.name" :value="item.code" v-for="item in cityData" :key="item.id"></el-option>
                        </el-select>
                        <el-select v-model="sear_areaid" placeholder="请选择区">
                            <el-option :label="item.name" :value="item.code" v-for="item in areaData" :key="item.id"></el-option>
                        </el-select>
                    </el-form-item>-->
                    <el-form-item label="昵称">
                        <el-input v-model="sear_name" placeholder="请输入昵称" @click.enter.native.native="getTableList(1)"></el-input>
                    </el-form-item>
                    <el-form-item label="手机号">
                        <el-input v-model="sear_mobile" placeholder="请输入手机号" @click.enter.native="getTableList(1)"></el-input>
                    </el-form-item>
                    <el-form-item label="用户身份">
                        <el-select v-model="sear_userType" placeholder="请选择用户身份">
                            <el-option label="代办" value="DB"></el-option>
                            <el-option label="客商" value="KS"></el-option>
                            <el-option label="冷库" value="LK"></el-option>
                            <el-option label="果农" value="ZZH"></el-option>
                            <el-option label="合作社" value="HZS"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="用户账户类型">
                        <el-select v-model="sear_userAccountType" placeholder="请选择用户账户类型">
                            <el-option label="组织账户主体账号" value="ORG_MAIN_ACCOUNT"></el-option>
                            <el-option label="组织账户子级账号" value="ORG_CHILD_ACCOUNT"></el-option>
                            <el-option label="个体账号" value="IND_MAIN_ACCOUNT"></el-option>
                        </el-select>
                    </el-form-item>
            		<el-form-item label="是否认证">
                        <el-select v-model="sear_pushFlag" @change="pushFlagChange">
                            <el-option label="已认证" value="Y"></el-option>
                            <el-option label="未认证" value="N"></el-option>
                        </el-select>
                    </el-form-item>
					<el-form-item label="开始时间：">
	                    <el-date-picker type="date" placeholder="开始时间" format="yyyy-MM-dd" v-model="sear_startDate" @click.enter.native="getTableList(1)"></el-date-picker>
					</el-form-item>
					<el-form-item label="结束时间：">
	                    <el-date-picker type="date" placeholder="结束时间" format="yyyy-MM-dd" v-model="sear_endDate" @click.enter.native="getTableList(1)"></el-date-picker>
					</el-form-item>
                    <el-button type="primary" @click="onSearch('cusomer')">搜索</el-button>
					<form id="form" method="post" style="display: none;" action="" enctype="multipart/form-data"></form>
                    <el-button type="primary" @click="goExport">导出</el-button>
                    <el-button @click="toCreate()">新增</el-button>
                    <el-button @click="onReset('cusomer')">重置</el-button>
                </el-form>
            </div>
            <!--批量删除-->
            <div class="batchGroup">
                <el-button type="primary" @click="authen">认证</el-button>
                <el-button type="primary" @click="bindProp('org')" :disabled="bindDisaleFlag">绑定组织</el-button>
                <el-button type="primary" @click="bindProp('account')">设置账户类型</el-button>
                <!--<el-button type="primary" @click="batchDel">批量删除</el-button>-->
            </div>
            <!--表格列表-->
            <div class="tableList">
                <el-table :data="dataList" ref="memberTable" header-row-class-name="headerRow"  highlight-current-row border stripe v-loading.body="loadCircle" style="width:100%;" @row-click="selUserinfo" @selection-change="handleChange">
                <!--<el-table :data="dataList" ref="memberTable"  highlight-current-row border stripe v-loading.body="loadCircle" style="width:100%;" @current-change="selUserid" @selection-change="selsChange">-->
                    <el-table-column type="selection" width="35" fixed="left"></el-table-column>
                    <el-table-column prop="id" label="编号"></el-table-column>
                    <el-table-column prop="name" label="昵称"></el-table-column>
                    <el-table-column prop="mobile" label="电话"></el-table-column>
                    <!--<el-table-column prop="userType" label="身份类型"></el-table-column>-->
                    <el-table-column label="身份">
                        <template slot-scope="scope">
                            <div>{{scope.row.userTypeKey}}<span v-if="scope.row.userDetailTypeKey">/</span>{{scope.row.userDetailTypeKey}}</div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="orgName" label="企业名称"></el-table-column>
                    <!-- <el-table-column prop="orgAccountType" label="客户类型"></el-table-column> -->
                    <el-table-column prop="orgAccountTypeKey" label="客户类型"></el-table-column>
                    <el-table-column prop="userAccountTypeKey" label="账户类型"></el-table-column>
                    <el-table-column prop="pushFlag" label="认证标识">
                        <template slot-scope="scope">
                            <!--<span>{{scope.row.pushFlag|pushFlagfilter}}</span>-->
                            <span v-if="scope.row.pushFlag==true">已认证</span>
                            <span v-else>未认证</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="userLevelKey" label="等级"></el-table-column>
                    <el-table-column prop="availablePoints" label="积分"></el-table-column>
                    <el-table-column prop="createDate" label="创建时间">
                            <template slot-scope="scope">{{scope.row.createDate|timeFilter}}</template>
                    </el-table-column>
                    <el-table-column prop="userStatusKey" label="账户状态"></el-table-column>
                    <!--<el-table-column prop="userStatus" label="账户状态码"></el-table-column>-->
                    <el-table-column label="操作" width="250" v-loading.body="true" fixed="right">
                        <template slot-scope="scope"  >
                            <el-button type="text" @click.stop="goDetail('view',scope.row.id)">详情</el-button>
                            <el-button type="text"  v-if="scope.row.userType!='ZZH' && scope.row.userType!='HZS' && !scope.row.pushFlag && scope.row.userAccountType!='ORG_CHILD_ACCOUNT'"   @click.stop="changeStatus('certificate',scope.row.id)">认证</el-button>
                            <el-button type="text" v-if="scope.row.pushFlag && scope.row.userAccountType!='ORG_CHILD_ACCOUNT'" @click.stop="changeStatus('calCertificate',scope.row.id)">取消认证</el-button>
                            <el-button type="text" v-if="scope.row.userStatus!='FREEZE'" @click.stop="changeAccount('disable',scope.row.id)">禁用</el-button>
                            <el-button type="text" v-if="scope.row.userStatus=='FREEZE'" @click.stop="changeAccount('enable',scope.row.id)">启用</el-button>
                            <el-button type="text" v-if="scope.row.userAccountType=='ORG_CHILD_ACCOUNT'" @click.stop="unbindOrg(scope.row.id)">解绑组织</el-button>
                            <el-button type="text" @click.stop="invitedUser(scope.row)">受邀用户</el-button>
                            <!--<el-button type="text" @click="goDetail('edit',scope.row.id)">编辑</el-button>-->
                            <!--<el-button type="text" @click="delTablelist(scope.row.id)">删除</el-button>-->
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination class="pageNation" @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pNum"
                               :page-sizes="[10,15,20,25,30]" :page-size="pSize" layout="total,sizes,prev,pager,next,jumper"
                               :total="pageData.totalElements" v-show="pageData.totalElements>10"></el-pagination>
            </div>
        </div>
        <!--客户form Page-->
        <el-row id="formPage" v-if="!showFlag && isDetail" style="width: 1000px;">
        	<el-col :span="12">
        		<h1 style="font-weight: bold; font-size: 16px; margin-top: 20px;">个人信息</h1>
	            <el-form ref="memberForm" :rules="memberRules" class="myformDetail memeberform" :class="{formBorder:viewFlag}">
	                <el-form-item label="编号">
	                    <el-input v-model="memberForm.id" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="昵称">
	                    <el-input v-model="memberForm.name" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="姓名">
	                    <el-input v-model="memberForm.realName" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="性别">
	                    <el-input v-model="memberForm.sexValue" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="电话">
	                    <el-input v-model="memberForm.mobile" :disabled="true"></el-input>
	                </el-form-item>
	                <el-form-item label="区域" :inline="true" v-if="!viewFlag">
	                    <!--修改市-->
	                    <el-select v-model="memberForm.cityId" class="selRegion" @change="changeCity(memberForm.cityId,'edit')">
	                        <el-option :label="item.name" :value="item.code" v-for="item in cityData" :key="item.id"></el-option>
	                    </el-select>
	                    <!--修改区-->
	                    <el-select v-model="memberForm.areaId" class="selRegion" @change="changeArea(memberForm.areaId)">
	                        <el-option :label="item.name" :value="item.code" v-for="item in areaData" :key="item.id"></el-option>
	                    </el-select>
	                    <!--修改街道-->
	                    <el-select v-model="memberForm.townId" class="selRegion">
	                        <el-option :label="item.name" :value="item.code" v-for="item in townData" :key="item.id"></el-option>
	                    </el-select>
	                </el-form-item>
	                <el-form-item label="用户主身份">
	                    <el-input v-model="memberForm.userTypeKey" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="工作年限">
	                    <el-input v-model="memberForm.workOfYear" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="用户子身份">
	                    <el-input v-model="memberForm.userDetailTypeKey" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="账户类型">
	                    <el-input v-model="memberForm.userAccountTypeKey" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="等级">
	                    <el-input v-model="memberForm.userPoint.userLevelKey" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="积分">
	                    <el-input v-model="memberForm.userPoint.availablePoints" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="创建时间">
	                    <el-input v-model="memberForm.createDate" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="账户状态">
	                    <el-input v-model="memberForm.userStatusKey" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="实名状态信息">
	                    <el-input v-model="memberForm.userRealAuthKey" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item class="btnGroup">
	                    <el-button type="primary" @click="submitForm" v-if="editFlag">保存</el-button>
	                </el-form-item>
	            </el-form>
	        </el-col>
	        <!--企业信息-->
        	<el-col :span="12">
        		<h1 style="font-weight: bold; font-size: 16px; margin-top: 20px;">企业信息</h1>
	            <el-form ref="memberForm" :rules="memberRules" class="myformDetail memeberform" :class="{formBorder:viewFlag}">
	            	<span v-for="item in create.userArray">
	            		<el-form-item :label="item.menuName" v-if="viewFlag">
		                    <el-input v-model="memberForm.userOrgClientVO[item.nameStr]" :disabled="true"></el-input>
		                </el-form-item>
	            	</span>
	                <!--<el-form-item label="区域：" v-if="viewFlag">
	                    <el-input v-model="memberForm.userOrgClientVO.address" :disabled="true"></el-input>
	                </el-form-item>
	                <el-form-item label="企业名称：">
	                    <el-input v-model="memberForm.userOrgClientVO.orgName" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="客户类型：">
	                    <el-input v-model="memberForm.userOrgClientVO.userAccountTypeKey" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="认证标识：">
	                    <el-input v-model="memberForm.userOrgClientVO.pushFlag" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="主营品种：">
	                    <el-input v-model="memberForm.userOrgClientVO.mainOperating" :disabled="viewFlag"></el-input>
	                </el-form-item>
	                <el-form-item label="实力描述：">
	                    <el-input v-model="memberForm.userOrgClientVO.remark" :autosize="{minRows:1}" type="textarea" :disabled="viewFlag"></el-input>
	                </el-form-item>-->
	            </el-form>
            </el-col>
            <el-col class="text-center" :span="24">
                <el-button @click="backTablePage">返回</el-button>
            </el-col>
        </el-row>
        <!--受邀用户-->
        <el-row v-if="!showFlag && !isDetail">
            <el-table :data="invitedUserList" highlight-current-row border stripe style="width:100%;" v-loading.body="loadCirOrg">
                <!--<el-table-column type="selection"></el-table-column>-->
                <el-table-column prop="imgUrl" label="头像">
                	<template slot-scope="scope">
                    	<img :src="scope.row.imgUrl+'?x-oss-process=style/_list'" class="org-img" v-if="scope.row.imgUrl" style="height: 35px;width: 35px;">
						<img src="./../../assets/images/DBKS.png" v-else style="height: 35px;width: 35px;">
                	</template>
                </el-table-column>
                <el-table-column prop="name" label="姓名"></el-table-column>
                <el-table-column prop="userTypeKey" label="身份"></el-table-column>
                <el-table-column prop="mobile" label="手机号"></el-table-column>
                <el-table-column label="注册时间">
                	<template slot-scope="scope">
                		{{filters.formatDatetime(scope.row.createDate)}}
                	</template>
                </el-table-column>
            </el-table>
            <fe-page style="text-align: right;" @retrieve="getinvitedUserList" :data="fePageData"></fe-page>
            <div class="text-center">
                <el-button @click="backTablePage">返回</el-button>
            </div>
        </el-row>
        <!--选择绑定的组织  window-->
        <el-dialog class="dialogTable" title="选择绑定的组织" :visible.sync="showOrgFlag">
            <!--搜索-->
            <div class="mysearch orgSearch">
                <el-form :inline="true" class="search">
                    <el-form-item label="企业名称">
                        <el-input v-model="org_orgName" placeholder="请输入企业名称"></el-input>
                    </el-form-item>
                    <el-form-item label="库存量">
                        <el-input v-model="org_orgStockCap" placeholder="请输入库存量"></el-input>
                    </el-form-item>
                    <el-button type="primary" @click="onSearch('org')">搜索</el-button>
                    <el-button @click="onReset('org')">重置</el-button>
                </el-form>
            </div>
            <!--组织列表-->
            <el-table :data="orgList" highlight-current-row border stripe style="width:100%;" @row-click="selsOrg" v-loading.body="loadCirOrg">
                <!--<el-table-column type="selection"></el-table-column>-->
                <el-table-column prop="orgName" label="企业名称"></el-table-column>
                <el-table-column prop="orgClientUsers" label="拥有的客户"></el-table-column>
                <el-table-column prop="mainOperating" label="主营品种"></el-table-column>
                <el-table-column prop="address" label="所在地区"></el-table-column>
                <el-table-column prop="orgStockCap" label="库存量"></el-table-column>
            </el-table>
            <el-pagination class="pageNation" @current-change="orgCurrentChange" :current-page="1"
            layout="total,prev,pager,next" :total="orgpageData.totalElements":page-size="orgpSize"
            v-show="orgpageData.totalElements > orgpSize"></el-pagination>
            <div slot="footer" class="dialog-footer">
                <el-button @click="closeWin('org')">取消</el-button>
                <el-button type="primary" @click.stop="submitWin('org')">提交</el-button>
            </div>
        </el-dialog>
        <!--设置账户类型  window-->
        <el-dialog class="dialogTable" title="设置账户类型" :visible.sync="showAccountFlag">
            <el-form :model="accountForm" class="accountForm">
                <el-form-item label="昵称">
                    <el-input v-model="accountForm.name" :readonly="true"/>
                </el-form-item>
                <el-form-item label="电话">
                    <el-input v-model="accountForm.mobile" :readonly="true"/>
                </el-form-item>
                <el-form-item label="身份">
                    <el-input v-model="accountForm.identify" :readonly="true"/>
                </el-form-item>
                <el-form-item label="企业名称">
                    <el-input v-model="accountForm.rgName" :readonly="true"/>
                </el-form-item>
                <el-form-item label="客户类型">
                    <el-radio-group v-model="accountForm.chooseType" @change="selCustomnType">
                        <el-radio label="company">企业</el-radio>
                        <el-radio label="IND_MAIN_ACCOUNT" :disabled="indDisaleFlag">个体</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="账户类型" v-if="showFirmflag" @change="selAccType">
                    <el-radio-group v-model="accountForm.accountType">
                        <el-radio label="ORG_MAIN_ACCOUNT">主账户</el-radio>
                        <el-radio label="ORG_CHILD_ACCOUNT" :disabled="childDisaleFlag">子账户</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="closeWin('account')">取消</el-button>
                <el-button type="primary" @click.stop="submitWin('account')">提交</el-button>
            </div>
        </el-dialog>
        
        <!-- 新增 -->
        <el-dialog class="dialogTable" width="730px" title="新增客户" :visible.sync="isCreating">
            <el-form :inline="true" label-width="100px">
                <el-form-item label="昵称">
                    <el-input v-model="create.name" />
                </el-form-item>
                <el-form-item label="电话" required>
                    <el-input v-model="create.mobile" @blur="checkPhone" />
                </el-form-item>
                <el-form-item label="密码">
                    <el-input v-model="create.password" />
                </el-form-item>
                <el-form-item label="身份" required>
					<el-select clearable v-model="create.userType" placeholder="请选择" @change="userTypeChange">
						<el-option
							v-for="item in userTypeList"
							:key="item.value"
							:value="item.value" 
							:label="item.name">
						</el-option>
					</el-select>
                </el-form-item>
                <el-form-item label="客户类型">
					<el-select clearable v-model="create.userAccountType" placeholder="请选择">
						<el-option
							v-for="item in customerTypeList"
							:key="item.value"
							:value="item.value" 
							:label="item.name">
						</el-option>
					</el-select>
                </el-form-item>
                <div v-for="item in create.userArray">
                	<span v-if="item.nameStr == 'address'">
		                <el-form-item :label="item.menuName">
							<el-cascader style="width: 520px;"
								id="address"
							    :options="options"
							    v-model="create.userOrgClientVO[item.nameStr]"
							    :props="props"
							    @active-item-change="handleItemChange"
								expand-trigger="hover"
							>
							</el-cascader>
		                </el-form-item>
            		</span>
            		<span v-else>
		                <el-form-item :label="item.menuName">
		                    <el-input v-model="create.userOrgClientVO[item.nameStr]" style="width: 520px;"/>
		                </el-form-item>
	                </span>
                </div>
            </el-form>
            <div class="text-center">
                <el-button @click="createCancel">取消</el-button>
                <el-button type="primary" @click="createConfirm">保存</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
    import header from '~/components/header/header.vue'
    import FePage from '~/components/pagination.vue'
    
    let vm;
    //新增构造函数
    function Create(){
    	this.name = '';
    	this.mobile = '';
    	this.password = '';
    	this.userType = '';
    	this.userAccountType = '';
    	this.userOrgClientVO = {
//  		orgName: '',
//  		mainOperating: '',//主营品种
//  		userAccountType: '',
//  		remark: ''//实力描述
    	};
    	this.userArray = [];//数据用数组，提交时删除
    }
    
    export default {
        data () {
            return {
                //面包屑
                firsTit:'客户',
                secondTit:'客户管理',
                thirdTit:'',
                breadflag:false,
                showFlag:true, //true 显示table页; false 显示form页
                isDetail: false,//详情显示
                showOrgFlag:false,//"选择绑定的组织"window
                showAccountFlag:false,//设置账户类型
                showFirmflag:false,//设置是否显示账户类型
                viewFlag:false,//表单只读标识
                editFlag:false,//表单编辑标识
                addFlag:false,//表单新增标识
                certFlag:null,//认证状态 推送时传送true,其他为false
                accountFlag:null,//账户禁用/启用状态 （true、false）
                cityData:[], //市
                areaData:[], //区
                townData:[], //镇
                //客户table‘搜索’字段
                sear_name:"",//客户姓名
                sear_mobile:"",//手机号
                sear_startDate: null,
                sear_endDate: null,
                sear_userType:null,//用户身份
                sear_pushFlag: "",//是否认证
                sear_userAccountType:null,//用户账户类型
                //组织table '搜索'字段
                org_orgName:"",//企业名称
                org_orgStockCap:"",//库存量
                //客户列表数据
                dataList:[],
                userid:null,//客户id
                //受邀用户数组
                invitedUserId: '',
                invitedUserList: [],
                fePageData: '',
                //组织列表数据
                orgList:[],
                orgid:null,//组织id
                //设置账户类型
                // setAccountType:null,
                setType:null,
                setDetailType:null,
                accountForm:{
                    name:"",//昵称
                    mobile:"",//电话
                    identify:"",//身份
                    rgName:"",//企业名称
                    accountType:null,//用户账号类型
                    chooseType:""//选择企业类型
                },
                accountInfo: this.keys.menber.menber.accountInfo,//用户账号类型
                //批量删除ids
                idsArray:[],
                //客户列表分页信息
                pageData:[],//分页数据信息
                pNum:1,//页码
                pSize:10,//页容量
                // 组织列表分页信息
                orgpageData:[],//分页数据信息
                orgpNum:1,//页码
                orgpSize:4,//页容量
                //客户列表‘转圈’加载
                loadCircle:false,
                //组织列表‘转圈’加载
                loadCirOrg:false,
                //操作列表转圈
                loadCirOpr:true,
                //当市改变时候清空 区和镇
                clearRegion:false,
                //详情页
                dataDetailList:[],//详情页数据集
                operateType:"", //view：详情 edit:编辑
                bindDisaleFlag:false,//绑定组织按钮是否禁用
                indDisaleFlag:false,//设置账户类型时个体是否禁选
                childDisaleFlag:false,//设置账户类型时子账号是否禁选
                memberForm:{
                    id:"",//编号
                    name:"",//客户姓名
                    mobile:"",//电话
                    cityId:"",//市
                    areaId:"",//区
                    townId:"",//街道
                    createDate:"",//创建日期
                    userTypeKey:"",//用户主身份
                    userDetailTypeKey:"",//用户子身份
                    realName: "",
                    sexValue: "",
                    workOfYear: "",
                    userRealAuthKey: "",
                    realName: "",
                    userType: "",
                    userOrgClientVO:{
                        mainOperating:"",//主营品种
                        address:"",//区域
                        orgName:"", //企业名称
                        userAccountTypeKey:"",//客户类型
                        pushFlag:"", //认证标识
                        remark:""
                    },
                    userPoint:{
                        userLevelKey:"",//等级
                        availablePoints:""//积分
                    },
                    userAccountTypeKey:"",//账户类型
                    userStatusKey:""//账户状态

                },
                memberRules:{//暂时无须校验字段,暂放
                    name:[
                        //{required:true,message:"请输入客户名称",trigger:"blur"}
                    ],//客户姓名
                    cityId:[{}],//市
                    areaId:[{}],//区
                    townId:[{}],//街道
                    mainOperating:[{}],//主营品种
                    strengthDescription:[{}]//实力描述
                },
                //新增部分
                isCreating: false,//显示新增弹框
                create: new Create(),
                userTypeList: this.keys.menber.menber.userTypeList,//用户类型列表
                customerTypeList: this.keys.menber.menber.customerType,
                userAccountTypeList: this.keys.menber.menber.userAccountType,
                options: [{
		          name: '烟台市',
		          code: 3706,
		          cities: []
		        },{
		          name: '威海市',
		          code: 3710,
		          cities: []
		        },{
		          name: '其他',
		          code: 1,
		        }],
                props: {
		            value: 'code',
		            label: 'name',
		            children: 'cities'
		        },
		        selectedVal: [],
            }
        },
        activated(){
            var vm=this;
        	vm.getTableList();//获取列表信息
//            vm.getCityList();//获取市信息
        },
        mounted(){
            vm=this;
            vm.initialPage();//初始化页面
        },
        deactivated(){
            var vm=this;
//            vm.loadCircle=false;//避免超时跳到首页出现加载图标
//            vm.loadCirOrg=false;
//            console.log("客户页离开了。。。不送");
        },
        filters:{
            pushflagFilter(val){
                return val==true?"已认证":"未认证";
            }
        },
        methods: {
        	//受邀用户
        	invitedUser(row){
        		vm.showFlag = false;
        		vm.invitedUserId = row.id;
//      		vm.getinvitedUserList(1, 10);
        	},
        	getinvitedUserList(currentNo, size){
        		if(size) {
        			vm.size = size;
        		}
                vm.loadCircle=true;
                let params={
                    url:vm.apiUrl.member.invitedUsers,
                    data:{
                    	id: vm.invitedUserId,
                        //分页信息
                        pageNumber: currentNo,//页码
                        pageSize: vm.size //页容量
                    }
                };
                let callback = function(data){
                	vm.invitedUserList = data.data.rows;
                	vm.fePageData = data.data;
                }
                vm.ax.post(params, callback, vm);
        	},
        	//新增按钮点击
        	toCreate(){
        		vm.isCreating = true;
        		vm.create = new Create();
        	},
        	//是否认证切换
        	pushFlagChange(){
        		vm.getTableList(1);
        	},
        	//批量认证
        	authen(){
        		if(vm.selectVal.length<1){
        			vm.$message.error("请至少选择一个");
        		}else{
        			let ids = [];
        			vm.selectVal.map(function(item){
        				ids.push(item.id);
        			})
        			let params = {
	                    url:vm.apiUrl.member.authen,
	                    data: {ids: ids}
	                };
	                let callback = function(data){
	                	vm.$message.success("认证通过");
	                	vm.isCreating = false;
	                	vm.getTableList();//刷新表格
	                }
	                vm.ax.post(params, callback);
        		}
        	},
        	//新增确认
        	createConfirm(){
        		let createCopy = vm.objCopy(vm.create);
        		if(vm.create.userOrgClientVO.address&&vm.create.userOrgClientVO.address.length>0){
        			let address = document.getElementById("address").innerText.replace(/\s*\/\s*/g, "");
//      			address = vm.create.userOrgClientVO.address.join(",");
        			createCopy.userOrgClientVO.address = address;
        		}
        		delete createCopy.userArray;//删除此数组
                let params = {
                    url:vm.apiUrl.member.create,
                    data: createCopy
                };
                let callback = function(data){
                	vm.$message.success("新增成功");
                	vm.isCreating = false;
                	vm.getTableList();
                }
        		if(vm.create.mobile.length!=11){
        			vm.$message.error("请输入正确的手机号");
        		}else if(!vm.create.userType){
        			vm.$message.error("请填写身份");
        		}else{
        			console.log(createCopy);
        			vm.ax.post(params, callback);
        		}
        	},
        	//新增取消
        	createCancel(){
        		vm.isCreating = false;
        	},
        	checkPhone(){
                let params={
                    url:vm.apiUrl.member.checkPhone,
                    data:{
                        mobile: vm.create.mobile
                    }
                };
                let callback = function(data){
                	if(data.data==true){
                		vm.$message.error("手机号已经注册过了");
                		vm.create.mobile = "";
                	}
                }
        		if(vm.create.mobile.length!=11&&vm.create.mobile.length>0){
        			vm.$message.error("请输入正确的手机号");
        		}else if(vm.create.mobile.length==11){
        			vm.ax.post(params, callback);
        		}
        	},
        	//用户身份改变
        	userTypeChange(type, userType, call){
                let params={
                    url:vm.apiUrl.member.getInfo,
                    data:{
                        userType: vm.create.userType
                    }
                };
                if(type == 'detail'){
                	params.data = {
                		userType: userType
                	}
                }
                let callback = function(data){
                	vm.create.userArray = data.data;
                	if(type == 'detail'){
                		call();
                	}
                }
    			vm.ax.post(params, callback);
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
			handleChange(val){
				vm.selectVal = val;
			},
			
            //设置账户win：选择客户类型
            selCustomnType(val){
//                console.log("val:"+val);
                if(val=='company'){
                    vm.showFirmflag=true;
                }
                else{
                    vm.showFirmflag=false;
                    vm.accountForm.accountType=vm.accountForm.chooseType;
                }
            },
            //设置账户win：选择账户类型
            selAccType(val){
//                console.log("账户类型val:"+val);
                vm.accountForm.accountType=val;
            },

            //组织/设置账户win show
            bindProp(type){
              if(!vm.userid){
                  vm.$message({
                      message:"请选择客户列表数据！",
                      type:'warning'
                  });
              }
              else if(type=='org'){//绑定组织
                  if(vm.orgList.length==0){
                      vm.getOrglist();//获取组织列表
                  }
                  else{
                      vm.showOrgFlag=true;//显示组织列表
                  }

              }
              else if(type=='account'){//设置账户
                  vm.showAccountFlag=true;
              }
            },
            //window submit
            submitWin(type){ //推送组织(org)、设置账户类型（account）
                vm.doUserorg(type);
            },
            //window close
            closeWin(type){
                if(type=='org'){//关闭组织账户win
                    vm.showOrgFlag=false;
                    if(vm.orgid){
                        vm.orgid=null;
                    }
                }
                else if(type=='account'){//关闭设置账户win
                    vm.showAccountFlag=false;
                }

//             vm.clearRowselect();//清空table row选择
            },
            //清空memberTable row选择（暂时不需要）
            clearRowselect(){
                console.log("清空行选择了");
                vm.$refs.memberTable.clearSelection();
            },
            //获取组织列表
            getOrglist(){
                vm.loadCirOrg=true;
                let params={
                    url:vm.apiUrl.member.orgtableUrl,
                    data:{
                        userAccountType:"ORG_ACCOUNT",//组织账户
                        //搜索
                        orgName:vm.org_orgName,  // 企业名称
                        orgStockCap:vm.org_orgStockCap,//库存量
                        //分页信息
                        pageNumber:vm.orgpNum,//页码
                        pageSize:vm.orgpSize //页容量
                    }
                };
                vm.ax.post(params,vm.getOrglistCb,vm);
            },
            getOrglistCb(data){
                vm.orgpageData=data.data;
                vm.orgList=data.data.rows;
                vm.showOrgFlag=true;//显示组织列表

            },
            //'认证'
            changeStatus(type,id,event){ //certificate 认证 calCertificate 取消认证

              /*  var evt = event || window.event;
                var target = evt.toElement || evt.srcElement;
                var parentDom=target.parentNode;
                target.style.color='grey';
                parentDom.disabled=true;*/

                if(type=='certificate'){
                    vm.userid=id;
                    vm.doUserorg(type);
                }
                else if(type=='calCertificate'){
                    vm.cancelCert(id);
                }
            },
            //取消组织认证
            cancelCert(id){
                vm.$confirm('确认取消认证当前选中账户码？','提示',{
                  confirmButtonText:'确定',
                  cancelButtonText:'取消',
                  type:'warning'
                }).then(() => {
                  var params={
                      url:vm.apiUrl.member.cancelCertUrl,
                      data:{
                          id:id //用户id
                      }
                  };
                  var cancelCertCb=function(data){
                      // var vm=this;
                      /*
                      console.log("cb-target:",target);
                      target.style.color='#409EFF';
                      target.parentNode.disabled=false;*/
                      vm.$message({
                          message:"取消认证成功！",
                          type:"success"
                      });
                      vm.getTableList();//获取列表信息
                  };
                  vm.ax.post(params,cancelCertCb);
                }).catch(() =>{

                });


            },

            //推送组织(org)、设置账户类型（account）,认证（certificate）
            doUserorg(type){
                var msg="";
                console.log("doUserorg type:"+type);
                if(type=='org'){ //绑定组织
                    msg="确定将选中的组织绑定到当前账户吗？";
                    var params={
                        url:vm.apiUrl.member.UserAndOrgUrl,
                        data:{
                            id:vm.userid,
                            userOrgId:vm.orgid
                        }
                    };
                }
                else if(type=='account'){ //设置账户
                  msg="确定将选中的账户类型设置到当前账户吗？";
                    var params={
                        url:vm.apiUrl.member.UserAndOrgUrl,
                        data:{
                            id:vm.userid,
                            userAccountType:vm.accountForm.accountType
                        }
                    };
                }
                else if(type=='certificate'){//认证
                  msg="确定认证当前选中账户吗？";
                    var params={
                        url:vm.apiUrl.member.UserAndOrgUrl,
                        data:{
                            id:vm.userid,
                            pushFlag:true //推送时传送true

                        }
                    };
                  }
                vm.$confirm(msg,'提示',{
                   confirmButtonText:'确定',
                   cancelButtonText:'取消',
                   type:'warning'
                }).then(() => {
                      var doUserorgCb=function(){
                        /*target.style.color='#409EFF';
                        target.parentNode.disabled=false;*/
                        vm.$message({
                            message:"设置成功！",
                            type:'success'
                        });
                        vm.showOrgFlag=false;//关闭 组织 window
                        vm.showAccountFlag=false;//关闭 账户 window
                        vm.getTableList();//获取列表信息
                      };

                  vm.ax.post(params,doUserorgCb);
                }).catch(() => {

                });
            },

            //批量删除(暂不需要)
            batchDel(){
                if(vm.idsArray.length==0){
                    vm.$message({
                        message:"请选择要删除的数据",
                        type:"warning"
                    });
                }
                else{
                    vm.delMoretable();

                }
            },
            //组织table行选择改变
            selsOrg(row){
                vm.orgid=row.id;
                console.log("org行数据：组织id:"+vm.orgid);
            },
            //客户table行选择改变
            selUserinfo(row, event, column){
            	console.log(JSON.stringify(event))
            	console.log(event)
                vm.showFirmflag = row.orgAccountType=="ORG_ACCOUNT"?true:false;//判断是否组织账户（企业）
                vm.bindDisaleFlag = row.userAccountType=="ORG_MAIN_ACCOUNT"?true:false;//主账号不能绑定组织
                vm.accountForm.accountType = row.userAccountType;//初始化账户类型
                vm.indDisaleFlag = row.orgAccountType=="ORG_ACCOUNT"?true:false;//主账号不能设置为个体
                vm.childDisaleFlag = row.orgAccountType=="IND_ACCOUNT"?true:false;//个体账号不能设置为组织子账户
                vm.accountForm.chooseType = row.orgAccountType=="IND_ACCOUNT"?"IND_MAIN_ACCOUNT":"company";//初始化客户类型
                vm.userid=row.id;
                vm.setAccountType=row.userAccountType;
                vm.setType=row.userType;
                vm.setDetailType=row.userDetailType;
                vm.accountForm.name=row.name;
                vm.accountForm.mobile=row.mobile;
                vm.accountForm.identify=row.userDetailTypeKey?row.userTypeKey+'/'+row.userDetailTypeKey:row.userTypeKey; //userTypeKey+userDetailTypeKey
                vm.accountForm.rgName=row.orgName;
                //如果数据为空，则显示“无”
                for(var i in vm.accountForm){
                  if(!vm.accountForm[i] && i!=="accountType" && i!=="chooseType"){
                      vm.accountForm[i]="无";
                  }
                };
                console.log("客户行数据：用户id:"+vm.userid
                    +" setAccountType:"+vm.setAccountType //用户账号类型
                    +" setType:"+vm.setType //用户身份
                    +" setDetailType:"+vm.setDetailType //用户子身份
                    +" name:"+vm.accountForm.name //昵称
                    +" mobile:"+vm.accountForm.mobile //电话
                    +" identify:"+vm.accountForm.identify  //身份
                    +" rgName:"+vm.accountForm.rgName //企业名称
                  /*  +" accountType:"+vm.accountForm.accountType //用户账号类型
                    +" chooseType:"+vm.accountForm.chooseType //选择企业类型*/
                );
            },
            //table行选择改变时触发（批量删除代码）
            selsChange(selection){
                vm.idsArray=[];
                vm.idsArray=selection.map(item => item.id);//获取所有选中行的id组成数组，以逗号分隔
                console.log("vm.idsArray:"+vm.idsArray);
            },
            //初始化页面
            initialPage(){
                vm.viewFlag=false;//表单只读标识
                vm.editFlag=false;//表单编辑标识
                vm.breadflag=false;
                vm.showFlag=true;//显示列表，隐藏表单
				vm.isDetail = false;
            },
            //批量删除table数据（暂时不要）
            delMoretable(){
                vm.$confirm('确认删除选中的数据吗？','提示',{
                    confirmButtonText:"确定",
                    cancelButtonText:"取消",
                    type:'warning'
                }).then(()=>{
                    let params={
                        url:vm.apiUrl.member.delMoretableUrl,
                        data:{
                            "ids":vm.idsArray //用户id
                        }
                    };
                vm.ax.post(params,vm.delMoretableCb);
            }).catch(()=>{
                    console.log("取消删除选中的数据");
                })


            },
            delMoretableCb(data){
                vm.$message({
                    message:"批量删除成功！",
                    type:'success'
                });
                vm.$refs.memberTable.clearSelection();//取消选择
                vm.getTableList();//获取列表信息
            },
            //禁用/启用table单条信息  disable/enable
            changeAccount(type,id){
                console.log("禁用/启用类型："+type+" id:"+id);
                if(type=="disable"){
                    vm.accountFlag=false;
                }
                else{
                    vm.accountFlag=true;
                }
               //确认提示框
                vm.$confirm('确认修改选中账户的状态吗？','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(()=>{
                   //禁用/启用方法
                    let params={
                        url:vm.apiUrl.member.delTableUrl,
                        data:{
                            id:id, //用户id
                            pushFlag:vm.accountFlag //启用 true 禁用 false
                        }
                    };
                    vm.ax.post(params,vm.changeAccountCb);
                }).catch(()=>{
                    console.log("已取消修改账户状态");
                });


            },
            changeAccountCb(data){
                vm.$message({
                    message:'账户状态修改成功！',
                    type:'success'
                });
                vm.getTableList();//获取列表信息
            },
            //'保存'编辑信息
            submitForm(){
                vm.editDetail();
            },
            //back to tablePage
            backTablePage(){
                vm.showFlag=true;
				vm.isDetail = false;
                vm.initialPage();//初始化页面标志
                vm.getTableList();//获取table数据
            },
            //编辑表单(save)
            editDetail(){
                console.log("编辑提交："+vm.memberForm.name);
                let params={
                    url:vm.apiUrl.member.editTableUrl,
                    data:{
                        id:vm.memberForm.id,
                        name:vm.memberForm.name,//用户姓名
                        address:vm.memberForm.address,//地址
                        mainOperating:vm.memberForm.mainOperating,//主营品种
                        strengthDescription:vm.memberForm.strengthDescription,//实力描述
                        cityId:vm.memberForm.cityId,//市
                        areaId:vm.memberForm.areaId,//区
                        townId:vm.memberForm.townId//镇

                    }
                }
                vm.ax.post(params,vm.editDetailCb);
            },
            editDetailCb(data){
                vm.$message({
                    message:'保存成功！',
                    type:'success'
                });
            },
            //go formPage    operateType:"" //view：详情 viewFlag=true; edit:编辑 viewFlag=false
            goDetail(type,id){
                vm.operateType=type;
                if(type=='view'){
                    vm.breadflag=true;
                    vm.viewFlag=true;
                    vm.editFlag=false;
                    vm.thirdTit='详情';
                }
                else if(type=='edit'){
                    vm.breadflag=true;
                    vm.editFlag=true;
                    vm.viewFlag=false;
                    vm.secondTit='编辑';
                }
                console.log("操作类型："+vm.operateType+"用户id:"+id);
                vm.getTableDetail(id);
            },
            //table详情info
            getTableDetail(id){
                let params={
                    url:vm.apiUrl.member.tableDetailUrl,
                    data:{
                        id:id //用户id
                    }
                }
                vm.ax.post(params,vm.tableDetailCb);
            },
            tableDetailCb(data){
              vm.showFlag=false;//显示form 隐藏table
			  vm.isDetail = true;
              vm.dataDetailList=data.data;
              if(vm.operateType=='edit'){
                  vm.getAreaList(data.data.cityId);//获取区列表
                  vm.getTownList(data.data.areaId);//获取镇列表
              }
              
              //根据用户类型不同展示不同信息
              vm.userTypeChange('detail', vm.dataDetailList.userType, dataCallBack);
              function dataCallBack(){
		    	  vm.create.userArray.map(function(item){
		    			vm.memberForm.userOrgClientVO[item.nameStr] = '';
		    	  })
	              //表单赋值
	              vm.getDataFomat(vm.memberForm,vm.dataDetailList);
	
	             //如果数据为空，显示“无”
	             for(var i in vm.memberForm){
	                    if(!vm.memberForm[i] && typeof vm.memberForm[i] !="object"){
	                        vm.memberForm[i]="无";
	                    }
	                    if(typeof vm.memberForm[i] == "object"){
	                        var temp = {}.toString.call(vm.memberForm[i]).slice(8, -1) =="Object" ? {}:[];
	                        for(var key in vm.memberForm[i]){
	                              if(vm.memberForm[i][key] == ""){
	                                  temp[key] = "无";
	                              }
	                            /*  else if(i == ""){
	
	                              }*/
	                              else {
	                                  temp[key] = vm.memberForm[i][key];
	                              }
	
	                        }
	
	                        vm.memberForm[i] = temp;
	                    }
	                }
	
	              setTimeout(function(){
	                 vm.clearRegion=true;
	              },1000);
              }
            },
            //数据格式转化
            /* vm.commonJs.getDataFomat(vm.memberForm,data.data);*/
            getDataFomat(obj1,obj2){
                for(var i in obj1){
                    for(var j in obj2){
                       if(i==j){
                           if(typeof obj1[i] !="object"){
                               if(i=="createDate"){
                                   console.log("createDate:"+obj1[i]);
                                   obj1[i]=vm.commonJs.timestampPattern(obj2[j]);
                               }
                               else{
                                   obj1[i]=obj2[j];
                               }
                           }
                           else if(typeof obj1[i] =="object"){
                               for(var key1 in obj1[i]){
                                   for(var key2 in obj2[j]){
                                       if(key1 == key2){
                                           if(key1=='pushFlag'){
                                               obj1[i][key1]=obj2[i][key1]==true?"已认证":"未认证";
                                           }
                                           else{
                                               obj1[i][key1]=obj2[j][key2];
//                                               console.log("key1:"+key1+" key2:"+key2+" i:"+i+" j:"+j);
//                                               console.log("key1:"+obj1[i][key1]+" key2:"+obj1[i][key2]);
                                           }
                                       }
                                   }

                               }

                           }
                       }
                    }
                }
                return obj1;
            },
            //市改变（暂不要）
            changeCity(id,type){
                console.log("市改变id："+id+"改变类型："+type);
                if(type=='sear'){
                    vm.sear_areaid="";
                }
                else if(type=='edit'){
                    console.log("edit在此");
                    if(vm.clearRegion){
                        vm.memberForm.areaId="";
                        vm.memberForm.townId="";
                    }

                }
                vm.getAreaList(id);//区
            },
            //区改变（暂不要）
            changeArea(id){
                if(vm.clearRegion){
                    vm.memberForm.townId="";
                }
                vm.getTownList(id);//镇
            },
            //搜索
            onSearch(type){
                if(type=='cusomer'){
                    //遍历市，获取市名
//                vm.sear_cityName=vm.commonJs.getArrayVal(vm.cityData,vm.sear_cityid,'code','name');
                    //遍历区，获取市名
//                vm.sear_areaName=vm.commonJs.getArrayVal(vm.areaData,vm.sear_areaid,'code','name');
                    //拼接地址
//                vm.sear_address=vm.sear_cityName+'|'+vm.sear_areaName;
                    vm.getTableList();
                }
                else if(type=='org'){
                    vm.getOrglist();
                }
            },
            //重置
            onReset(type){
                if(type=='cusomer'){
                    var reg=/^sear_[a-z0-9]+/;
                    for(var i in vm.$data){
                        if(reg.test(i)){
                            if(i!='sear_userType'|| i!='sear_userAccountType')
                                vm.$data[i]='';
                            else{
                                vm.$data[i]=null;
                            }
                        }
                    }
                    vm.getTableList();

                }
                else if(type=='org'){
                    var reg=/^org_[a-z0-9]+/;
                    for(var i in vm.$data){
                        if(reg.test(i)){
                            vm.$data[i]="";
                        }
                    }
                    vm.getOrglist();
                }

            },
            //获取市信息（暂不要）
            getCityList(){
                let params={
                    url:vm.apiUrl.member.regionUrl,
                    data:{
                        /*parentId:"",
                        name:""*/
                    }
                }
                vm.ax.post(params,vm.cityListCb);
            },
            cityListCb(data){
                vm.cityData=data.data;
                console.log("市长度："+data.data.length);
            },
            //获取区信息（暂不要）
            getAreaList(cityid){
                let params={
                    url:vm.apiUrl.member.regionUrl,
                    data:{
                        parentId:cityid
                    }
                }
                vm.ax.post(params,vm.areaListCb);
            },
            areaListCb(data){
                vm.areaData=data.data;
                console.log("区名："+vm.areaData[0].name)

            },
            //获取镇信息（暂不要）
            getTownList(areaid){
                let params={
                    url:vm.apiUrl.member.regionUrl,
                    data:{
                        parentId:areaid
                    }
                }
                vm.ax.post(params,vm.townListCb);
            },
            townListCb(data){
                vm.townData=data.data;

            },
            //获取表格数据
            getTableList(pNum){
            	console.log(pNum)
            	if(pNum){
            		vm.pNum = pNum;
            	}
                vm.userid = null;
                vm.loadCircle=true;
                let params={
                    url:vm.apiUrl.member.tableUrl,
                    data:{
                        name:vm.sear_name,//客户姓名
                        mobile:vm.sear_mobile,//手机
                        startDate: vm.sear_startDate,
                        endDate: vm.sear_endDate,
                        userType:vm.sear_userType,//用户身份
                        userAccountType:vm.sear_userAccountType,//用户账户类型
                        pushFlag: vm.sear_pushFlag,
                        //分页信息
                        pageNumber:vm.pNum,//页码
                        pageSize:vm.pSize //页容量
                    }
                };
                vm.ax.post(params,vm.tableListCb,vm);
            },
            //导出
            goExport(){
                var data = {
                        name:vm.sear_name,//客户姓名
                        mobile:vm.sear_mobile,//手机
                        startDate: vm.moment(vm.sear_startDate).format('YYYY-MM-DD'),
                        endDate: vm.moment(vm.sear_endDate).format('YYYY-MM-DD'),
                        userType:vm.sear_userType,//用户身份
                        userAccountType:vm.sear_userAccountType,//用户账户类型
                        pushFlag: vm.sear_pushFlag
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
                form.setAttribute("action", vm.apiUrl.member.export);
                form.submit();
            },
            tableListCb(data){
                vm.pageData=data.data;
                vm.dataList=data.data.rows;

            },
            //pageSize改变
            handleSizeChange(val){
                vm.pSize=val;
                vm.getTableList();
            },
            //客户页码改变
            handleCurrentChange(val){ //memeber 客户列表  org 组织
                vm.pNum=val;
                vm.getTableList();
            },
            //组织页码改变
            orgCurrentChange(val){ //memeber 客户列表  org 组织
                vm.orgpNum=val;
                vm.getOrglist();
            },
            unbindOrg(id){//子账户类型可解绑
            		let params={
                    url:vm.apiUrl.member.unbindUrl,
                    data:{
                    		id:id
                    }
                };
                    vm.ax.post(params,vm.unbindOrgCb);
            },
            unbindOrgCb(data){
                vm.$message({
                    message:"解绑成功！",
                    type:'success'
                });
                //账户类型置为空
                vm.accountForm.accountType="IND_MAIN_ACCOUNT";//个体账户

                vm.getTableList();//获取列表信息
            }
        },
        filters:{
            authFilter(val){
                return val=="UNREALAUTH"?"未认证":"已认证";
            },
            //暂时不用
            addrFilter(val){
                return val.replace(/"([^|]*)"/g,'');


            }
        },
        components:{
            'my-header':header,
            'FePage': FePage
        }

    }
</script>
<style scoped>
    @import '../../assets/css/member.css';
</style>
