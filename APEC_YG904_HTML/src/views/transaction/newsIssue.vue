/**
* Created by gsy on 2017/9/12.
* 行情发布
*/
<template>
    <div class="memberList">
        <my-header v-on:initialPage="initialPage" :firstTitle="firsTit" :secondTitle="secondTit" :thirdTitle="thirdTit" :breadFlag="breadflag"></my-header>
        <!--table Page-->
        <div id="tablePage" v-if="showFlag">
            <!--搜索-->
            <div class="mysearch">
                <el-form :inline="true" class="search" label-width="100px">
                    <el-form-item label="姓名：">
                        <el-input v-model="sear_author" placeholder="请输入姓名"></el-input>
                    </el-form-item>
                    <el-form-item label="创建时间：">
                        <el-date-picker type="date" placeholder="开始时间" format="yyyy-MM-dd" v-model="sear_beginDate"></el-date-picker>
                        <span>-</span>
                        <el-date-picker type="date" placeholder="结束时间" format="yyyy-MM-dd" v-model="sear_endDate"></el-date-picker>
                    </el-form-item>
                    <el-form-item label="审核类型">
                        <el-select v-model="sear_auditType" placeholder="请选择审核类型">
                          <el-option label="全部" value=""></el-option>
                          <el-option label="未审核" value="0"></el-option>
                          <el-option label="审核通过" value="Y"></el-option>
                          <el-option label="审核驳回" value="N"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="发布类型">
                        <el-select v-model="sear_issueType" placeholder="请选择发布类型">
                          <el-option label="全部" value=""></el-option>
                          <el-option label="个人" value="Y"></el-option>
                          <el-option label="平台" value="N"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-button type="primary" @click="onSearch">搜索</el-button>
                    <el-button @click="onReset">重置</el-button>
                </el-form>
            </div>
            <!--批量删除-->
            <div class="batchGroup">
                <el-button type="primary" @click="goDetail('','add')">发布行情</el-button>
                <!--暂不需要-->
                <!--<el-button type="primary" @click="batchDel">批量删除</el-button>-->
            </div>
            <!--表格列表-->
            <div class="tableList">
                <el-table :data="dataList" ref="memberTable" border stripe v-loading.body="loadCircle" header-row-class-name="headerRow"  style="width:100%;" @selection-change="selsChange">
                    <el-table-column type="selection" width="35" fixed="left"></el-table-column>
                    <!-- <el-table-column prop="ordinal" label="序号"></el-table-column> -->
                    <el-table-column prop="title" label="标题"></el-table-column>
                    <el-table-column prop="createDate" label="创建时间">
                        <template slot-scope="scope">
                            <span>{{scope.row.createDate|timeFilter}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="auditState" label="审核状态">
                        <template slot-scope="scope">
                          <span>{{scope.row.auditState|auditFilter}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="priv" label="置顶状态">
                        <template slot-scope="scope">
                          <span>{{scope.row.priv|privFilter}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="personPub" label="发布类型">
                        <template slot-scope="scope">
                          <span>{{scope.row.personPub|issueTypeFlilter}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="viewCount" label="阅读数"></el-table-column>
                    <el-table-column prop="likeCount" label="点赞数"></el-table-column>
                    <el-table-column prop="replyCount" label="评论数"></el-table-column>
                    <el-table-column prop="transCount" label="转发数"></el-table-column>
                    <el-table-column prop="mobile" label="手机号"></el-table-column>
                    <el-table-column prop="author" label="上传人">
						<template slot-scope="scope">
	            			<a style="text-decoration: underline; cursor: pointer;color: #20A0FF;" @click="goto(scope.row, 'newsIssue')">{{scope.row.author}}</a>
						</template>
                    </el-table-column>
                    <el-table-column label="操作" fixed="right" width="250">
                        <template slot-scope="scope">
                            <el-button type="text" @click="goDetail(scope.row, 'view')">详情</el-button>
                            <el-button type="text" v-if="scope.row.auditState != 'Y' && scope.row.auditState != 'N' " @click="goDetail(scope.row, 'edit')">编辑</el-button>
                            <el-button type="text" @click="delTablelist(scope.row.id)">删除</el-button>
                            <el-button type="text" @click="setTop(scope.row.id)" v-if="!scope.row.priv">置顶</el-button>
                            <el-button type="text" @click="cancelTop(scope.row.id)"  v-if="scope.row.priv">取消置顶</el-button>
                            <!-- <el-button type="text" @click="setTop(scope.row.id)" v-if="scope.row.url">置顶</el-button>  -->
                            <el-button type="text" @click="newsAudit(scope.row.id,'Y')" v-if="scope.row.auditState != 'Y' && scope.row.auditState != 'N' ">审核通过</el-button>
                            <el-button type="text" @click="newsAudit(scope.row.id,'N')" v-if="scope.row.auditState != 'Y' && scope.row.auditState != 'N' ">审核驳回</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination class="pageNation" @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pNum"
                               :page-sizes="[10,15,20,25,30]" :page-size="pSize" layout="total,sizes,prev,pager,next,jumper"
                               :total="pageData.totalElements" v-show="pageData.totalElements>10"></el-pagination>
            </div>
        </div>
        <!--form Page-->
        <el-row id="formPage" v-if="!showFlag">
        	<div class="detail-wrap clearfix">
	        	<el-col :span="14">
		            <el-form ref="newsForm" :model="newsForm" :rules="newsRules" class="myformDetail" style="width: 100%;" :class="{formBorder:viewFlag}">
		                <el-form-item label="标题：" prop="title">
		                    <el-input v-model="newsForm.title" :disabled="viewFlag" :autosize="{minRows:1}" type="textarea" ></el-input>
		                </el-form-item>
		                <el-form-item label="发布时间：" v-if="!addFlag">
		                    <el-input v-model="newsForm.createDate" :disabled="!addFlag"></el-input>
		                </el-form-item>
		                <el-form-item label="发布人：" prop="author">
		                    <el-input v-model="newsForm.author" :disabled="viewFlag"></el-input>
		                </el-form-item>
		                <el-form-item label="上传图片：" prop="url">
		                <!--aliyun方式上传图片-->
		                <el-upload class="avatar-uploader" :headers="{'token':token}" :data="{}"  v-if="addFlag || editFlag" :http-request="submitImg" :action="noUse" :show-file-list="false"  :before-upload="beforeLoad">
		                    <img v-if="imageUrl" :src="imageUrl+'?x-oss-process=style/_list'" alt="上传图片">
		                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
		                </el-upload>
		
		                <!--ftp方式上传图片
		                    <el-upload class="avatar-uploader" :headers="{'token':token}" :data="{'whetherCompression':'0'}"  v-if="addFlag || editFlag" :action="uploadApi" :show-file-list="false" :on-success="upLoadImg" :before-upload="beforeLoad">
		                    <img v-if="imageUrl" :src="imageUrl" class="avatar" alt="上传图片">
		                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
		                </el-upload> -->
		                <div>
		                    <img :src="newsForm.url+'?x-oss-process=style/_list'" v-if="newsForm.url && viewFlag">
		                    <span v-if="!newsForm.url && viewFlag">无</span>
		                </div>
		            </el-form-item>
		                <el-form-item label="新闻内容：" prop="content">
		                    <el-input  class="textareaStyle" :autosize="{minRows:10}" type="textarea" style="padding-top:0px;" v-model="newsForm.content" :disabled="viewFlag"></el-input>
		                </el-form-item>
		                <el-form-item class="btnGroup">
		                    <el-button type="primary" @click="submitForm('newsForm')" v-if="editFlag || addFlag">提交</el-button>
		                </el-form-item>
		            </el-form>
	        	</el-col>
	        	<el-col :span="10" style="border-left: solid 1px #ccc;" v-if="!addFlag">
	        		<div class="news-title">
	        			<span>共{{dataDetailList.replyCount}}评论</span>
	        			<span>点赞{{dataDetailList.likeCount}}</span>
	        			<span>阅读{{dataDetailList.viewCount}}</span>
	        		</div>
	        		<el-row>
	        			<el-col style="padding: 10px;" v-for="(comment, index) in commentList">
	        				<el-row>
	        					<div class="fl">
	        						<img :src="comment.imgUrl+'?x-oss-process=style/_head'" v-if="comment.imgUrl" style="height: 35px;width: 35px;">
	        						<img src="./../../assets/images/DBKS.png" v-else style="height: 35px;width: 35px;">
	        					</div>
	        					<div class="fl" style="padding-left: 10px;">
	        						<p style="color: #409eff;">{{comment.name}}</p>
	        						<p>{{moment(comment.createDate).format('YYYY-MM-DD')}}</p>
	        					</div>
	        				</el-row>
	        				<el-row style="line-height: 35px;">
	        					{{comment.content}}
	        					<el-button type="text" class="fr" @click="delMonment(comment)">删除</el-button>
	        					<el-button type="text" v-if="!comment.formSystem" class="fr" @click="goReply(comment, index)">回复</el-button>
	        					<el-row v-show="reply['show'+index]" class="clearfix">
	        						<el-col :span="20">
	        							<el-input v-model="reply.content"></el-input>
	        						</el-col>
	        						<el-col :span="3" style="margin: -5px 0 0 5px;">
	        							<el-button @click="replyConfirm">确认</el-button>
	        						</el-col>
	        					</el-row>
	        				</el-row>
	        				<el-row v-for="(item, index2) in comment.societyLzlReplyViewVOS">
	        					<span style="color: #0b55a0;">{{item.name}}@{{item.toUserName}}：</span>{{item.content}}
	        					<el-button type="text" class="fr" @click="delSubMonment(item)">删除</el-button>
	        					<el-button type="text" v-if="!item.formSystem" class="fr" @click="goReply(item, index, index2)">回复</el-button>
	        					<el-row v-show="reply['show'+index+index2]" class="clearfix">
	        						<el-col :span="20">
	        							<el-input v-model="reply.content"></el-input>
	        						</el-col>
	        						<el-col :span="3" style="margin: -5px 0 0 5px;">
	        							<el-button @click="replyConfirm">确认</el-button>
	        						</el-col>
	        					</el-row>
	        				</el-row>
	        			</el-col>
	        		</el-row>
					<div class="pagination-wrapper" style="text-align: right;" v-if="commentList.length">
						<el-pagination layout="jumper, sizes, prev, pager, next, total" :page-count="pageCount2" :currentPage="currentNo2" :page-sizes="[10, 20, 30, 40]" :page-size="pageSize2" :total="total2" @size-change="sizeChange2" @current-change="getComments"></el-pagination>
					</div>
					<div v-else style="text-align: center; font-size: 16px; line-height: 50px;">
						<h1>暂无用户评价信息</h1>
					</div>
	        	</el-col>
	        </div>
	    	<div class="text-center">
	            <el-button @click="backTablePage">返回</el-button>
	    	</div>
        </el-row>
    </div>
</template>

<script>
    import header from '~/components/header/header.vue'
    import encrypt from '~/assets/js/oss/encrypt'
    
    function Reply(){
    	this.toReplyId = '';
    	this.formSystem = true;
    	this.content = '';
    	//删除项
    	this.show = false;
    }

    export default {
        data () {
            return {
                //breadcrumb
                firsTit:'交易',
                secondTit:'行情发布',
                thirdTit:'',
                breadflag:false,
                showFlag:true, //true 显示table页; false 显示form页
                viewFlag:false,//表单只读标识
                editFlag:false,//表单编辑标识
                addFlag:false,//表单新增标识
                //‘搜索’字段
                sear_beginDate:"",//开始时间
                sear_endDate:"",//结束时间
                sear_author:"",//客户姓名
                sear_auditType:"",//审核类型
                sear_issueType:"",//发布类型
                //列表数据
                dataList:[],
                //header token
                token:this.$store.state.authToken || this.commonjs.getValue("authToken"),
                //批量删除ids
                idsArray:[],
                //分页信息
                pageData:[],//分页数据
                pNum:1,//页码
                pSize:10,//页容量
                //‘转圈’加载
                loadCircle:false,
                //回复
                reply: new Reply(),
                //详情页
                dataDetailList:[],//详情页数据集
                operateType:"", //view：详情 edit:编辑
                imageUrl:"",//上传图片地址
                authoriseApi:this.apiUrl.transaction.authoriseUrl,//上传图片接口
//                aliyunApi:this.apiUrl.transaction.uploadImg,//阿里云
                noUse:"",//因为ele-upload,action必传参数
                hasImage:true,//是否上传图片
                newsForm:{
                    id:null,
                    title:"",//行情标题
                    content:"",//行情内容
                    author:"",//作者
                    url:"",//行情图片链接
                    createDate:""//发布时间（由系统自动生成）

                },
                newsRules:{//暂时无须校验字段,暂放
                    title:[
                        {required:true,message:"请输入标题",trigger:"blur"}
                    ],//客户姓名
                    author:[
                        {required:true,message:"请输入发布人",trigger:"blur"}
                    ],//发布人
                    url:[
//                        {required:true,message:"请上传图片",trigger:"blur"}
                    ],//上传图片
                    content:[
                        {required:true,message:"请输入新闻内容",trigger:"blur"}
                    ]//新闻内容

                },
//				分页容器
				currentNo2: 1,
				pageCount2: 1,
				pageSize2: 10,
				total2: 0,
				
				societyPostId: '',//用于详情的评论
				commentList: [],
				
            }
        },
        activated(){
            var vm=this;
            encrypt.getAuthorise();
//            console.log("阿里云地址："+vm.apiUrl.common.alyserverUrl);
            vm.initialPage();//初始化页面
			setTimeout(function(){
				vm.checkFromWhistleBlowing(vm,'goDetail','id','view');//检查是否从交收单过来
			},1)
            //获取图片上传授权信息
        },
        methods: {
//			每页数据条数变化
			sizeChange2(size) {
				let vm = this;
				vm.getComments(vm.currentNo2, size);
			},
            //设置行情置顶
            setTop(id){
              var vm=this;
              vm.$confirm('确认置顶当前选中行情吗？','提示',{
                confirmButtonText:'确定',
                cancelButtonText:'取消',
                type:'warning'
              }).then(() => {
                let params={
                  url:vm.apiUrl.transaction.setTopUrl,
                  data:{
                    id:id,
                    isTop:true
                  }
                };
                vm.ax.post(params,vm.setTopCb);
              }).catch(() => {

              });

            },
            setTopCb(data){
              var vm=this;
              vm.$message({
                message:"置顶设置成功！",
                type:"success"
              });
              vm.getTableList();//获取列表信息
            },
            //取消置顶
            cancelTop(id){
              var vm=this;
              vm.$confirm('确认取消置顶当前选中的行情吗？','提示',{
                confirmButtonText:'确定',
                cancelButtonText:'取消',
                type:'warning'
              }).then(() => {
                let params={
                  url:vm.apiUrl.transaction.setTopUrl,
                  data:{
                    id:id,
                    "isTop":false
                  }
                };
                vm.ax.post(params,vm.cancelTopCb);
              }).catch(() => {

              });

            },
            cancelTopCb(data){
              var vm=this;
              vm.$message({
                message:"取消置顶成功！",
                type:"success"
              });
              vm.getTableList();//获取列表信息
            },
            //行情审核
            newsAudit(id,type){
              var vm=this;
              var msg="";
              if(type =='Y'){
                msg="确定审核通过当前选中的行情吗？";
              }
              else if(type == 'N'){
                msg="确定审核驳回当前选中的行情吗？";

              }
              vm.$confirm(msg,'提示',{
                confirmButtonText:'确定',
                cancelButtonText:'取消',
                type:'warning'

              }).then(() => {
                let params={
                  url:vm.apiUrl.transaction.auditUrl,
                  data:{
                    id:id,
                    auditState:type
                  }
                };
                vm.ax.post(params,vm.newsAuditCb);
              }).catch(() => {

              })

            },
            newsAuditCb(data){
              var vm=this
              vm.$message({
                message:"审核操作已完成！",
                type:'success'
              });
              vm.getTableList();//获取列表信息

            },
            //上传图片(ftp)
            /*  upLoadImg(res,file){
                var vm=this;
                vm.imageUrl=URL.createObjectURL(file.raw);
                if(res.succeed){
                    //将上传后返回的img地址赋值给newsForm.imageUrl
                    var backimgUrl=res.data[0][0].imagePath;
                    vm.newsForm.url=backimgUrl;
                    console.log("图片地址："+res.data.name);

                }
                else{
                    vm.$message.error(res.errorMsg);
                }
            },*/
            //校验图片格式
            beforeLoad(){
                var vm=this;
                if(!vm.validateImg) {//图片格式校验通过
                    return false;
                }
            },
            //上传图片aly
            submitImg(data){
                var vm = this;
                 console.log("submitImg:"+data);
                //gsy
                return encrypt.ossUpload(data.file,function(result){
                 console.log(result,999999);
                 vm.$message.success('上传图片成功!');
                 vm.newsForm.url=vm.apiUrl.common.alyserverUrl+result.name;
                 vm.imageUrl=vm.apiUrl.common.alyserverUrl+result.name;
                 return true;
                 },function(err){
                 console.log("upload image failed by oss,"+ err);
                 vm.$message.error('上传图片失败!');
                 return false;
                 });
                //上传图片yd
                /*return encrypt.ossUpload(vm.storeAs,data.file,vm.accessKeyId,vm.accessKeySecret,vm.securityToken,function(result){
                    console.log(result);
                    vm.$message.success('上传图片成功!');
                    vm.newsForm.url=vm.apiUrl.common.alyserverUrl+result.name;
                    vm.imageUrl=vm.apiUrl.common.alyserverUrl+result.name;
                    return true;
                },function(err){
                     console.log("upload image  failed by oss,"+ err);
                     vm.$message.success('上传图片失败!');
                    return false;
                })*/
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
            //数据格式转化
           /* vm.commonJs.getDataFomat(vm.newsForm,data.data);*/
            getDataFomat(obj1,obj2){
                var vm=this;
                for(var i in obj1){
                    for(var j in obj2){
                        if(i==j){
                            if(i=="createDate"){
                                obj1[i]=vm.commonJs.timestampPattern(obj2[i]);
                            }
                            else{
                                obj1[i]=obj2[j];
                            }
                        }

                    }
                }
                return obj1;
            },
            //新增行情
            addNews(){
                var vm=this;
                vm.hasImage=vm.newsForm.url?true:false;
                let params={
                    url:vm.apiUrl.transaction.addNewsUrl,
                    data:{
                        "realm": "ARTICLE",   //固参
                        "hasImage":vm.hasImage,//固参
                        "author":vm.newsForm.author,//作者
                        "content":vm.newsForm.content,//行情内容
                        "title":vm.newsForm.title,//行情标题
                        "url":vm.newsForm.url,//行情图片链接
                        "personPub":false
                    }
                }
                vm.ax.post(params,vm.addNewsCb);
            },
            addNewsCb(data){
                var vm=this;
                vm.$message({
                    message:'发布成功！',
                    type:'success'
                });
                //重置表单
                vm.resetForm();
                vm.initialPage();


            },
            //重置表单
            resetForm(){
                var vm=this;
                for(var i in vm.newsForm){
                    vm.newsForm[i]="";
                }
                vm.imageUrl="";
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
                vm.getTableList();//获取列表信息
                vm.breadflag=false;//隐藏最后一级
                vm.showFlag=true;//显示列表，隐藏表单

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
            //删除table单条信息
            delTablelist(id){
                var vm=this;
                //确认删除框
                vm.$confirm('确认删除选中的数据吗？','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(()=>{
                    //删除方法
                    let params={
                        url:vm.apiUrl.transaction.delNewsUrl,
                        data:{
                            id:id //用户id
                        }
                    };
                vm.ax.post(params,vm.delTableCb);
            }).catch(()=>{
                    console.log("已取消删除");
            });


            },
            delTableCb(data){
                var vm=this;
                vm.$message({
                    message:'删除成功！',
                    type:'success'
                });
                vm.getTableList();//获取列表信息
            },
            //'保存'编辑信息
            submitForm(formname){
                var vm=this;
                console.log("editFlag:"+vm.editFlag+" addFlag:"+vm.addFlag);
                vm.$refs[formname].validate((valid) => {
                    if(valid){
                        if(vm.editFlag){
                            vm.editDetail();
                            console.log("-------------invoke 编辑-----------");
                        }
                        else{
                            vm.addNews();
                            console.log("-------------invoke 新增-----------")
                        }
                    }
                    else{
                        console.log("表单校验未通过");
                        return false;
                    }
                });

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
                console.log("编辑提交："+vm.newsForm.name);
                let params={
                    url:vm.apiUrl.transaction.editNewsUrl,
                    data:{
                        id:vm.newsForm.id,
                        "hasImage": "true",//固参
                        "author":vm.newsForm.author,//作者
                        "content":vm.newsForm.content,//行情内容
                        "title":vm.newsForm.title,//行情标题
                        "url":vm.newsForm.url//行情图片链接
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

             //重置表单
                vm.resetForm();
                vm.initialPage();

            },
            //go formPage    operateType:"" //view：详情 viewFlag; edit:编辑 viewFlag; add:新增 addFlag;
            goDetail(row, type){
                var vm=this;
                vm.operateType=type;
                if(type=='view'){
                    vm.viewFlag=true;
                    vm.editFlag=false;
                    vm.addFlag=false;
                    vm.thirdTit='详情';
                    vm.breadflag=true;
                    vm.getTableDetail(row.id);
                    vm.societyPostId = row.id;//将id赋值
                    vm.getComments();
                }
                else if(type=='edit'){
                    vm.editFlag=true;
                    vm.viewFlag=false;
                    vm.addFlag=false;
                    vm.thirdTit='编辑';
                    vm.breadflag=true;
                    vm.getTableDetail(row.id);
                }
                else{//新增
                    vm.addFlag=true;
                    vm.editFlag=false;
                    vm.viewFlag=false;
                    vm.thirdTit='新增';
                    vm.breadflag=true;
                    //重置表单
                    vm.resetForm();
                    vm.showFlag=false;//显示form 隐藏table
                }
                console.log("操作类型："+vm.operateType+"用户id:"+row.id);
                //接口改造，测试，正式去掉
                //vm.showFlag=false;//显示form 隐藏table

            },
            //获取评论列表
            getComments(currentNo, size) {
				let vm = this;
				if (size) {
					vm.pageSize2 = size;
				}
				vm.currentNo2 = currentNo;
                let params={
                    url:vm.apiUrl.transaction.moments,
                    data:{
                        pageNumber: vm.currentNo2,
                        pageSize: vm.pageSize2,
                        societyPostId: vm.societyPostId
                    }
                }
                let callback = function(data){
					vm.currentNo2 = data.data.currentNo;
					vm.total2 = data.data.totalElements;
					vm.commentList = data.data.rows;
                }
                vm.ax.post(params,callback);
            },
            //回复
            goReply(row, index, index2){
            	let vm = this;
            	vm.reply = new Reply();
            	index = JSON.stringify(index);
            	index2 = JSON.stringify(index2);
            	if(index2){
            		vm.reply['show'+index+index2] = true;
            		console.log('show'+index+index2)
            		console.log(index)
            		console.log(index2)
            	}else{
            		vm.reply['show'+index] = true;
            		console.log('show'+index)
            	}
            	vm.reply.toReplyId = row.id;
            },
            //回复确认
            replyConfirm(){
                var vm=this;
                let params={
                    url:vm.apiUrl.transaction.reply,
                    data: {
                    	content: vm.reply.content,
                    	formSystem: vm.reply.formSystem,
                    	toReplyId: vm.reply.toReplyId
                    }
                }
                let callback = function(data){
                	vm.$message.success("回复成功");
                	vm.reply = new Reply();
                	vm.getComments();
                }
                if(vm.reply.content){
                	vm.ax.post(params, callback);
                }else{
                	vm.reply = new Reply();
                }
            },
            //删除评论
            delMonment(row){
                var vm=this;
                let params={
                    url:vm.apiUrl.transaction.delMonment,
                    data:{
                        id: row.id//用户id
                    }
                }
                let callback = function(data){
                	vm.$message.success("删除成功");
                	vm.getComments();
                }
                vm.ax.post(params, callback);
            },
            //删除楼中评论
            delSubMonment(row){
                var vm=this;
                let params={
                    url:vm.apiUrl.transaction.delSubMonment,
                    data:{
                        id: row.id//用户id
                    }
                }
                let callback = function(data){
                	vm.$message.success("删除成功");
                	vm.getComments();
                }
                vm.ax.post(params, callback);
            },
            //table详情info
            getTableDetail(id){
                var vm=this;
                let params={
                    url:vm.apiUrl.transaction.tableDetailUrl,
                    data:{
                        id:id //用户id
                    }
                }
                vm.ax.post(params,vm.tableDetailCb);
            },
            tableDetailCb(data){
            	let vm = this;
            	if(!data.data){
            		vm.$message.error("数据不存在");
            	}else{
	                vm.showFlag=false;//显示form 隐藏table
	                vm.dataDetailList=data.data;
	//                vm.newsForm=vm.dataDetailList; //暂时注释
	                if(vm.editFlag || vm.viewFlag){
	                    vm.imageUrl=data.data.url;
	                }
	               //将data.data的值赋给 vm.newsForm
	               vm.getDataFomat(vm.newsForm,data.data);
	                //如果数据为空，显示“无”
	                for(var i in vm.newsForm){
	                    if(!vm.newsForm[i] && i!='url'){
	                        vm.newsForm[i]="无";
	                    }
	                }
	            }

            },
            //搜索
            onSearch(){
                var vm=this;
                vm.getTableList();

            },
            //重置
            onReset(){
                var vm=this;
                var reg=/^sear_[a-z0-9]+/;
                for(var i in vm.$data){
                    if(reg.test(i)){
                        vm.$data[i]='';
                    }
                }
                vm.getTableList();
            },
            //获取表格数据
            getTableList(pNum){
            	var vm=this;
            	console.log(vm.pNum)
            	if(pNum){
            		vm.pNum = pNum;
            	}
                vm.loadCircle=true;
                vm.sear_beginDate=vm.sear_beginDate?vm.commonJs.timePattern(new Date(vm.sear_beginDate),'yyyy-MM-dd'):"";
                vm.sear_endDate=vm.sear_endDate?vm.commonJs.timePattern(new Date(vm.sear_endDate),'yyyy-MM-dd'):"";
                let params={
                    url:vm.apiUrl.transaction.tableUrl,
                    data:{
                        //查询参数
                        author:vm.sear_author,
                        beginDate:vm.sear_beginDate,
                        endDate:vm.sear_endDate,
                        auditState:vm.sear_auditType,//审核类型
                        personPub:vm.sear_issueType,//发布类型
                        //固参
                        realm:"ARTICLE",
                        //分页信息
                        pageNumber:vm.pNum,//页码
                        pageSize:vm.pSize //页容量
                    }
                };
                vm.ax.post(params,vm.tableListCb,vm);
            },
            tableListCb(data){
                var vm=this;
                vm.loadCircle=false;
                vm.pageData=data.data;
                vm.dataList=data.data.rows;
            },
            //pageSize改变
            handleSizeChange(val){
                var vm=this;
                vm.pSize=val;
                vm.getTableList();
            },
            //页码改变
            handleCurrentChange(val){
                var vm=this;
                vm.pNum=val;
                vm.getTableList();
            }
        },
        filters:{
          //置顶状态
          privFilter(val){
            return val?"已置顶":"未置顶";
          },
          //审核类型
          auditFilter(val){
            switch(val){
              case "0":
              return "未审核";
              break;
              case "Y":
              return "审核通过";
              break;
              case "N":
              return "审核驳回";
              break;
            }
          },
          //发布类型
          issueTypeFlilter(val){
              return val?"个人发布":"平台发布";
          }
            //暂时不用
           /* addrFilter(val){
                return val.replace(/"([^|]*)"/g,'');
            }*/
        },
        components:{
            'my-header':header
        }

    }
</script>
<style scoped>
.news-title>span {
	display: inline-block;
	margin: 10px;
}
</style>
