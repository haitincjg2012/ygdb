<template>
    <div class="memberList">
        <my-header v-on:initialPage="initialPage" :firstTitle="firsTit" :secondTitle="secondTit" :thirdTitle="thirdTit" :breadFlag="breadflag"></my-header>
        <!--客户form Page-->
        <el-row id="formPage" style="width: 1000px;">
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
	            </el-form>
	        </el-col>
	        <!--企业信息-->
        	<el-col :span="12">
        		<h1 style="font-weight: bold; font-size: 16px; margin-top: 20px;">企业信息</h1>
	            <el-form ref="memberForm" :rules="memberRules" class="myformDetail memeberform" >
	            	<span v-for="item in create.userArray">
	            		<el-form-item :label="item.menuName" v-if="viewFlag">
		                    <el-input v-model="memberForm.userOrgClientVO[item.nameStr]" :disabled="true"></el-input>
		                </el-form-item>
	            	</span>
	            </el-form>
            </el-col>
            <el-col class="text-center" :span="24">
                <el-button @click="backTablePage">返回</el-button>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    import header from '~/components/header/header.vue'
    
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
                viewFlag:true,//表单只读标识
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
            }
        },
        activated(){
            vm = this;
            let uploaderId;
            if(!vm.$route.params.id) {
            	uploaderId = sessionStorage.getItem("uploaderId");
            } else {
            	sessionStorage.setItem("uploaderId", vm.$route.params.id);
            	sessionStorage.setItem("uploaderRouter", vm.$route.params.router);
            	uploaderId = vm.$route.params.id;
            }
        	vm.goDetail(uploaderId);
        },
        methods: {
        	//用户身份改变
        	userTypeChange(type, userType, call){
        		console.log(vm.create)
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

            //初始化页面
            initialPage(){
                vm.viewFlag=false;//表单只读标识
                vm.breadflag=false;
                vm.showFlag=true;//显示列表，隐藏表单
				vm.isDetail = false;
            },
            //back to tablePage
            backTablePage(){
                vm.$router.push(sessionStorage.getItem("uploaderRouter"));
            },
            //go formPage    operateType:"" //view：详情 viewFlag=true; edit:编辑 viewFlag=false
            goDetail(id){
                vm.breadflag=true;
                vm.viewFlag=true;
                vm.thirdTit='详情';
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
              	console.log(vm.create.userArray)
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
            }
        },
        components:{
            'my-header':header
        }

    }
</script>
<style scoped>
    @import '../../../assets/css/member.css';
</style>
