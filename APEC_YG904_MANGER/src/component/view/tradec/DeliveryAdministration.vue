<template>
   <div>
       <el-row class="z-top">
           <el-col>
               <el-form :inline="true">
                   <el-form-item label="上传人">
                       <el-input v-model="name" placeholder="请输入内容"></el-input>
                   </el-form-item>
                   <el-form-item label="开始日期">
                      <start></start>
                   </el-form-item>
                   <el-form-item label="结束日期">
                       <end></end>
                   </el-form-item>
                   <el-form-item>
                       <el-button type="primary" @click="retrieve(1)">搜索</el-button>
                   </el-form-item>
               </el-form>
           </el-col>
       </el-row>
       <el-row class="z-top">
           <el-col :span="16">
               <el-table
                       :data="tableData"
                       border
                       style="width: 100%"
                       class="z-top">
                   <el-table-column
                           prop="date"
                           label="日期"
                           width="180">
                   </el-table-column>
                   <el-table-column
                           prop="seller"
                           label="卖货方"
                           width="180">
                   </el-table-column>
                   <el-table-column
                           prop="buyer"
                           label="买货方"
                           width="180">
                   </el-table-column>
                   <el-table-column
                           prop="weight"
                           label="总重量(斤)"
                           width="180">
                   </el-table-column>
                   <el-table-column
                           prop="total"
                           label="合计金额(元)"
                           width="180">
                   </el-table-column>
                   <el-table-column
                           prop="dateDelivery"
                           label="交收日期"
                           width="180">
                   </el-table-column>
                   <el-table-column
                           prop="upload"
                           label="上传人"
                           width="180">
                   </el-table-column>
                   <el-table-column
                           prop="pic"
                           label="图片"
                           width="180">
                   </el-table-column>
                   <el-table-column
                           label="操作"
                            width="180">
                       <template scope="scope">
                           <el-button type="text" size="small" @click="">编辑</el-button>
                           <el-button type="text" size="small"
                                      @click="del"
                           >删除</el-button>
                       </template>
                   </el-table-column>
               </el-table>
           </el-col>
       </el-row>
   </div>
</template>
<script>
    import Time from './time.vue'
    var data = [{
        date:"2017-2-10",
        seller:"张三",
        buyer:"李四",
        wight:10000,
        total:20000,
        upload:"李四",
        pic:"",
        dateDelivery:"2017-1-10"
    }]
    export default{
        data(){
          return {
              name:'',
              tableData:data
          }
        },
        components: {
            start:Time,
            end:Time
        },
        methods:{
            retrieve(){
                let vm = this;
//                正文
                let url = '/enquiry/manager/getCheckedBanker';
                let param = {
                    currentNo: this.currentNo,
                    pageSize: 15,
                    bankName: vm.organization,
                    bankerName: vm.agent
                };
                let callback = function (res) {
                    if (res.data.data == null) {
                        console.log('data = null');
                        vm.registeredList = [];
                    }
                    vm.registeredList = res.data.data.rows;
                    vm.pageCount = res.data.data.pageCount;
                };
                axiosPost(url, param, callback);
            },
            del(){
                alert("删除的代码逻辑接口");
            }
        }

    }
</script>