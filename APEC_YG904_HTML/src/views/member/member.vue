/**
* Created by gsy on 2017/8/19.
* 客户列表
*/
<template>
    <div class="memberList">
        <my-header v-on:initialPage="initialPage" :fatherTitle="fatitle" :headTitle="title" :viewFlag="viewFlag" :editFlag="editFlag" :addFlag="addFlag" ></my-header>
        <!--table Page-->
        <div id="tablePage" v-if="showFlag">
            <!--搜索-->
            <div class="mysearch">
                <el-form :inline="true" class="search">
                   <!-- <el-form-item label="区域：">
                        <el-select v-model="sear_cityid" placeholder="请选择市" @change="changeCity(sear_cityid,'sear')">
                            <el-option :label="item.name" :value="item.code" v-for="item in cityData" :key="item.id"></el-option>
                        </el-select>
                        <el-select v-model="sear_areaid" placeholder="请选择区">
                            <el-option :label="item.name" :value="item.code" v-for="item in areaData" :key="item.id"></el-option>
                        </el-select>
                    </el-form-item>-->
                    <el-form-item label="昵称：">
                        <el-input v-model="sear_name" placeholder="请输入昵称"></el-input>
                    </el-form-item>
                    <el-form-item label="手机号：">
                        <el-input v-model="sear_mobile" placeholder="请输入手机号"></el-input>
                    </el-form-item>
                    <el-form-item label="用户身份：">
                        <el-select v-model="sear_userType" placeholder="请选择用户身份">
                            <el-option label="代办" value="DB"></el-option>
                            <el-option label="客商" value="KS"></el-option>
                            <el-option label="冷库" value="LK"></el-option>
                            <el-option label="果农" value="ZZH"></el-option>
                            <el-option label="合作社" value="HZS"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="用户账户类型：">
                        <el-select v-model="sear_userAccountType" placeholder="请选择用户账户类型">
                            <el-option label="组织账户主体账号" value="ORG_MAIN_ACCOUNT"></el-option>
                            <el-option label="组织账户子级账号" value="ORG_CHILD_ACCOUNT"></el-option>
                            <el-option label="个体账号" value="IND_MAIN_ACCOUNT"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-button type="primary" @click="onSearch('cusomer')">搜索</el-button>
                    <el-button @click="onReset('cusomer')">重置</el-button>
                </el-form>
            </div>
            <!--批量删除-->
            <div class="batchGroup">
                <el-button type="primary" @click="bindProp('org')">绑定组织</el-button>
                <el-button type="primary" @click="bindProp('account')">设置账户类型</el-button>
                <!--<el-button type="primary" @click="batchDel">批量删除</el-button>-->
            </div>
            <!--表格列表-->
            <div class="tableList">
                <el-table :data="dataList" ref="memberTable"  highlight-current-row border stripe v-loading.body="loadFlag" style="width:100%;" @row-click="selUserinfo">
                <!--<el-table :data="dataList" ref="memberTable"  highlight-current-row border stripe v-loading.body="loadFlag" style="width:100%;" @current-change="selUserid" @selection-change="selsChange">-->
                    <!--<el-table-column type="selection"></el-table-column>-->
                    <el-table-column prop="id" label="编号"></el-table-column>
                    <el-table-column prop="name" label="昵称"></el-table-column>
                    <el-table-column prop="mobile" label="电话"></el-table-column>
                    <!--<el-table-column prop="userType" label="身份类型"></el-table-column>-->
                    <el-table-column label="身份">
                        <template scope="scope">
                            <div>{{scope.row.userTypeKey}}<span v-if="scope.row.userDetailTypeKey">/</span>{{scope.row.userDetailTypeKey}}</div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="orgName" label="企业名称"></el-table-column>
                    <el-table-column prop="orgAccountTypeKey" label="客户类型"></el-table-column>
                    <el-table-column prop="userAccountTypeKey" label="账户类型"></el-table-column>
                    <el-table-column prop="pushFlag" label="认证标识">
                        <template scope="scope">
                            <!--<span>{{scope.row.pushFlag|pushFlagfilter}}</span>-->
                            <span v-if="scope.row.pushFlag==true">已认证</span>
                            <span v-else>未认证</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="userLevelKey" label="等级"></el-table-column>
                    <el-table-column prop="availablePoints" label="积分"></el-table-column>
                    <el-table-column prop="createDate" label="创建时间">
                            <template scope="scope">{{scope.row.createDate|timeFilter}}</template>
                    </el-table-column>
                    <el-table-column prop="userStatusKey" label="账户状态"></el-table-column>
                    <!--<el-table-column prop="userStatus" label="账户状态码"></el-table-column>-->
                    <el-table-column label="操作">
                        <template scope="scope">
                            <el-button type="text" @click="goDetail('view',scope.row.id)">详情</el-button>
                            <!--<el-button type="text" v-if="scope.row.pushFlag==false" @click="changeStatus('certificate',scope.row.id)">认证</el-button>-->
                            <el-button type="text" v-if="scope.row.userType!='HZS' && scope.row.pushFlag==false" @click="changeStatus('certificate',scope.row.id)">认证</el-button>
                            <el-button type="text" v-if="scope.row.userStatus!='FREEZE'" @click="changeAccount('disable',scope.row.id)">禁用</el-button>
                            <el-button type="text" v-if="scope.row.userStatus=='FREEZE'" @click="changeAccount('enable',scope.row.id)">启用</el-button>
                            <!--<el-button type="text" @click="goDetail('edit',scope.row.id)">编辑</el-button>-->
                            <!--<el-button type="text" @click="delTablelist(scope.row.id)">删除</el-button>-->
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination class="pageNation" @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="1"
                               :page-sizes="[10,15,20,25,30]" :page-size="pSize" layout="total,sizes,prev,pager,next,jumper"
                               :total="pageData.totalElements" v-show="pageData.totalElements>pSize"></el-pagination>
            </div>
        </div>
        <!--客户form Page-->
        <div id="formPage" v-if="!showFlag">
            <!--<el-form ref="memberForm" :model="memberForm" :rules="memberRules" class="myformDetail" :class="{formBorder:viewFlag}">-->
            <el-form ref="memberForm" :rules="memberRules" class="myformDetail" :class="{formBorder:viewFlag}">
                <el-form-item label="编号：">
                    <el-input v-model="memberForm.id" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="昵称：">
                    <el-input v-model="memberForm.name" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="电话：">
                    <el-input v-model="memberForm.mobile" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="区域：" :inline="true" v-if="!viewFlag">
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
                <el-form-item label="区域：" v-if="viewFlag">
                    <el-input v-model="memberForm.userOrgClientVO.address" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="用户主身份：">
                    <el-input v-model="memberForm.userTypeKey" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="用户子身份：">
                    <el-input v-model="memberForm.userDetailTypeKey" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="企业名称：">
                    <el-input v-model="memberForm.userOrgClientVO.orgName" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="客户类型：">
                    <el-input v-model="memberForm.orgAccountTypeKey" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="账户类型：">
                    <el-input v-model="memberForm.userAccountTypeKey" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="等级：">
                    <el-input v-model="memberForm.userPoint.userLevelKey" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="积分：">
                    <el-input v-model="memberForm.userPoint.availablePoints" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="账户状态：">
                    <el-input v-model="memberForm.userStatusKey" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="主营品种：">
                    <el-input v-model="memberForm.mainOperating" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="实力描述：">
                    <el-input v-model="memberForm.remark" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="账户状态：">
                    <el-input v-model="memberForm.userRealAuthKey" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item class="btnGroup">
                    <el-button type="primary" @click="submitForm" v-if="editFlag">保存</el-button>
                    <el-button @click="backTablePage">返回</el-button>
                </el-form-item>
            </el-form>
        </div>
        <!--选择绑定的组织  window-->
        <el-dialog title="选择绑定的组织" :visible.sync="showOrgFlag">
            <!--搜索-->
            <div class="mysearch orgSearch">
                <el-form :inline="true" class="search">
                    <el-form-item label="企业名称：">
                        <el-input v-model="org_orgName" placeholder="请输入企业名称"></el-input>
                    </el-form-item>
                    <el-form-item label="库存量：">
                        <el-input v-model="org_orgStockCap" placeholder="请输入库存量"></el-input>
                    </el-form-item>
                    <el-button type="primary" @click="onSearch('org')">搜索</el-button>
                    <el-button @click="onReset('org')">重置</el-button>
                </el-form>
            </div>
            <!--列表-->
            <el-table :data="orgList" highlight-current-row border stripe style="width:100%;" @row-click="selsOrg" v-loading.body="orgloadFlag">
                <!--<el-table-column type="selection"></el-table-column>-->
                <el-table-column prop="orgName" label="企业名称"></el-table-column>
                <el-table-column prop="userAccountType" label="企业类型"></el-table-column>
                <el-table-column prop="mainOperating" label="主营品种"></el-table-column>
                <el-table-column prop="address" label="所在地区"></el-table-column>
                <el-table-column prop="orgStockCap" label="库存量"></el-table-column>
            </el-table>
            <el-pagination class="pageNation" @current-change="orgCurrentChange" :current-page="1"
            layout="total,prev,pager,next" :total="orgpageData.totalElements":page-size="orgpSize"
            v-show="orgpageData.totalElements > orgpSize"></el-pagination>
            <div slot="footer" class="dialog-footer">
                <el-button @click="closeOrgWin">取消</el-button>
                <el-button type="primary" @click.stop="submitOrg">提交</el-button>
            </div>
        </el-dialog>
        <!--设置账户类型  window-->
        <el-dialog title="设置账户类型" :visible.sync="showAccountFlag">
            <el-form :model="accountForm" class="accountForm">
                <el-form-item label="昵称：">
                    <el-input v-model="accountForm.name" :readonly="true"/>
                </el-form-item>
                <el-form-item label="电话：">
                    <el-input v-model="accountForm.mobile" :readonly="true"/>
                </el-form-item>
                <el-form-item label="身份：">
                    <el-input v-model="accountForm.identify" :readonly="true"/>
                </el-form-item>
                <el-form-item label="企业名称：">
                    <el-input v-model="accountForm.rgName" :readonly="true"/>
                </el-form-item>
                <el-form-item label="客户类型：">
                    <el-radio-group v-model="accountForm.chooseType" @change="selCustomnType">
                        <el-radio label="company">企业</el-radio>
                        <el-radio label="IND_MAIN_ACCOUNT">个体</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="账户类型：" v-if="showFirmflag" @change="selAccType">
                    <el-radio-group v-model="accountForm.accountType">
                        <el-radio label="ORG_MAIN_ACCOUNT">主账户</el-radio>
                        <el-radio label="ORG_CHILD_ACCOUNT">子账户</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="closeAccWin">取消</el-button>
                <el-button type="primary" @click.stop="submitAccount">提交</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
    import header from '~/components/header/header.vue'
    export default {
        data () {
            return {
                fatitle:'客户',
                title:'客户列表',
                showFlag:true, //true 显示table页; false 显示form页
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
                sear_userType:null,//用户身份
                sear_userAccountType:null,//用户账户类型
                //组织table '搜索'字段
                org_orgName:"",//企业名称
                org_orgStockCap:"",//库存量
                //客户列表数据
                dataList:[],
                userid:null,//客户id
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
                accountInfo:[
                    {name:"主账户",value:"ORG_MAIN_ACCOUNT"},
                    {name:"子账户",value:"ORG_CHILD_ACCOUNT"},
                    {name:"个体",value:"IND_MAIN_ACCOUNT"}
                ],//用户账号类型
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
                loadFlag:false,
                //组织列表‘转圈’加载
                orgloadFlag:false,
                //当市改变时候清空 区和镇
                clearRegion:false,
                //详情页
                dataDetailList:[],//详情页数据集
                operateType:"", //view：详情 edit:编辑
                memberForm:{
                    id:null,//编号
                    name:"",//客户姓名
                    mobile:"",//电话
                    cityId:"",//市
                    areaId:"",//区
                    townId:"",//街道
                    address:"",//区域
                    userTypeKey:"",//用户主身份
                    userDetailTypeKey:"",//用户子身份
                    userOrgClientVO:{
                        orgName:"", //企业名称
                        userAccountType:""//客户类型
                    },
                    mainOperating:"",//主营品种
                    strengthDescription:"",//实力描述
                    userType:"",//用户身份
                    userRealAuthKey:""//认证标识
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
                }
            }
        },
        activated(){
            var vm=this;
            vm.getTableList();//获取列表信息
            vm.getCityList();//获取市信息
            // test
            vm.getOrglist();//获取组织列表
        },
        created() {

        },
        mounted(){
            var vm=this;
            vm.initialPage();//初始化页面
        },
        deactivated(){
            var vm=this;
            vm.loadFlag=false;//避免超时跳到首页出现加载图标
            vm.orgloadFlag=false;
            console.log("客户页离开了。。。不送");
        },
        filters:{
            pushflagFilter(val){
                console.log();
                return val==true?"已认证":"未认证";
            }
        },
        methods: {
            //选择客户类型
            selCustomnType(val){
                var vm=this;
                console.log("val:"+val);
                if(val=='company'){
                    vm.showFirmflag=true;
                }
                else{
                    vm.showFirmflag=false;
                    vm.accountForm.accountType=vm.accountForm.chooseType;
                }
            },
            //选择账户类型
            selAccType(val){
                var vm=this;
                console.log("账户类型val:"+val);
                vm.accountForm.accountType=val;
                
            },

            //设置账户类型
            submitAccount(){
                var vm=this;
                vm.certFlag=false;
                console.log("账户类型："+vm.accountForm.accountType);
                vm.doUserorg();
            },
            //初始化 组织/账户 数据
            initdoUserorg(){
                var vm=this;
                vm.accountForm.chooseType=null;
                vm.accountForm.accountType=null;
                vm.setType=null;
                vm.setDetailType=null;
            },
            //绑定组织/账户
            bindProp(type){
              var vm=this;

              if(!vm.userid){
                  vm.$message({
                      message:"请选择客户列表数据！",
                      type:'warning'
                  });
              }
              else if(type=='org'){
                  vm.initdoUserorg();
                  vm.showOrgFlag=true;
              }
              else if(type=='account'){
                  vm.showAccountFlag=true;
              }
            },
            //组织window submit
            submitOrg(){
                var vm=this;
                vm.certFlag=false;
                vm.doUserorg();
            },
            //账户类型
            closeAccWin(){
                var vm=this;
                vm.showAccountFlag=false;
            },
            //组织window close
            closeOrgWin(){
                var vm=this;
                vm.showOrgFlag=false;
                if(vm.orgid){
                    vm.orgid=null;
                }
            },
            //获取组织列表
            getOrglist(){
                var vm=this;
                vm.orgloadFlag=true;
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
                vm.ax.post(params,vm.getOrglistCb);
            },
            getOrglistCb(data){
                var vm=this;
                vm.orgloadFlag=false;
                vm.orgpageData=data.data;
                vm.orgList=data.data.rows;

            },
            //认证状态改变
            changeStatus(type,id){
                var vm=this;
                vm.userid=id;
                console.log("type:"+type+"id:"+id);
                if(type=="certificate"){
                    vm.certFlag=true;
                }
                vm.doUserorg();
            },
            //推送组织和设置用户类型,认证开关
            doUserorg(){
                var vm=this;
                let params={
                    url:vm.apiUrl.member.UserAndOrgUrl,
                    data:{
                        id:vm.userid,
                        pushFlag:vm.certFlag, //推送时传送true
                        userAccountType:vm.accountForm.accountType,
                        userType:vm.setType,
                        userDetailType:vm.setDetailType,
                        userOrgId:vm.orgid
                    }
                };
                vm.ax.post(params,vm.doUserorgCb);
            },
            doUserorgCb(data){
                var vm=this;
                vm.$message({
                    message:"设置成功！",
                    type:'success'
                });
                vm.showOrgFlag=false;//关闭 组织 window
                vm.showAccountFlag=false;//关闭 账户 window
                //初始化数据
                vm.initdoUserorg();
                vm.getTableList();//获取列表信息
                


            },
            //批量删除
            batchDel(){
                var vm=this;
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
                var vm=this;
                vm.orgid=row.id;
                console.log("org行数据：组织id:"+vm.orgid);
            },
            //客户table行选择改变
            selUserinfo(row){
                var vm=this;
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
            //table行选择改变时触发
            selsChange(selection){
                var vm=this;
                vm.idsArray=[];
                vm.idsArray=selection.map(item => item.id);//获取所有选中行的id组成数组，以逗号分隔
                console.log("vm.idsArray:"+vm.idsArray);
            },
            //初始化页面
            initialPage(){
                var vm=this;
                vm.viewFlag=false;//表单只读标识
                vm.editFlag=false;//表单编辑标识
                vm.showFlag=true;//显示列表，隐藏表单
                vm.title='客户列表';
            },
            //批量删除table数据
            delMoretable(){
                var vm=this;
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
                var vm=this;
                vm.$message({
                    message:"批量删除成功！",
                    type:'success'
                });
                vm.$refs.memberTable.clearSelection();//取消选择
                vm.getTableList();//获取列表信息
            },
            //禁用/启用table单条信息  disable/enable
            changeAccount(type,id){
               var vm=this;
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
                var vm=this;
                vm.$message({
                    message:'账户状态修改成功！',
                    type:'success'
                });
                vm.getTableList();//获取列表信息
            },
            //'保存'编辑信息
            submitForm(){
                var vm=this;
                vm.editDetail();
            },
            //back to tablePage
            backTablePage(){
                var vm=this;
                vm.showFlag=true;
                vm.initialPage();//初始化页面标志
                vm.getTableList();//获取table数据
            },
            //编辑表单(save)
            editDetail(){
                var vm=this;
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
                var vm=this;
                vm.$message({
                    message:'保存成功！',
                    type:'success'
                });
            },
            //go formPage    operateType:"" //view：详情 viewFlag=true; edit:编辑 viewFlag=false
            goDetail(type,id){
                var vm=this;
                vm.operateType=type;
                if(type=='view'){
                    vm.viewFlag=true;
                    vm.editFlag=false;
                    vm.title='客户详情';
                }
                else if(type=='edit'){
                    vm.editFlag=true;
                    vm.viewFlag=false;
                    vm.title='客户编辑';
                }
                console.log("操作类型："+vm.operateType+"用户id:"+id);
                vm.getTableDetail(id);
                //接口改造，测试，正式去掉
//                vm.showFlag=false;//显示form 隐藏table

            },
            //table详情info
            getTableDetail(id){
                var vm=this;
                let params={
                    url:vm.apiUrl.member.tableDetailUrl,
                    data:{
                        id:id //用户id
                    }
                }
                vm.ax.post(params,vm.tableDetailCb);
            },
            tableDetailCb(data){
              var vm=this;
              vm.showFlag=false;//显示form 隐藏table
              vm.dataDetailList=data.data;
              if(vm.operateType=='edit'){
                  vm.getAreaList(data.data.cityId);//获取区列表
                  vm.getTownList(data.data.areaId);//获取镇列表
              }
                vm.memberForm=vm.dataDetailList;
                //如果数据为空，显示“无”
                for(var i in vm.memberForm){
                    if(!vm.memberForm[i]){
                        vm.memberForm[i]="无";
                    }
                }
              /* 没有需要数据转换的地方，如上直接赋值
              vm.memberForm.cityId=vm.dataDetailList.cityId;
              vm.memberForm.areaId=vm.dataDetailList.areaId;
              vm.memberForm.townId=vm.dataDetailList.townId;
              vm.memberForm.address=vm.memberForm.address;
              vm.memberForm.id=vm.dataDetailList.id;
              vm.memberForm.name=vm.dataDetailList.name;
              vm.memberForm.mobile=vm.dataDetailList.mobile;
              vm.memberForm.mainOperating=vm.dataDetailList.mainOperating;
              vm.memberForm.strengthDescription=vm.dataDetailList.strengthDescription;
              vm.memberForm.userType=vm.dataDetailList.userType;
              vm.memberForm.userRealAuthKey=vm.dataDetailList.userRealAuthKey;
              */
              setTimeout(function(){
                 vm.clearRegion=true;
              },1000);
            },
            //市改变
            changeCity(id,type){
                var vm=this;
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
            //区改变
            changeArea(id){
                var vm=this;
                if(vm.clearRegion){
                    vm.memberForm.townId="";
                }
                vm.getTownList(id);//镇
            },
            //搜索
            onSearch(type){
                var vm=this;
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
                var vm=this;
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
            //获取市信息
            getCityList(){
                var vm=this;
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
                var vm=this;
                vm.cityData=data.data;
                console.log("市长度："+data.data.length);
            },
            //获取区信息
            getAreaList(cityid){
                var vm=this;
                let params={
                    url:vm.apiUrl.member.regionUrl,
                    data:{
                        parentId:cityid
                    }
                }
                vm.ax.post(params,vm.areaListCb);
            },
            areaListCb(data){
                var vm=this;
                vm.areaData=data.data;
                console.log("区名："+vm.areaData[0].name)

            },
            //获取镇信息
            getTownList(areaid){
                var vm=this;
                let params={
                    url:vm.apiUrl.member.regionUrl,
                    data:{
                        parentId:areaid
                    }
                }
                vm.ax.post(params,vm.townListCb);
            },
            townListCb(data){
                var vm=this;
                vm.townData=data.data;

            },
            //获取表格数据
            getTableList(){
                var vm=this;
                vm.loadFlag=true;
                let params={
                    url:vm.apiUrl.member.tableUrl,
                    data:{
                        name:vm.sear_name,//客户姓名
                        mobile:vm.sear_mobile,//手机
                        userType:vm.sear_userType,//用户身份
                        userAccountType:vm.sear_userAccountType,//用户账户类型
                        //分页信息
                        pageNumber:vm.pNum,//页码
                        pageSize:vm.pSize //页容量
                    }
                };
                vm.ax.post(params,vm.tableListCb);
            },
            tableListCb(data){
                var vm=this;
                vm.loadFlag=false;
                vm.pageData=data.data;
                vm.dataList=data.data.rows;

            },
            //pageSize改变
            handleSizeChange(val){
                var vm=this;
                vm.pSize=val;
                vm.getTableList();
            },
            //客户页码改变
            handleCurrentChange(val){ //memeber 客户列表  org 组织
                var vm=this;
//                console.log("页码改变type："+type+" 页码："+val);
//                console.log(" 页码："+val);
                vm.pNum=val;
                vm.getTableList();
            },
            //组织页码改变
            orgCurrentChange(val){ //memeber 客户列表  org 组织
                var vm=this;
                vm.orgpNum=val;
                vm.getOrglist();
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
            'my-header':header
        }

    }
</script>
<style scoped>
    @import '../../assets/css/member.css';
</style>