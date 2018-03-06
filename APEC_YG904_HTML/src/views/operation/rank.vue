<template>
    <div class="certificate">
        <my-header v-on:initialPage="initialPage" :firstTitle="firsTit" :secondTitle="secondTit" :thirdTitle="thirdTit" :breadFlag="breadflag"></my-header>
        <!--搜索-->
        <div class="mysearch" v-if="showFlag">
            <el-form :inline="true" class="search clearfix" label-width="100px">
                <el-form-item label="日期：">
                    <el-date-picker type="date" placeholder="开始时间" format="yyyy-MM-dd" v-model="query.startTime"></el-date-picker>
                    <span>-</span>
                    <el-date-picker type="date" placeholder="结束时间" format="yyyy-MM-dd" v-model="query.endTime"></el-date-picker>
                </el-form-item>
                <el-form-item label="类型：">
	                <el-select v-model="query.rankingType">
	                	<el-option
	                		v-for="(item, index) in rankingTypeList"
	                		:key="index"
	                		:value="item.value"
	                		:label="item.name"
	                	></el-option>
	                </el-select>
                </el-form-item>
                <el-form-item label="是否实时：">
	                <el-select v-model="query.actualTime">
	                	<el-option
	                		v-for="(item, index) in actualTimeList"
	                		:key="index"
	                		:value="item.value"
	                		:label="item.name"
	                	></el-option>
	                </el-select>
                </el-form-item>
                <el-button type="primary" @click="retrieve(1)">搜索</el-button>
                <el-button @click="goCreate">新增</el-button>
                <el-button @click="toClear">重置</el-button>
            </el-form>
        </div>
        <!--表格列表-->
        <div class="tableList" v-if="showFlag" style="margin-top: 20px;">
            <el-table :data="dataList" border stripe v-loading.body="loadCircle" header-row-class-name="headerRow"  >
            	<el-table-column prop="id" label="序号"></el-table-column>
            	<el-table-column prop="userLevelName" label="添加时间">
            		<template slot-scope="scope">
            			{{filters.formatDate(scope.row.createDate)}}
            		</template>
            	</el-table-column>
            	<el-table-column label="类型" prop="rankingTypeKey">
            	</el-table-column>
            	<el-table-column prop="point" label="时间范围">
            		<template slot-scope="scope">
            			{{filters.formatDatetime(scope.row.startTime)+'至'+filters.formatDatetime(scope.row.endTime)}}
            		</template>
            	</el-table-column>
            	<el-table-column prop="frezzing" label="是否实时">
            		<template slot-scope="scope">
            			{{scope.row.actualTime?"是":"否"}}
            		</template>
            	</el-table-column>
            	<el-table-column prop="auditState" label="状态"></el-table-column>
                <el-table-column label="操作" fixed="right" width="100">
                    <template slot-scope="scope">
                		<el-button v-if="scope.row.auditState != '进行中'&&scope.row.auditState != '已下线'" type="text" @click="goDetail(scope.row)">编辑</el-button>
                		<el-button v-if="scope.row.auditState != '已下线'" type="text" @click="quit(scope.row)">下线</el-button>
                		<el-button type="text" @click="goDetail(scope.row,'view')">详情</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页-->
			<div class="pagination-wrapper" style="text-align: right;">
				<el-pagination layout="jumper, sizes, prev, pager, next, total" :page-count="pageCount" :currentPage="currentNo" :page-sizes="[10, 20, 30, 40]" :page-size="pageSize" :total="total" @size-change="sizeChange" @current-change="retrieve"></el-pagination>
			</div>
        </div>

        <!-- Page-->
        <div v-if="!showFlag">
        	<el-row v-if="isDetail">
	            <el-form :inline="true" label-width="120px" style="width: 700px; padding: 20px;" class="wrap-form">
	            	<div class="settlement-div">
		                <el-form-item label="排行榜类型：">
		                    <span v-for="item in rankingTypeList" style="margin: 0 10px;">
		                    	<el-radio v-model="detail.rankingType" :label="item.value">{{item.name}}</el-radio>
		                    </span>
		                </el-form-item>
		                <el-form-item label="数据是否实时：">
		                    <el-switch
							  v-model="detail.actualTime"
							  active-text="是"
							  inactive-text="否"
							  active-color="#13ce66"
							  inactive-color="#ff4949">
							</el-switch>
		                </el-form-item>
		                <el-form-item label="时间范围：">
		                    <el-date-picker type="datetime" placeholder="开始时间" format="yyyy-MM-dd HH:mm:ss" v-model="detail.startTime"></el-date-picker>
		                    <span>-</span>
		                    <el-date-picker type="datetime" placeholder="结束时间" format="yyyy-MM-dd HH:mm:ss" v-model="detail.endTime"></el-date-picker>
		                </el-form-item>
		                <el-form-item label="上榜条件：">
							<div v-for="(item, index) in conditionsTypeList">
								<el-checkbox v-model="detail.conditions[index].checked" style="width: 100px;">{{item.name}}</el-checkbox>
								<el-input style="width: 120px;" @input="numberOnly" v-model="detail.conditions[index].num"></el-input>
								<span v-if="item.name=='调果量'">（斤）　&nbsp;</span><span v-else>（条/次）</span>
			                    <span v-for="item2 in categoriesList">
			                    	<el-radio v-model="detail.conditions[index].categories" :label="item2.value">{{item2.name}}</el-radio>
			                    </span>
							</div>
		                </el-form-item>
		                <el-form-item label="选择人员：" v-if="!detail.actualTime">
		                	<span style="margin: 0 10px;" v-for="item in detail.rankingMans">{{item.name}}</span>
							<el-button @click="chooseMember(1)"><i style="font-size: 20px;vertical-align: middle;margin-right: 5px;" class="el-icon-circle-plus"></i>选择人员</el-button>
		                </el-form-item>
		                <el-form-item label="备注：">
							<el-input style="width: 461px;" type="textarea" v-model="detail.remark"></el-input>
		                </el-form-item>
		                <!--<el-form-item label="是否上线：">
		                    <el-switch
							  v-model="detail.frezzing"
							  active-text="否"
							  inactive-text="是"
							  active-color="#ff4949"
							  inactive-color="#13ce66">
							</el-switch>
		                </el-form-item>-->
	                </div>
				  	<el-row class="text-center">
				  		<span v-if="!viewing">
		                	<el-button @click="update('create')" v-if="isCreating">保存</el-button>
		                	<el-button @click="update" v-else>确认</el-button>
	                	</span>
	                    <el-button @click="backTablePage">返回</el-button>
				  	</el-row>
	            </el-form>
        	</el-row>
        </div>
        
		<!--举报内容-->
        <el-dialog class="dialogTable" width="600px" title="修改积分" :visible.sync="isMember">
            <el-table :data="memberList" ref="memberTable"  border stripe @selection-change="handleChange">
                <el-table-column type="selection" width="35"></el-table-column>
                <el-table-column prop="name" label="昵称"></el-table-column>
                <!--<el-table-column prop="mobile" label="电话"></el-table-column>-->
                <el-table-column prop="userType" label="身份"></el-table-column>
            </el-table>
			<div class="pagination-wrapper" style="text-align: right;">
				<el-pagination layout="jumper, sizes, prev, pager, next, total" :page-count="pageCount2" :currentPage="currentNo2" :page-sizes="[10, 20, 30, 40]" :page-size="pageSize2" :total="total2" @size-change="sizeChange2" @current-change="chooseMember"></el-pagination>
			</div>
		  	<el-row class="text-center">
            	<el-button @click="memberConfirm">确认</el-button>
                <el-button @click="memberCancel">取消</el-button>
		  	</el-row>
        </el-dialog>
        
    </div>
</template>

<script>
    import header from '~/components/header/header.vue'
    import moment from 'moment'
    
    //查询构造函数
    function Query(){
    	this.rankingType = '';
    	this.actualTime = '';
    	this.startTime = '';
    	this.endTime = '';
    }
    //详情构造函数
    function Detail(){
    	this.rankingType = 'ACTIVE_MAN';
    	this.actualTime = '';
    	this.startTime = '';
    	this.endTime = '';
    	this.remark = '';
    	this.conditions = [];
    	this.rankingMans = [];
    }
//  上榜条件构造函数
    function Conditions(){
    	this.checked = false;
    	this.conditionsType = '';
    	this.num = '';
    	this.categories = 'TOTAL';
    }
    
    let vm;
    export default{
        data(){
            return {
            	api: this.apiUrl.rank,
                //breadcrumb
                firsTit:'运营',
                secondTit:'排行榜',
                thirdTit:'',
                breadflag:false,
                /*header参数*/
                viewFlag:false,
                showFlag:true, //true 显示table页; false 显示form页
                loadCircle:false,//加载显示
                dataList:[],

				//查询
				query: new Query(),
				rankingTypeList: this.keys.operation.rank.rankingTypeList,
				actualTimeList: this.keys.operation.rank.actualTimeList,
				//详情
				isCreating: false,
                isDetail: false,
                viewing: false,
                detail: new Detail(),
                conditionsTypeList: this.keys.operation.rank.conditionsTypeList,
                categoriesList: this.keys.operation.rank.categoriesList,
                isMember: false,
                memberList: [],
                memberSelected: [],

//				分页容器
				currentNo: 1,
				pageCount: 1,
				pageSize: 10,
				total: 0,
				currentNo2: 1,
				pageCount2: 1,
				pageSize2: 10,
				total2: 0,
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
			sizeChange2(size) {
				vm.chooseMember(vm.currentNo2, size);
			},
			//详情
			goDetail(row, view){
				if(view == 'view'){
					vm.viewing = true;
				}
				vm.detail = new Detail();
				vm.getConditions();
                let params={
                    url: vm.api.detail,
                    data: {id: row.id}
                }
	            let callback = function(data){
					vm.showFlag = false;
					vm.isDetail = true;
					vm.detail = vm.objCopy(data.data);
					vm.detail.conditions = [];
					vm.getConditions();
					vm.detail.conditions.map(function(item){
						data.data.conditions.map(function(item2){
							if(item.conditionsType == item2.conditionsType){
								item.checked = true;
								item.num = item2.num;
								item.categories = item2.categories;
							}
						})
					})
	            }
                vm.ax.post(params, callback);
			},
			//新增
			goCreate(){
				vm.showFlag = false;
				vm.isDetail = true;
				vm.isCreating = true;
				vm.detail = new Detail();
				vm.getConditions();
			},
			//获取上榜条件
			getConditions(){
				vm.conditionsTypeList.map(function(item){
					let con = new Conditions();
					con.conditionsType = item.value;
					vm.detail.conditions.push(con);
				});
			},
			//选择人员
			chooseMember(currentNo, size){
				if (size) {
					vm.pageSize2 = size;
				}
				vm.currentNo2 = currentNo;
				var full = true;//判断输入框为空
				var con = [];
				//处理上榜条件的选中
				vm.detail.conditions.map(function(item){
					if(item.checked){
						con.push({conditionsType: item.conditionsType, num: item.num, categories: item.categories});
						if(!item.num)full = false;
					}
				});
				if(!full){
					vm.$message.error("勾选的上榜条件请输入条数");
				}else{
	                let params={
	                    url:vm.api.member,
	                    data:{
	                        pageNumber: vm.currentNo2,
	                        pageSize: vm.pageSize2,
	                    	rankingType: vm.detail.rankingType,
	                    	conditions: con
	                    }
	                }
		            let callback = function(data){
		                vm.memberList = data.data.rows;
	//					vm.currentNo2 = data.data.currentNo;
						vm.total2 = data.data.totalElements;
						vm.isMember = true;
						setTimeout(function(){
							vm.detail.rankingMans.map(function(item){
								vm.memberList.map(function(item2){
									if(item.userId == item2.userId){
										vm.$refs.memberTable.toggleRowSelection(item2);
									}
								})
							});
						},0);
		            }
	                vm.ax.post(params, callback);
				}
			},
			//成员选中
			handleChange(val){
				vm.memberSelected = val;
			},
			//成员确认
			memberConfirm(){
				vm.detail.rankingMans = [];
				if(vm.memberSelected.length>0){
					vm.memberSelected.map(function(item){
						vm.detail.rankingMans.push(item);
					})
				}
				vm.isMember = false;
			},
			//取消选择成员
			memberCancel(){
				vm.isMember = false;
			},
			update(word) {
				var detailCopy = vm.objCopy(vm.detail);
				detailCopy.startTime = vm.detail.startTime;
				detailCopy.endTime = vm.detail.endTime;
				if(!detailCopy.startTime || !detailCopy.endTime){
					vm.$message.error("请输入开始时间和结束时间");
				}else if(!vm.filters.compareTime(detailCopy.startTime, detailCopy.endTime)){
					vm.$message.error("开始时间不能大于结束时间");
				}else{
					var full = true;//判断输入框为空
					var con = [];
					//处理上榜条件的选中
					vm.detail.conditions.map(function(item){
						if(item.checked){
							con.push({conditionsType: item.conditionsType, num: item.num, categories: item.categories});
							if(!item.num)full = false;
						}
					});
					detailCopy.conditions = con;
					//处理人员的选中
					if(!vm.detail.actualTime){//实时与人二选一
						var userIds = [];
						vm.detail.rankingMans.map(function(item){
							userIds.push({userId: item.userId});
						});
						detailCopy.rankingMans = userIds;
					}else{
						detailCopy.rankingMans = [];
					}
					
					if(!full){
						vm.$message.error("勾选的上榜条件请输入条数");
					}else{
		                let params={
		                    url:vm.api.create,
		                    data: detailCopy
		                }
						//新增
						if(word != 'create'){
							params.url = vm.api.update;
						}
			            let callback = function(data){
							vm.showFlag = true;
							vm.isDetail = false;
							vm.$message.success("操作成功");
							vm.retrieve(vm.currentNo);
			            }
		                vm.ax.post(params, callback);
					}
				}
			},
			//删除
			quit(row){
				var callback = function(data){
					vm.$message.success("操作成功");
					vm.retrieve(vm.currentNo);
				}
                //确认删除框
                vm.$confirm('确认下线吗？','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(()=>{
                    //删除方法
                    let params={
                        url:vm.api.out,
                        data:{
                            id: row.id
                        }
                    };
	                vm.ax.post(params,callback);
	            }).catch(()=>{
	                    console.log("已取消下线");
	            });
			},
            //back to tablePage
            backTablePage(){
                vm.showFlag=true;
                vm.isCreating = false;
                vm.isDetail = false;
                vm.viewing = false;
            },
            //初始化页面
            initialPage(){
                vm.showFlag=true;//显示列表，隐藏表单
                vm.breadflag=false;
                vm.title='排行榜';
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
                    	rankingType: vm.query.rankingType,
                    	actualTime: vm.query.actualTime,
                    	startTime: vm.query.startTime,
                    	endTime: vm.query.endTime,
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
