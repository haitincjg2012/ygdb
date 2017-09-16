/**
* Created by gsy on 2017/9/12.
* 行情发布
*/
<template>
    <div class="memberList">
        <my-header v-on:initialPage="initialPage" :fatherTitle="fatitle" :headTitle="title" :viewFlag="viewFlag" :editFlag="editFlag" :addFlag="addFlag"></my-header>
        <!--table Page-->
        <div id="tablePage" v-if="showFlag">
            <!--搜索-->
            <div class="mysearch">
                <el-form :inline="true" class="search">
                    <el-form-item label="姓名：">
                        <el-input v-model="sear_author" placeholder="请输入姓名"></el-input>
                    </el-form-item>
                    <el-form-item label="时间：">
                        <el-date-picker type="date" placeholder="开始时间" format="yyyy-MM-dd" v-model="sear_beginDate"></el-date-picker>
                        <span>-</span>
                        <el-date-picker type="date" placeholder="结束时间" format="yyyy-MM-dd" v-model="sear_endDate"></el-date-picker>
                    </el-form-item>
                    <el-button type="primary" @click="onSearch">搜索</el-button>
                    <el-button @click="onReset">重置</el-button>
                </el-form>
            </div>
            <!--批量删除-->
            <div class="batchGroup">
                <el-button type="default" @click="goDetail('add')">发布行情</el-button>
                <!--暂不需要-->
                <!--<el-button type="primary" @click="batchDel">批量删除</el-button>-->
            </div>
            <!--表格列表-->
            <div class="tableList">
                <el-table :data="dataList" ref="memberTable" border stripe v-loading.body="loadFlag" style="width:100%;" @selection-change="selsChange">
                    <el-table-column type="selection"></el-table-column>
                    <el-table-column prop="ordinal" label="序号"></el-table-column>
                    <el-table-column prop="title" label="标题"></el-table-column>
                    <el-table-column prop="createDate" label="时间">
                        <template scope="scope">
                            <span>{{scope.row.createDate|timeFilter}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="author" label="上传人"></el-table-column>
                    <el-table-column label="操作">
                        <template scope="scope">
                            <el-button type="text" @click="goDetail('view',scope.row.id)">详情</el-button>
                            <el-button type="text" @click="goDetail('edit',scope.row.id)">编辑</el-button>
                            <el-button type="text" @click="delTablelist(scope.row.id)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination class="pageNation" @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="1"
                               :page-sizes="[10,15,20,25,30]" :page-size="pSize" layout="total,sizes,prev,pager,next,jumper"
                               :total="pageData.totalElements" v-show="pageData.totalElements>10"></el-pagination>
            </div>
        </div>
        <!--form Page-->
        <div id="formPage" v-if="!showFlag">
            <el-form ref="newsForm" :model="newsForm" :rules="newsRules" class="myformDetail" :class="{formBorder:viewFlag}">
                <el-form-item label="标题：" prop="title">
                    <el-input v-model="newsForm.title" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="发布时间：" v-if="!addFlag">
                    <el-input v-model="newsForm.createDate" :disabled="!addFlag"></el-input>
                </el-form-item>
                <el-form-item label="发布人：" prop="author">
                    <el-input v-model="newsForm.author" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item label="上传图片：" prop="url">
                    <el-upload class="avatar-uploader" :headers="{'token':token}" :data="{'whetherCompression':'0'}"  v-if="addFlag || editFlag" :action="uploadApi" :show-file-list="false" :on-success="upLoadImg" :before-upload="validateImg">
                        <img v-if="imageUrl" :src="imageUrl" class="avatar" alt="上传图片">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                    <div>
                        <img :src="newsForm.url" class="avatar" v-if="newsForm.url && viewFlag">
                        <span v-if="!newsForm.url && viewFlag">无</span>
                    </div>
                </el-form-item>
                <el-form-item label="新闻内容：" prop="content">
                    <el-input style="width:800px;" :autosize="{minRows:10}" type="textarea" v-model="newsForm.content" :disabled="viewFlag"></el-input>
                </el-form-item>
                <el-form-item class="btnGroup">
                    <el-button type="primary" @click="submitForm('newsForm')" v-if="editFlag || addFlag">保存</el-button>
                    <el-button @click="backTablePage">返回</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    import header from '~/components/header/header.vue'
    export default {
        data () {
            return {
                fatitle:'交易',
                title:'行情发布',
                showFlag:true, //true 显示table页; false 显示form页
                viewFlag:false,//表单只读标识
                editFlag:false,//表单编辑标识
                addFlag:false,//表单新增标识
                //‘搜索’字段
                sear_beginDate:"",//开始时间
                sear_endDate:"",//结束时间
                sear_author:"",//客户姓名
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
                loadFlag:false,
                //详情页
                dataDetailList:[],//详情页数据集
                operateType:"", //view：详情 edit:编辑
                imageUrl:"",//上传图片地址
                uploadApi:this.apiUrl.transaction.uploadImgUrl,//上传图片接口
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
                        {required:true,message:"请上传图片",trigger:"blur"}
                    ],//上传图片
                    content:[
                        {required:true,message:"请输入新闻内容",trigger:"blur"}
                    ]//新闻内容

                }
            }
        },
        activated(){
            var vm=this;
            vm.getTableList();//获取列表信息
            //test texterea分段
//            vm.testTextarea();

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
        },
        methods: {
            //test
            testTextarea(){
                var str="      1、按规定抽取检验样茧，必须坚持隔包抽样，必要时逐包抽样。三分之一抽样茧包抽取上部，三分之一抽样茧包抽取中部，三分之一机样茧包抽取底部，抽样时须做到每包抽出样茧量均匀，每包抽取样茧200±20克。↵      1、按规定抽取检验样茧，必须坚持隔包抽样，必要时逐包抽样。三分之一抽样茧包抽取上部，三分之一抽样茧包抽取中部，三分之一机样茧包抽取底部，抽样时须做到每包抽出样茧量均匀，每包抽取样茧200±20克。↵      1、按规定抽取检验样茧，必须坚持隔包抽样，必要时逐包抽样。三分之一抽样茧包抽取上部，三分之一抽样茧包抽取中部，三分之一机样茧包抽取底部，抽样时须做到每包抽出样茧量均匀，每包抽取样茧200±20克。";
                str=str.split("\n");
                console.log("str:"+str);

            },
            //上传图片
            upLoadImg(res,file){
                var vm=this;
                vm.imageUrl=URL.createObjectURL(file.raw);
                if(res.succeed){
                    //将上传后返回的img地址赋值给newsForm.imageUrl
                    var backimgUrl=res.data[0][0].imagePath;
                    vm.newsForm.url=backimgUrl;
                    console.log("图片地址："+vm.newsForm.url);

                }
                else{
                    vm.$message.error(res.errorMsg);
                }
            },
            //图片格式校验
            validateImg(file){
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
                let params={
                    url:vm.apiUrl.transaction.addNewsUrl,
                    data:{
                        "category": "NEWS",   //固参
                        "channelCode": "NEWS",//固参
                        "hasImage": "true",//固参
                        "author":vm.newsForm.author,//作者
                        "content":vm.newsForm.content,//行情内容
                        "title":vm.newsForm.title,//行情标题
                        "url":vm.newsForm.url//行情图片链接
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
                vm.viewFlag=false;//表单只读标识
                vm.editFlag=false;//表单编辑标识
                vm.showFlag=true;//显示列表，隐藏表单
                vm.title='行情发布';
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
                        "category": "NEWS",   //固参
                        "channelCode": "NEWS",//固参
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
            },
            //go formPage    operateType:"" //view：详情 viewFlag; edit:编辑 viewFlag; add:新增 addFlag;
            goDetail(type,id){
                var vm=this;
                vm.operateType=type;
                if(type=='view'){
                    vm.viewFlag=true;
                    vm.editFlag=false;
                    vm.addFlag=false;
                    vm.title='行情详情';
                    vm.getTableDetail(id);
                }
                else if(type=='edit'){
                    vm.editFlag=true;
                    vm.viewFlag=false;
                    vm.addFlag=false;
                    vm.title='行情编辑';
                    vm.getTableDetail(id);
                }
                else{//新增
                    vm.addFlag=true;
                    vm.editFlag=false;
                    vm.viewFlag=false;
                    vm.title='发布行情';
                    //重置表单
                    for(var i in vm.newsForm){
                        vm.newsForm[i]="";
                        vm.imageUrl="";
                    }
                    vm.showFlag=false;//显示form 隐藏table
                }
                console.log("操作类型："+vm.operateType+"用户id:"+id);
                //接口改造，测试，正式去掉
                //vm.showFlag=false;//显示form 隐藏table

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
                var vm=this;
                vm.showFlag=false;//显示form 隐藏table
                vm.dataDetailList=data.data;
//                vm.newsForm=vm.dataDetailList; //暂时注释
                if(vm.editFlag){
                    vm.imageUrl=data.data.url;
                }
               //将data.data的值赋给 vm.newsForm
               vm.getDataFomat(vm.newsForm,data.data);
                //如果数据为空，显示“无”
                for(var i in vm.newsForm){
                    if(!vm.newsForm[i]){
                        vm.newsForm[i]="无";
                    }
                }
               //test 查看属性
               /*for(var i in vm.newsForm){
                 console.log("vm.newsForm["+i+"]:"+vm.newsForm[i]);
               }*/
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
            getTableList(){
                var vm=this;
                vm.loadFlag=true;
                vm.sear_beginDate=vm.sear_beginDate?vm.commonJs.timePattern(new Date(vm.sear_beginDate),'yyyy-MM-dd'):"";
                vm.sear_endDate=vm.sear_endDate?vm.commonJs.timePattern(new Date(vm.sear_endDate),'yyyy-MM-dd'):"";
                let params={
                    url:vm.apiUrl.transaction.tableUrl,
                    data:{
                        //查询参数
                        author:vm.sear_author,
                        beginDate:vm.sear_beginDate,
                        endDate:vm.sear_endDate,
                        //固参
                        channelCode:"NEWS",
                        category:"NEWS",
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
            //页码改变
            handleCurrentChange(val){
                var vm=this;
                vm.pNum=val;
                vm.getTableList();
            }
        },
        filters:{
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

</style>
