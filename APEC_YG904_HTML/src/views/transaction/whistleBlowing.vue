<template>
    <div class="certificate">
        <my-header v-on:initialPage="initialPage" :firstTitle="firsTit" :secondTitle="secondTit" :thirdTitle="thirdTit" :breadFlag="breadflag"></my-header>
        <!--搜索-->
        <div class="mysearch" v-if="showFlag">
            <el-form :inline="true" class="search clearfix" label-width="120px">
                <el-form-item label="举报人姓名：">
                    <el-input v-model="query.informantUser" placeholder="请输入"></el-input>
                </el-form-item>
                <el-form-item label="被举报人姓名：">
                    <el-input v-model="query.reportedUser" placeholder="请输入"></el-input>
                </el-form-item>
                <el-form-item label="关联数据来源：">
	                <el-select v-model="query.realm">
	                	<el-option
	                		v-for="(item, index) in realmList"
	                		:key="index"
	                		:value="item.value"
	                		:label="item.name"
	                	></el-option>
	                </el-select>
                </el-form-item>
                <el-form-item label="类型：">
	                <el-select v-model="query.feedBackType">
	                	<el-option
	                		v-for="(item, index) in typeList"
	                		:key="index"
	                		:value="item.value"
	                		:label="item.name"
	                	></el-option>
	                </el-select>
                </el-form-item>
                <el-button type="primary" @click="retrieve(1)">搜索</el-button>
                <el-button @click="toClear">重置</el-button>
            </el-form>
        </div>
        <!--表格列表-->
        <div class="tableList" v-if="showFlag" style="margin-top: 20px;">
            <el-table :data="dataList" border stripe v-loading.body="loadCircle" header-row-class-name="headerRow"  >
            	<el-table-column prop="id" label="编号"></el-table-column>
            	<el-table-column prop="feedBackInfo" label="举报原因" width="250"></el-table-column>
            	<el-table-column prop="supplementary" label="补充原因" width="250"></el-table-column>
            	<el-table-column label="举报凭证">
            		<template slot-scope="scope">
            			<a style="text-decoration: underline; cursor: pointer;color: #20A0FF;" @click="goDetail(scope.row)">查看图片</a>
            		</template>
            	</el-table-column>
            	<el-table-column prop="reportedUser" label="被举报人"></el-table-column>
            	<el-table-column prop="informantUser" label="举报人"></el-table-column>
            	<el-table-column prop="reportTime" label="举报时间">
            		<template slot-scope="scope">
            			{{filters.formatDate(scope.row.reportTime)}}
            		</template>
            	</el-table-column>
            	<el-table-column prop="reportTime" label="类型">
            		<template slot-scope="scope">
            			{{filters.whistleBlowingType(scope.row.feedBackType)}}
            		</template>
            	</el-table-column>
            	<el-table-column prop="reportTime" label="关联单据来源">
            		<template slot-scope="scope">
            			{{filters.whistleBlowingRealm(scope.row.realm)}}
            		</template>
            	</el-table-column>
            	<el-table-column prop="realm" label="关联单据">
            		<template slot-scope="scope">
            			<a style="text-decoration: underline; cursor: pointer;color: #20A0FF;" @click="relatedData(scope.row)">{{scope.row.relatedIds}}</a>
            		</template>
            	</el-table-column>
                <el-table-column label="操作" fixed="right">
                    <template slot-scope="scope">
                		<el-button type="text" @click="del(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页-->
			<div class="pagination-wrapper" style="text-align: right;">
				<el-pagination layout="jumper, sizes, prev, pager, next, total" :page-count="pageCount" :currentPage="currentNo" :page-sizes="[10, 20, 30, 40]" :page-size="pageSize" :total="total" @size-change="sizeChange" @current-change="retrieve"></el-pagination>
			</div>
        </div>
		
		<!--举报内容-->
        <el-dialog class="dialogTable" width="450px" title="查看举报内容" :visible.sync="isDetail">
            <el-form :inline="true" class="whistle-detail-form" label-width="100px">
                <el-form-item label="举报原因：">
                    {{detail.feedBackInfo}}
                </el-form-item>
                <el-form-item label="补充原因：">
                	{{detail.supplementary}}
                </el-form-item>
                <el-form-item label="举报凭证：">
                    <div class="clearfix">
                    	<span class="fl" v-for="item in detail.imgs" v-if="detail.certificateUrl" @click="scaleImg(item)">
                    		<img :src="item+'?x-oss-process=style/_list'"/>
                    	</span>
                    </div>
                </el-form-item>
                <el-form-item label="被举报人：">
                    {{detail.reportedUser}}
                </el-form-item>
                <el-form-item label="举报人：">
                    {{detail.informantUser}}
                </el-form-item>
                <el-form-item label="举报时间：" required>
                    {{filters.formatDatetime(detail.reportTime)}}
                </el-form-item>
            </el-form>
            <div class="text-center">
                <el-button @click="detailCancel">关闭</el-button>
            </div>
        </el-dialog>
		
		<!--图片放大-->
        <el-dialog class="imgDialog" width="80%" :visible.sync="showImg.show">
            <img :src="showImg.src">
        </el-dialog>
        
    </div>
</template>

<script>
    import header from '~/components/header/header.vue'
    import moment from 'moment'
    
    //查询构造函数
    function Query(){
    	this.informantUser = '';
    	this.reportedUser = '';
    	this.feedBackType = null;
    	this.realm = null;
    }
    function ShowImg(){
    	this.src = '';
    	this.show = false;
    }
    
    let vm;
    export default{
        data(){
            return {
            	api: this.apiUrl.whistleBlowing,
                //breadcrumb
                firsTit:'交易',
                secondTit:'举报',
                thirdTit:'',
                breadflag:false,
                /*header参数*/
                viewFlag:false,
                showFlag:true, //true 显示table页; false 显示form页
                loadCircle:false,//加载显示
                dataList:[],

				//查询
                query: new Query(),
                realmList: this.keys.transaction.whistleBlowing.realm,
                typeList: this.keys.transaction.whistleBlowing.type,
                //查看图片
                isDetail: false,
                detail: '',
                
                //放大图片
                showImg: new ShowImg(),

//				分页容器
				currentNo: 1,
				pageCount: 1,
				pageSize: 10,
				total: 0,
            }
        },
        activated(){
            vm = this;
            vm.retrieve(1);
        },
        deactivated(){
            var vm=this;
            vm.loadCircle=false;//避免超时闪现加载图标
        },
        methods: {
        	toClear(){
        		vm.query = new Query();
        		vm.retrieve(1);
        	},
//			每页数据条数变化
			sizeChange(size) {
				vm.retrieve(vm.currentNo, size);
			},
			//详情
			goDetail(row){
                let params={
                    url:vm.api.detail,
                    data: {
                    	id: row.id
                    }
                }
	            let callback = function(data){
					vm.isDetail = true;
					vm.detail = data.data;
					//将img做成数组
					vm.detail.imgs = vm.detail.certificateUrl.split(",");
	            }
                vm.ax.post(params, callback);
			},
			//图片放大
			scaleImg(src){
				vm.showImg.show = true;
				vm.showImg.src = src;
			},
			//查看管理单据
			relatedData(row){
				console.log("enter")
				sessionStorage.setItem('relatedIds',row.relatedIds);
				
				//点击跳转到该菜单选项的时候，切换菜单的active状态；
				function addActive(parentEle, textVal){
					parentEle.childNodes.forEach(function(value, index, array){
						if(value.className){
							if(value.className.indexOf("is-active")>-1){
								value.className = "el-menu-item";
							}
							if(value.innerText == textVal){
								value.click();
								return;
							}
							if(value.className.indexOf("el-submenu")>-1){
								addActive(value.getElementsByTagName("ul")[0]);
							}
						}
					});
				}
				var textVal, router;
				switch (row.realm){
					case 'VOCHER':
						router = 'settlement';
						textVal = '交收单管理';
						break;
					case 'SOCIETYPOST':
						router = 'moments';
						textVal = '果友圈动态';
						break;
					case 'PRODUCT':
						router = 'supplyDemand';
						textVal = '供求管理';
						break;
					case 'ARTICLE':
						router = 'newsIssue';
						textVal = '行情发布';
						break;
				}
				var transactionNav = document.getElementById("transactionNav");
				var thisUl = transactionNav.getElementsByTagName("ul")[0];
				addActive(thisUl, textVal);
				vm.$router.push(router);
			},
			detailCancel(){
				vm.isDetail = false;
			},
			//删除
			del(row){
				var callback = function(data){
					vm.$message.success("删除成功");
					vm.retrieve(vm.currentNo);
				}
                //确认删除框
                vm.$confirm('确认删除吗？','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(()=>{
                    //删除方法
                    let params={
                        url:vm.api.del,
                        data:{
                            id: row.id //用户id
                        }
                    };
	                vm.ax.post(params,callback);
	            }).catch(()=>{
	                    console.log("已取消删除");
	            });
			},
            //初始化页面
            initialPage(){
                vm.showFlag=true;//显示列表，隐藏表单
                vm.breadflag=false;
                vm.title='交收单管理';
            },
            //获取列表数据
			retrieve(currentNo, size) {
				if (size) {
					vm.pageSize = size;
				}
				vm.currentNo = currentNo;
                let params={
                    url:vm.api.retrieve,
                    data:{
                    	informantUser: vm.query.informantUser,
                    	reportedUser: vm.query.reportedUser,
                    	realm: vm.query.realm,
                    	feedBackType: vm.query.feedBackType,
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
.whistle-detail-form .el-form-item {
	width: 100%;
}
</style>