/**
* Created by gsy on 2017/08/21.
* 会员实名认证审核
*/
<template>
    <div class="certificate">
        <my-header :fatherTitle="fatitle" :headTitle="title" :viewFlag="viewFlag" :editFlag="editFlag" :addFlag="addFlag"></my-header>
        <!--搜索-->
        <div class="mysearch">
            <el-form :inline="true" class="search">
                <el-form-item label="姓名：">
                    <el-input v-model="name" placeholder="请输入姓名"></el-input>
                </el-form-item>
                <el-button type="primary" @click="onSearch">搜索</el-button>
                <el-button @click="onReset">重置</el-button>
            </el-form>
        </div>
        <!--表格列表-->
        <div class="tableList">
            <el-table :data="dataList" border stripe v-loading.body="loadFlag" style="width:100%;">
                <el-table-column type="selection"></el-table-column>
                <el-table-column prop="realName" label="客户姓名" >
                    <template scope="scope">
                        <div>{{scope.row.realName}}</div>
                    </template>
                </el-table-column>
                <el-table-column prop="mobile" label="手机号"></el-table-column>
                <el-table-column prop="idNumber" label="身份证号"></el-table-column>
                <el-table-column label="照片" width="250">
                    <template scope="scope">
                        <img :src="scope.row.imgOneURL" class="photoImg img1" @click="viewImg(scope.row.imgOneURL)" v-if="scope.row.imgOneURL && !dialogFormVisible"/>
                        <img :src="scope.row.imgTwoURL" class="photoImg" @click="viewImg(scope.row.imgTwoURL)" v-if="scope.row.imgTwoURL && !dialogFormVisible"/>
                        <el-dialog :visible.sync="dialogFormVisible" class="photoBox">
                            <img :src="imgUrl" class="photoBig"/>
                        </el-dialog>
                    </template>
                </el-table-column>
                <!--<el-table-column prop="success" label="状态" :formatter="formatStatus"></el-table-column>-->
                <el-table-column prop="success" label="状态" >
                    <template scope="scope">
                        <span :class="{red:scope.row.success=='N',green:scope.row.success=='Y'}">{{scope.row.success|statusFilter}}</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template scope="scope">
                        <el-button type="text" v-if="scope.row.success==''" @click="pass(scope.row.id)">通过</el-button>
                        <el-button type="text" v-if="scope.row.success==''" @click="refuse(scope.row.id)">驳回</el-button>
                        <el-dialog title="驳回意见" :visible.sync="dialogRmark">
                            <el-form>
                                <el-input type="textarea" :rows="6" placeholder="请输入内容" v-model="remark" auto-complete="off"></el-input>
                            </el-form>
                            <div slot="footer" class="dialog-footer">
                                <el-button @click="dialogCancel">取消</el-button>
                                <el-button @click="dialogOk" type="primary">确定</el-button>
                            </div>
                        </el-dialog>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页-->
            <el-pagination class="pageNation" @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="1"
            :page-sizes="[10,15,20,25,30]" :page-size="pSize" layout="total,sizes,prev,pager,next,jumper"
            :total="pageData.totalElements" v-show="pageData.totalElements>10"></el-pagination>
        </div>
    </div>
</template>

<script>
    import header from '~/components/header/header.vue'
    export default{
        data(){
            return {
                fatitle:'客户',
                title:'会员实名认证审核',
                /*header参数*/
                viewFlag:false,
                editFlag:false,
                addFlag:false,
                name:"",
                loadFlag:false,//加载显示
                dataList:[],
                imgUrl:"",
                dialogFormVisible:false,//显示大图对话框
                dialogRmark:false,//驳回意见对话框
                pageData:[],//分页数据
                pSize:10 ,//页容量
                pNum:1, //页码
                remark:"",//驳回意见
                approvalId:"",//实名认证id
                approvalFlag:"" //Y为通过，N为驳回

            }
        },
        created(){
            var vm=this;
            vm.getTableList();
        },
        deactivated(){
            var vm=this;
            vm.loadFlag=false;//避免超时闪现加载图标
        },

        methods: {
            // 实名认证审批接口
            doApproval(){
                var vm=this;
                let params={
                    url:vm.apiUrl.certificate.approvalUrl,
                    data:{
                        id:vm.approvalId,//实名认证记录id
                        success:vm.approvalFlag,//Y为通过，N为驳回 审批结果
                        remark:vm.remark //审批理由
                    }
                }
                vm.ax.post(params,vm.approvalCb);
            },
            approvalCb(data){
                var vm=this;
                var flag=vm.approvalFlag;
                console.log("审批类型："+flag);
                vm.getTableList();
                if(flag=='Y'){
                    vm.$message({
                        showClose:true,
                        message:"审核成功",
                        type:"success"
                    })
                }
                else if(flag=='N'){
                    vm.$message({
                        showClose:true,
                        message:"驳回成功",
                        type:"success"
                    })
                }


            },
            //“通过”审核
            pass(id){
                var vm=this;
                vm.approvalId=id;
                console.log("id:"+vm.approvalId);
                vm.approvalFlag="Y";
                vm.doApproval();
            },
            //"驳回"审核
            refuse(id){
                var vm=this;
                vm.dialogRmark=true;
                vm.approvalId=id;
                console.log("id:"+vm.approvalId);
                vm.approvalFlag="N";
            },
            //"驳回"取消
            dialogCancel(){
                var vm=this;
                vm.dialogRmark=false
            },
            //"驳回"确定
            dialogOk(){
                var vm=this;
                //do something
                vm.doApproval();
                vm.dialogRmark=false;
            },

            //点击照片查看
            viewImg(val){
                var vm=this;
                vm.dialogFormVisible = true;
                vm.imgUrl=val;
                console.log("图片地址："+vm.imgUrl);
            },
            //"状态"转换(暂不用，已使用filters)
            formatStatus(row,column){
                switch(row.success){
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
                vm.loadFlag=true;
                let params={
                    url:vm.apiUrl.certificate.tableUrl,
                    data:{
                        realName:vm.name, //用户真实姓名
                        pageNumber:vm.pNum,//页码
                        pageSize:vm.pSize
                    }
                }
                vm.ax.post(params,vm.tableListCb);
            },
            tableListCb(data){
                var vm=this;
                vm.loadFlag=false;
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
                vm.name="";
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
    @import '../../assets/css/member.css';
</style>
