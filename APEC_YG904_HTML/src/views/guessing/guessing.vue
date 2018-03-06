<template>
    <div class="certificate">
        <my-header v-on:initialPage="initialPage" :firstTitle="firsTit" :secondTitle="secondTit" :thirdTitle="thirdTit" :breadFlag="breadflag"></my-header>
        <!--搜索-->
        <div class="mysearch" v-if="showFlag">
            <el-form :inline="true" class="search clearfix">
                <el-form-item label="期号：">
                    <el-input v-model="issue" placeholder="请输入期号"></el-input>
                </el-form-item>
                <el-button type="primary" @click="retrieve(1)">搜索</el-button>
                <el-button @click="toClear">重置</el-button>
                <el-button @click="goCreate" class="fr">新增</el-button>
            </el-form>
        </div>
        <!--表格列表-->
        <div class="tableList" v-if="showFlag" style="margin-top: 20px;">
            <el-table :data="dataList" border stripe v-loading.body="loadCircle" header-row-class-name="headerRow"  >
            	<el-table-column prop="id" label="编号"></el-table-column>
            	<el-table-column prop="issue" label="期号"></el-table-column>
            	<el-table-column prop="title" label="竞猜标题"></el-table-column>
            	<el-table-column prop="content" label="竞猜内容" width="300"></el-table-column>
            	<el-table-column prop="remark" label="备注信息" width="300"></el-table-column>
                <el-table-column prop="createDate" label="创建时间">
            		<template slot-scope="scope">
                		 {{scope.row.createDate|timeFilters}}
                	</template>
                </el-table-column>
                <el-table-column prop="startTime" label="上线时间">
            		<template slot-scope="scope">
                		 {{scope.row.startTime|timeFilters}}
                	</template>
                </el-table-column>
                <el-table-column prop="endTime" label="下线时间">
            		<template slot-scope="scope">
                		 {{scope.row.endTime|timeFilters}}
                	</template>
                </el-table-column>
                <el-table-column prop="endTime" label="出竞猜结果时间">
            		<template slot-scope="scope">
                		 {{scope.row.passDate|timeFilters}}
                	</template>
                </el-table-column>
            	<el-table-column prop="auditState" label="竞猜结果">
            		<template slot-scope="scope">
                		 {{scope.row.auditState|stateFilter}}
                	</template>
            	</el-table-column>
            	<el-table-column prop="auditStateStr" label="状态"></el-table-column>
            	<el-table-column prop="quotationHigh" label="看涨的人数"></el-table-column>
            	<el-table-column prop="quotationLight" label="看跌的人数"></el-table-column>
            	<el-table-column prop="replyCount" label="评论量"></el-table-column>
            	<el-table-column prop="totalUser" label="参与的总人数"></el-table-column>
                <el-table-column label="操作" width="200" fixed="right">
                    <template slot-scope="scope">
                    	<span v-if="scope.row.auditStateStr=='进行中'">
                    		<el-button type="text" @click="goDetail(scope.row)">详情</el-button>
                    		<el-button type="text" @click="edit(scope.row)">编辑</el-button>
                    		<el-button type="text" @click="outLine(scope.row)">下线</el-button>
                    	</span>
                    	<span v-if="scope.row.auditStateStr=='未开始'">
                    		<el-button type="text" @click="goDetail(scope.row)">详情</el-button>
                    		<el-button type="text" @click="edit(scope.row)">编辑</el-button>
                    		<el-button type="text" @click="del(scope.row)">删除</el-button>
                    	</span>
                    	<span v-if="scope.row.auditStateStr=='已下线'">
                    		<el-button type="text" @click="goDetail(scope.row)">详情</el-button>
                    		<el-button type="text" @click="conclusion(scope.row)">评定结果</el-button>
                    		<el-button type="text" @click="del(scope.row)">删除</el-button>
                    	</span>
                    	<span v-if="scope.row.auditStateStr=='已结束'">
                    		<el-button type="text" @click="goDetail(scope.row)">详情</el-button>
                    		<el-button type="text" @click="del(scope.row)">删除</el-button>
                    	</span>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页-->
			<div class="pagination-wrapper" style="text-align: right;">
				<el-pagination layout="jumper, sizes, prev, pager, next, total" :page-count="pageCount" :currentPage="currentNo" :page-sizes="[10, 20, 30, 40]" :page-size="pageSize" :total="total" @size-change="sizeChange" @current-change="retrieve"></el-pagination>
			</div>
        </div>

        <!--企业form Page-->
        <div v-if="!showFlag&&!isAdding">
        	<el-form :inline="true" class="search clearfix addingform">
                <el-form-item label="竞猜判定结果：">
                    <el-tag v-if="detailList.auditState=='Y'" color="#CC6633">{{detailList.auditState|stateFilter}}</el-tag>
                    <el-tag v-if="detailList.auditState=='N'" color="#009966">{{detailList.auditState|stateFilter}}</el-tag>
                </el-form-item>
                <el-form-item>
                	<template slot-scope="scope">
                		参与人数： <a :style="{'color': colorTotalUser}" @click="changeAuditState()">{{detailList.totalUser}}</a>
                	</template>
				</el-form-item>
                <el-form-item>
                	<template slot-scope="scope">
                		看涨： <a :style="{'color': colorQuotationHigh}" @click="changeAuditState('Y')">{{detailList.quotationHigh}}</a>
                	</template>
                </el-form-item>
                <el-form-item>
                	<template slot-scope="scope">
                		看跌： <a :style="{'color': colorQuotationLight}" @click="changeAuditState('N')">{{detailList.quotationLight}}</a>
                	</template>
                </el-form-item>
            </el-form>
            <el-table :data="userInfoList" border stripe>
            	<el-table-column prop="issue" label="头像">
            		<template slot-scope="scope">
            			<img style="height: 35px;width: 35px;" v-if="scope.row.imgUrl" :src="scope.row.imgUrl+'?x-oss-process=style/_head'"/>
                		<img style="height: 35px;width: 35px;" v-else src="../../assets/images/DBKS.png"/>
                	</template>
            	</el-table-column>
            	<el-table-column prop="name" label="昵称"></el-table-column>
            	<el-table-column prop="userTypeKey" label="代办"></el-table-column>
            	<el-table-column prop="userType" label="类型">
            		<template slot-scope="scope">
                    	<el-tag v-if="scope.row.auditState=='Y'" color="#CC6633">看涨</el-tag>
                    	<el-tag v-if="scope.row.auditState=='N'" color="#009966">看跌</el-tag>
                	</template>
            	</el-table-column>
            </el-table>
            <!--分页-->
			<div class="pagination-wrapper" style="text-align: right;">
				<el-pagination layout="jumper, sizes, prev, pager, next, total" :page-count="pageCount2" :currentPage="currentNo2" :page-sizes="[5, 10, 20, 30]" :page-size="pageSize2" :total="total2" @size-change="sizeChange2" @current-change="retrieve2"></el-pagination>
			</div>
			<!--评论-->
			<el-row>
				<h1 style="font-size: 18px; border-bottom: 1px solid #ddd; margin-top: 10px;">用户评价</h1>
    			<el-col style="padding: 10px;" v-for="comment in commentList">
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
    					{{comment.content}}<el-button type="text" class="fr" @click="delMonment(comment)">删除</el-button>
    				</el-row>
    				<el-row v-for="item in comment.societyLzlReplyViewVOS">
    					<span style="color: #0b55a0;">{{item.name}}@{{item.toUserName}}：</span>{{item.content}}
    					<el-button type="text" class="fr" @click="delSubMonment(item)">删除</el-button>
    				</el-row>
    			</el-col>
    		</el-row>
			<div class="pagination-wrapper" style="text-align: right;" v-if="commentList.length">
				<el-pagination layout="jumper, sizes, prev, pager, next, total" :page-count="pageCount3" :currentPage="currentNo3" :page-sizes="[5, 10, 20, 30]" :page-size="pageSize3" :total="total3" @size-change="sizeChange3" @current-change="retrieve3"></el-pagination>
			</div>
			<div v-else style="text-align: center; font-size: 16px; line-height: 50px;">
				<h1>暂无用户评价信息</h1>
			</div>
		  	<el-row class="text-center">
                <el-button @click="backTablePage">返回</el-button>
		  	</el-row>
        </div>
        
        <!--新增-->
        <div v-if="!showFlag&&isAdding">
        	<el-alert
        		:closable="false"
			    title="活动名称及标题尽量控制在12个汉字的大小之内，尽可能以数字，-，/ 等符号表明意图"
			    type="warning">
			</el-alert>
        	<el-alert
        		:closable="false"
			    title="几个备用的名称及标题大致有，果价猜猜猜，果价竞猜，猜果价，猜红富士，本月9日-22日，至12月底，1月上旬，12月份"
			    type="warning">
			</el-alert>
            <el-form :inline="true" label-width="100px" style="width: 700px; padding: 20px;">
            	<div class="settlement-div">
	                <el-form-item label="活动名称：" required>
	                    <el-input style="width: 461px;" v-model="add.title"></el-input>
	                </el-form-item>
	                <el-form-item label="时间范围：" required>
					    <el-date-picker
					      v-model="add.startTime"
					      type="datetime"
					      :disabled="disabledStartTime"
					      @change="changeDate"
					      placeholder="开始日期时间"
					      >
					    </el-date-picker> 至 
					    <el-date-picker
					      v-model="add.endTime"
					      type="datetime"
					      placeholder="结束日期时间"
					      >
					    </el-date-picker>
	                </el-form-item>
	                <el-form-item label="期号：">
	                    <el-input style="width: 461px;" v-model="add.issue" disabled></el-input>
	                </el-form-item>
	                <el-form-item label="备注：">
	                    <el-input style="width: 461px;" v-model="add.remark"></el-input>
	                </el-form-item>
	                <el-form-item label="内容：">
						<el-input style="width: 461px;" placeholder="尽量言简意赅，让用户明白是猜什么时候的果价，如，12月份截止" type="textarea" v-model="add.content"></el-input>
	                </el-form-item>
                </div>
			  	<el-row class="text-center">
                	<el-button v-if="isEditing" @click="createSubmit('edit')">确认</el-button>
                	<el-button v-else @click="createSubmit('add')">保存</el-button>
                    <el-button @click="backTablePage">返回</el-button>
			  	</el-row>
            </el-form>
        </div>
         <!--行情结果判定-->
        <el-dialog class="dialogTable" width="400px" title="判定结果" :visible.sync="isJudging">
            <el-form :inline="true">
                <el-select v-model="auditState" placeholder="请选择判定结果" style="width: 100%;margin-bottom: 20px;">
                  <el-option label="涨" value="Y"></el-option>
                  <el-option label="跌" value="N"></el-option>
                </el-select>
			  	<el-row class="text-center">
                	<el-button @click="judgeConfirm">确定</el-button>
                    <el-button @click="judgeCancel">取消</el-button>
			  	</el-row>
            </el-form>
        </el-dialog>
        
    </div>
</template>

<script>
    import header from '~/components/header/header.vue'
    import moment from 'moment'
    
    //新增构造函数
    function Add(){
    	this.title = '';
    	this.content = '';
    	this.remark = '';
    	this.startTime = '';
    	this.endTime = '';
    	this.issue = '';
    }
    let vm;
    export default{
        data(){
            return {
            	api: this.apiUrl.guessing,
            	issue: '',//期号
                //breadcrumb
                firsTit:'价格竞猜',
                secondTit:'竞猜信息',
                thirdTit:'',
                breadflag:false,
                /*header参数*/
                viewFlag:false,
                showFlag:true, //true 显示table页; false 显示form页
                loadCircle:false,//加载显示
                dataList:[],
                //详情部分
                detailList: '',
                userInfoList: [],
                quotationId: '',//用于查询参与用户
                auditState: '',
                commentList: [],//评论列表
                //判定结果
                isJudging: false,
                auditState: '',
                judgeId: '',
                
                
                //新增部分
                isAdding: false,
                add: new Add(),
                //编辑部分
                isEditing: false,
                disabledStartTime: false,
                colorTotalUser:'',
                colorQuotationHigh:'',
                colorQuotationLight: '',

//				分页容器
				currentNo: 1,
				pageCount: 1,
				pageSize: 10,
				total: 0,
				currentNo2: 1,
				pageCount2: 1,
				pageSize2: 5,
				total2: 0,
				currentNo3: 1,
				pageCount3: 1,
				pageSize3: 5,
				total3: 0,
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
        filters: {
        	typeFormatter:function(val){
        		return val == "KS"?"客商":"果农";	
        	},
        	userTypeFilter:function(val){
        		var re;
        		switch(val){
        			case "DB": 
        				re = "代办";
        				break;
        			case "KS": 
        				re = "客商";
        				break;
        			case "LK": 
        				re = "冷库";
        				break;
        			case "ZZH": 
        				re = "果农";
        				break;
        			case "HZS": 
        				re = "合作社";
        				break;
        		}
        		return re;
        	},
        	stateFilter:function(val){
        		let re;
        		switch(val){
        			case '0': 
        				re = "";//未出结果
        				break;
        			case 'Y': 
        				re = "涨";
        				break;
        			case 'N': 
        				re = "跌";
        				break;
        		}
        		return re;
        	},
        	timeFilters: function(val){
        		return val?moment(val).format("YYYY-MM-DD HH:mm:ss"):"";
        	}
        },

        methods: {
        	toClear(){
        		vm.issue = '';
        		vm.retrieve(1);
        	},
//			每页数据条数变化
			sizeChange(size) {
				vm.retrieve(vm.currentNo, size);
			},
			sizeChange2(size) {
				vm.retrieve2(vm.currentNo2, size);
			},
			sizeChange3(size) {
				vm.retrieve3(vm.currentNo3, size);
			},
    		//新增
    		goCreate(){
                vm.thirdTit='新增';
                vm.breadflag=true;
	            vm.showFlag=false;//显示form 隐藏table
                vm.disabledStartTime = false;
                vm.isEditing = false;
                vm.isAdding = true;
                vm.add = new Add();
    		},
    		//选中时间
    		changeDate(val){
    			if(val){
    				vm.add.issue = vm.moment(val).format("YYYYMMDD")+"期";
    			}
    		},
    		//新增商品确认
    		createSubmit(addOrEdit){
    			let params;
    			if(addOrEdit=='add'){
	    			let addCopy = vm.objCopy(vm.add);
	    			addCopy.startTime = vm.add.startTime;
	    			addCopy.endTime = vm.add.endTime;
	                params={
	                    url: vm.api.create,
	                    data: addCopy
	                };
    			}else{
    				let editData = {
    					id: vm.add.id,
    					title: vm.add.title,
    					content: vm.add.content,
    					remark: vm.add.remark,
    					startTime: vm.add.startTime,
    					endTime: vm.add.endTime,
    					issue: vm.add.issue
    				}
	                params={
	                    url: vm.api.edit,
	                    data: editData
	                };
    			}
				let callback = function (data) {
					vm.$message.success("操作成功");
					vm.showFlag = true;
					vm.retrieve(1);
				};
				if(!vm.add.title||!vm.add.startTime||!vm.add.endTime){
					vm.$message.error("请填写完整信息");
				}else{
					vm.ax.post(params, callback);
				}
			},
			//详情
			goDetail(row){
				vm.showFlag = false;
				vm.isAdding = false;
				vm.detailList = row;
				vm.quotationId = row.id;
				vm.auditState = '';
				vm.retrieve2(1);
				vm.retrieve3(1);
			},
            //删除评论
            delMonment(row){
                let params={
                    url:vm.api.delMonment,
                    data:{
                        id: row.id//用户id
                    }
                }
                let callback = function(data){
                	vm.$message.success("删除成功");
                	vm.retrieve3(1);
                }
                vm.ax.post(params, callback);
            },
            //删除楼中评论
            delSubMonment(row){
                let params={
                    url:vm.api.delSubMonment,
                    data:{
                        id: row.id//用户id
                    }
                }
                let callback = function(data){
                	vm.$message.success("删除成功");
                	vm.retrieve3(1);
                }
                vm.ax.post(params, callback);
            },
			//下线
			outLine(row){
                let params={
                    url:vm.api.outLine,
                    data:{
                        id: row.id//用户id
                    }
                }
                let callback = function(data){
                	vm.$message.success("下线成功");
                	vm.retrieve(1);
                }
                
				vm.$confirm('确定下线吗？', '消息', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
				}).then(() => {
					 vm.ax.post(params, callback);
				}).catch(() => {
					console.log('取消');
				});
			},
			del(row){
                let params={
                    url:vm.api.del,
                    data:{
                        id: row.id//用户id
                    }
                }
                let callback = function(data){
                	vm.$message.success("删除成功");
                	vm.retrieve(1);
                }
                
				vm.$confirm('确定删除吗？', '消息', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
				}).then(() => {
					 vm.ax.post(params, callback);
				}).catch(() => {
					console.log('取消');
				});
			},
			//编辑
			edit(row){
                vm.thirdTit='编辑';
                vm.breadflag=true;
	            vm.showFlag=false;//显示form 隐藏table
                vm.isAdding = true;
                vm.isEditing = true;
                //进行中的话 开始时间不可以修改
                vm.disabledStartTime = false;
                if(row.auditStateStr == '进行中'){
                	vm.disabledStartTime = true;
                }
                vm.add = row;
                vm.add.startTime = vm.add.startTime?new Date(vm.add.startTime):"";
                vm.add.endTime = vm.add.endTime?new Date(vm.add.endTime):"";
			},
			//评定结果
			conclusion(row){
				vm.isJudging = true;
				vm.judgeId = row.id;
				vm.auditState = '';
			},
			//取消评定
			judgeCancel(){
				vm.isJudging = false;
			},
			//确认评定
			judgeConfirm(){
                let params={
                    url:vm.api.judge,
                    data:{
                        id: vm.judgeId,
                        auditState: vm.auditState
                    }
                }
                let callback = function(data){
                	vm.$message.success("操作成功");
                	vm.isJudging = false;
                	vm.retrieve(1);
                }
				if(!vm.auditState){
					vm.$message.error("请先选择");
				}else{
					vm.ax.post(params, callback);
				}
			},
		    
            //back to tablePage
            backTablePage(){
                vm.showFlag=true;
                vm.isAdding = false;
                vm.isEditing = false;
                vm.initialPage();//初始化页面标志
                vm.retrieve(vm.currentNo);//获取table数据
            },
            //初始化页面
            initialPage(){
                vm.viewFlag=false;//表单只读标识
                vm.showFlag=true;//显示列表，隐藏表单
                vm.breadflag=false;
                vm.title='交收单管理';
            },
            //
            changeAuditState(param){
            	console.log(param)
            	if(param=='Y'||param=='N'){
            		vm.auditState = param;
            		vm.colorTotalUser = '';
            		vm.colorQuotationHigh = '';
            		vm.colorQuotationLight = '';
            		param=='Y'?vm.colorQuotationHigh='#ff4050':vm.colorQuotationLight='#ff4050';
            	}else{
            		vm.auditState = '';
            		vm.colorTotalUser = '#ff4050';
            		vm.colorQuotationHigh = '';
            		vm.colorQuotationLight = '';
            	}
            	vm.retrieve2(1);
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
                    	issue: vm.issue,
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
           },
            //获取详情列表
			retrieve2(currentNo, size) {
				if (size) {
					vm.pageSize2 = size;
				}
				vm.currentNo2 = currentNo;
                let params={
                    url:vm.api.userInfo,
                    data:{
                        pageNumber: currentNo,//页码
                        pageSize: vm.pageSize2,
                        quotationId: vm.quotationId,
                        auditState: vm.auditState
                    }
                }
	            let callback = function(data){
	                vm.userInfoList = data.data.rows;
					vm.pageCount2 = data.data.pageCount;
					vm.total2 = data.data.totalElements;
	            }
                vm.ax.post(params, callback, vm);
           },
            //获取评论列表
			retrieve3(currentNo, size) {
				if (size) {
					vm.pageSize3 = size;
				}
				vm.currentNo3 = currentNo;
                let params={
                    url:vm.api.comment,
                    data:{
                        pageNumber: currentNo,//页码
                        pageSize: vm.pageSize3,
                        societyPostId: vm.quotationId
                    }
                }
	            let callback = function(data){
	                vm.commentList = data.data.rows;
					vm.pageCount3 = data.data.pageCount;
					vm.total3 = data.data.totalElements;
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
	.el-tag {color: #fff;}
	.addingform a {
		text-decoration: underline; color: #20A0FF; cursor: pointer;
	}
</style>
