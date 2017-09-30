/**
* Created by wq on 2017/09/21.
* 企业管理
*/
<template>
    <div class="certificate">
        <my-header v-on:initialPage="initialPage" :firstTitle="firsTit" :secondTitle="secondTit" :thirdTitle="thirdTit" :breadFlag="breadflag"></my-header>
        <!--搜索-->
        <div class="mysearch" v-if="showFlag">
            <el-form :inline="true" class="search">
                <el-form-item label="企业名称：">
                    <el-input v-model="name" placeholder="请输入企业名称"></el-input>
                </el-form-item>
                <el-form-item label="企业类型：">
                    <el-select v-model="sear_companyType" placeholder="请选择企业类型" @change="getTableList">
                        <el-option label="组织账号" value="ORG_ACCOUNT"></el-option>
                        <el-option label="个体账户" value="IND_ACCOUNT"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="区域：">
                    <el-input v-model="sear_address" placeholder="请输入区域"></el-input>
                </el-form-item>
                <el-form-item label="主营品种：">
                    <el-input v-model="sear_mainOperating" placeholder="请输入主营品种"></el-input>
                </el-form-item>
                <el-button type="primary" @click="onSearch">搜索</el-button>
                <el-button @click="onReset">重置</el-button>
            </el-form>
        </div>
        <!--表格列表-->
        <div class="tableList" v-if="showFlag" style="margin-top: 20px;">
            <el-table :data="dataList" border stripe v-loading.body="loadCircle" style="width:100%;">
                <el-table-column prop="id" label="编号"></el-table-column>
                <el-table-column prop="orgName" label="企业名称"></el-table-column>
                <el-table-column prop="userAccountTypeKey" label="企业类型"></el-table-column>
                <el-table-column prop="address" label="区域"></el-table-column>
                <el-table-column prop="addressDetail" label="详细地址"></el-table-column>
                <el-table-column prop="saleAddress" label="客户市场"></el-table-column>
                <el-table-column prop="mainOperating" label="主营品种"></el-table-column>	
                <el-table-column prop="orgClientUsers" label="拥有的用户"></el-table-column>
                <el-table-column prop="userTagsVOS" label="标签" width="170">
	                	<template scope="scope">
	                		  <button v-for="item in scope.row.userTagsVOS" class="tag">{{item.tagName}}</button>
	                	</template>
                </el-table-column>
                <el-table-column prop="remark" label="实力描述"></el-table-column>
                <el-table-column prop="createDate" label="创建时间">
                		<template scope="scope">
	                		 	{{scope.row.createDate|timeFilter}}
	                	</template>
                </el-table-column>
                <el-table-column label="操作">
                    <template scope="scope">
                        <el-button type="text" @click="goDetail(scope.row.id)">详情</el-button>
                        <el-button type="text" @click="openTag(scope.row)">设置标签</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-dialog title="选择标签" :visible.sync="tagDialogVisible" class="template-dialog">
            		<div>
            			<button v-for="(item,index) in tagList" :class="[item.chosen ? 'tag' : 'tag-gry']" @click="toggleTag(index)">{{item.tagName}}</button>
            		</div>
							  <div slot="footer" class="dialog-footer">
							  		<el-button type="primary" @click="setTag()">确 定</el-button>
							    	<el-button @click="initialPage()">取 消</el-button>
							  </div>
						</el-dialog>
            <!--分页-->
            <el-pagination class="pageNation" @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="1"
            :page-sizes="[10,15,20,25,30]" :page-size="pSize" layout="total,sizes,prev,pager,next,jumper"
            :total="pageData.totalElements" v-show="pageData.totalElements>10"></el-pagination>
        </div>
        
        <!--企业form Page-->
        <div id="formPage" v-if="!showFlag">
            <el-form ref="companyForm" class="myformDetail" :class="{formBorder:viewFlag}">
                <el-form-item label="编号：">
                    <el-input v-model="companyForm.id" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="企业名称：">
                    <el-input v-model="companyForm.orgName" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="库存：">
                    <el-input v-model="companyForm.orgStockCap" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="区域：">
                    <el-input v-model="companyForm.address" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="详细地址：">
                    <el-input v-model="companyForm.addressDetail" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="客户市场：">
                    <el-input v-model="companyForm.saleAddress" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="主营品种：">
                    <el-input v-model="companyForm.mainOperating" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="拥有的用户：">
                    <el-input v-model="companyForm.orgClientUsers" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="标签：">
                    <span v-for="item in companyForm.userTagsVOS" class="tag">{{item.tagName}}</span>
                </el-form-item>
                <el-form-item label="实力描述：">
                    <el-input v-model="companyForm.remark" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="实力图片：" class="img-box">
                	<img v-for="item in companyForm.userOrgImageVOS" :src="item.imageUrl" class="org-img"/>
                </el-form-item>
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
                firsTit:'客户',
                secondTit:'企业管理',
                thirdTit:'',
                breadflag:false,
                /*header参数*/
                viewFlag:false,
//              editFlag:false,//表单编辑标识
//              addFlag:false,//表单新增标识
                showFlag:true, //true 显示table页; false 显示form页
                name:null,
                sear_companyType:null,
                sear_address:null,
                sear_mainOperating:null,
                loadCircle:false,//加载显示
                tagDialogVisible:false,//选择标签显示
                tagList:[
                	{id:null,tagName:'企业认证',chosen:false,className:'QYRZ'},
                	{id:null,tagName:'供应链金融合作库',chosen:false,className:'GYLJRHZK'}
                ],
                setTagId:'',
                dataList:[],
                pageData:[],//分页数据
                pSize:10 ,//页容量
                pNum:1, //页码
                companyForm:{},
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
                    url:vm.apiUrl.company.tableDetailUrl,
                    data:{
                        id:id 
                    }
                }
                vm.ax.post(params,vm.tableDetailCb);
        		},
        		tableDetailCb(data){
		            var vm=this;
		            vm.showFlag=false;//显示form 隐藏table
		            vm.companyForm.id=data.data.id;
		            vm.companyForm.orgName=data.data.orgName;
		            vm.companyForm.orgStockCap=data.data.orgStockCap;
		            vm.companyForm.address=data.data.address;
		            vm.companyForm.addressDetail=data.data.addressDetail;
		            vm.companyForm.saleAddress=data.data.saleAddress;
		            vm.companyForm.mainOperating=data.data.mainOperating;
		            vm.companyForm.orgClientUsers=data.data.orgClientUsers;
		            vm.companyForm.userTagsVOS=data.data.userTagsVOS;
		            vm.companyForm.remark=data.data.remark;
		            vm.companyForm.userOrgImageVOS=data.data.userOrgImageVOS;
            },
            //弹出标签选择框
            openTag(row){
            		var vm = this;
            		for(let i in row.userTagsVOS){
            			for(let j in vm.tagList){
            				if(row.userTagsVOS[i].tagName==vm.tagList[j].tagName){
            					vm.tagList[j].chosen = true;
            					vm.tagList[j].id = row.userTagsVOS[i].id;
            				}
            			}
            		}
            		vm.tagDialogVisible = true;
            		vm.setTagId = row.id;
            		
            },
            toggleTag(index){
            		var vm = this;
            		vm.tagList[index].chosen = vm.tagList[index].chosen?false:true;
            },
            setTag(){
            		var vm = this;
            		var userTagsVOS = [];
            		vm.tagList.forEach(function(e){
            			if(e.chosen){
            				userTagsVOS.push({tagName:e.tagName,className:e.className,id:e.id});
            			}
            		});
            		let params={
                    url:vm.apiUrl.company.setTagUrl,
                    data:{
                        id:vm.setTagId,
                        userTagsVOS:userTagsVOS
                    }
                }
                vm.ax.post(params,vm.setTagOk);
            },
            setTagOk(){
            		this.initialPage();
            		this.getTableList();
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
                vm.tagDialogVisible = false;
                vm.tagList.forEach(function(e){
                	e.chosen = false;
                });
                vm.showFlag=true;//显示列表，隐藏表单
                vm.breadflag=false;
                vm.title='企业管理';
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
                vm.loadCircle=true;
                let params={
                    url:vm.apiUrl.company.tableUrl,
                    data:{
                    		orgName:vm.name,
                    		userAccountType:vm.sear_companyType,
                    		address:vm.sear_address,
                    		mainOperating:vm.sear_mainOperating,
                        pageNumber:vm.pNum,//页码
                        pageSize:vm.pSize
                    }
                }
                vm.ax.post(params,vm.tableListCb,vm);
            },
            tableListCb(data){
                var vm=this;
                vm.pageData=data.data;
                console.log("页码:"+vm.pageData.totalElements);
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
                vm.sear_companyType=null;
                vm.sear_address=null;
                vm.sear_companyType=null;
                vm.sear_mainOperating=null;
                vm.getTableList();
            }
        },
        filters:{
            statusFilter(val){
                switch(val){
                    case "Y":
                        return "已审核";
                        break;
                    case "N":
                        return "已驳回";
                        break;
                    case "":
                        return "待审核";
                        break;
                }
            }
        },
        components: {
            'my-header':header
        }
    }
</script>

<style scoped>
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
    .org-img {
    	max-width: 300px;
    	max-height: 300px;
    	margin-right: 10px;
    	vertical-align: top;
    }
</style>
<style>
		.img-box .el-form-item__content {
			width: auto !important;
		}
		.template-dialog .el-dialog {
			width: 400px;
			top: 30% !important;
		}
</style>
