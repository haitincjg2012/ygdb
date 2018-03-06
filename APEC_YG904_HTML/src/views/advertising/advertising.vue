<template>
    <div class="certificate">
        <my-header v-on:initialPage="initialPage" :firstTitle="firsTit" :secondTitle="secondTit" :thirdTitle="thirdTit" :breadFlag="breadflag"></my-header>
        <!--搜索-->
        <div class="mysearch" v-if="showFlag">
            <el-form :inline="true" class="search clearfix">
                <el-form-item label="栏目类：">
	                <el-select v-model="query.channelId">
	                	<el-option
	                		v-for="(item, index) in categoryList"
	                		:key="index"
	                		:value="item.id"
	                		:label="item.name"
	                	></el-option>
	                </el-select>
                </el-form-item>
                <el-button type="primary" @click="retrieve(1)">搜索</el-button>
                <el-button @click="toClear">重置</el-button>
                <el-button class="fr" @click="goDetail">新增</el-button>
            </el-form>
        </div>
        <!--表格列表-->
        <div class="tableList" v-if="showFlag" style="margin-top: 20px;">
            <el-table :data="dataList" border stripe v-loading.body="loadCircle" header-row-class-name="headerRow"  >
            	<el-table-column label="图片" width="140">
            		<template slot-scope="scope">
            			<img style="max-height: 100px; max-width: 120px;" :src="scope.row.content+'?x-oss-process=style/_detail_hq'" v-if="scope.row.content.match(/http/)"/>
            			<span v-else>
						  <el-tooltip :content="scope.row.content" placement="bottom">
						    <span>{{scope.row.content.length>15?scope.row.content.substring(0,15)+'...':scope.row.content}}</span>
						  </el-tooltip>
            			</span>
            			
            		</template>
            	</el-table-column>
            	<el-table-column prop="channelName" label="广告位"></el-table-column>
            	<el-table-column prop="url" label="跳转地址" width="250"></el-table-column>
            	<el-table-column prop="point" label="上传时间">
            		<template slot-scope="scope">
            			{{filters.formatDate(scope.row.createDate)}}
            		</template>
            	</el-table-column>
            	<el-table-column prop="ordinal" label="排序"></el-table-column>
            	<el-table-column prop="remark" label="备注"></el-table-column>
                <el-table-column label="操作" fixed="right" width="100">
                    <template slot-scope="scope">
                		<el-button type="text" @click="goDetail(scope.row, 'edit')">编辑</el-button>
                		<el-button type="text" @click="del(scope.row)">删除</el-button>
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
	            <el-form :inline="true" label-width="100px" style="width: 700px; padding: 20px;">
	                <el-form-item label="广告位：">
		                <el-select v-model="detail.channelId" style="width: 461px;">
		                	<el-option
		                		v-for="(item, index) in categoryList"
		                		:key="index"
		                		:value="item.id"
		                		:label="item.name"
		                	></el-option>
		                </el-select>
	                </el-form-item>
	                <el-form-item label="图片：">
						<el-input style="width: 461px;" type="textarea" v-model="detail.content" v-if="detail.content&&!detail.content.match(/http/)"></el-input>
		                <!--aliyun方式上传图片-->
		                <el-upload v-else class="avatar-uploader" :headers="{'token':token}" :data="{}" :http-request="submitImg" :action="noUse" :show-file-list="false"  :before-upload="beforeLoad">
		                    <img style="max-width: 400px; max-height: 300px;" v-if="detail.content" :src="detail.content+'?x-oss-process=style/_stydetail'" alt="上传图片">
		                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
		                </el-upload>
		                <!--<div>
		                    <img :src="detail.imageUrl+'?x-oss-process=style/_list'" v-if="detail.imageUrl">
		                    <span v-if="!detail.imageUrl">无</span>
		                </div>-->
	                </el-form-item>
	                <el-form-item label="跳转类型：">
		                <el-select v-model="detail.type" style="width: 461px;" @change="typeChange">
		                	<el-option
		                		key="0"
		                		value="1"
		                		label="不做跳转"
		                	></el-option>
		                	<el-option
		                		key="0"
		                		value="2"
		                		label="外部html链接"
		                	></el-option>
		                </el-select>
	                </el-form-item>
	                <el-form-item label="Url地址：" v-if="detail.showUrl">
						<el-input style="width: 461px;" v-model="detail.url"></el-input>
	                </el-form-item>
	                <el-form-item label="备注：">
						<el-input style="width: 461px;" type="textarea" v-model="detail.remark"></el-input>
	                </el-form-item>
				  	<el-row class="text-center">
	                	<el-button v-if="isEditing" @click="update('edit')">确认</el-button>
	                	<el-button v-else @click="update">保存</el-button>
	                    <el-button @click="backTablePage">返回</el-button>
				  	</el-row>
	            </el-form>
        	</el-row>
        </div>
        
    </div>
</template>

<script>
    import header from '~/components/header/header.vue'
    import encrypt from '~/assets/js/oss/encrypt'
    
    //查询构造函数
    function Query(){
    	this.channelId = null;
    }
    //详情构造函数
    function Detail(){
    	this.channelCode = '';
    	this.category = '';
    	this.url = '';
    	this.remark = '';
    	this.content = '';
    	
    	//删除项
    	this.type = '1';
    	this.showUrl = false;
    	this.channelId = '';
    }
    
    let vm;
    export default{
        data(){
            return {
            	api: this.apiUrl.advertising,
                //breadcrumb
                firsTit:'广告',
                secondTit:'广告管理',
                thirdTit:'',
                breadflag:false,
                /*header参数*/
                viewFlag:false,
                showFlag:true, //true 显示table页; false 显示form页
                loadCircle:false,//加载显示
                dataList:[],

				//查询
				query: new Query(),
				categoryList: [],
				
				//详情
                isDetail: false,
                detail: new Detail(),
                isEditing: false,
                //header token
                token:this.$store.state.authToken || this.commonjs.getValue("authToken"),
                noUse:"",//因为ele-upload,action必传参数

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
            encrypt.getAuthorise();
            vm.getCategory();
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
            //校验图片格式
            beforeLoad(){
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
	                 vm.detail.content = vm.apiUrl.common.alyserverUrl+result.name;
	                 return true;
                },function(err){
	                 console.log("upload image failed by oss,"+ err);
	                 vm.$message.error('上传图片失败!');
	                 return false;
                });
            },
            //图片格式校验
            validateImg(){
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
			//获取栏目列表
			getCategory(){
                let params={
                    url:vm.api.category,
                    data: {}
                }
	            let callback = function(data){
					vm.categoryList = data.data;
	            }
                vm.ax.post(params, callback);
			},
			//跳转类型
			typeChange(){
				vm.detail.showUrl = vm.detail.type=='1'?false:true;
			},
			//详情
			goDetail(row, edit){
				vm.detail = new Detail();
				if(edit == 'edit'){
					vm.detail.id = row.id;
					vm.detail.channelId = row.channelId;
					vm.detail.url = row.url;
					vm.detail.remark = row.remark;
					vm.detail.content = row.content;
					vm.detail.showUrl = row.url?true:false;
					vm.detail.type = row.url?'2':'1';
					vm.isEditing = true;
				}
				vm.showFlag = false;
				vm.isDetail = true;
			},
			update(edit) {
				let detailCopy = vm.objCopy(vm.detail);
				delete detailCopy.type;
				delete detailCopy.showUrl;
				if(vm.detail.channelId){
					vm.categoryList.map(function(item){
						if(item.id == vm.detail.channelId){
							detailCopy.channelCode = item.code;
							detailCopy.category = item.category;
							delete detailCopy.channelId;
						}
					})
				}
				if(!vm.detail.showUrl){
					delete detailCopy.url;
				}
				
                let params={
                    url: vm.api.create,
                    data: detailCopy
                }
                if(edit == 'edit'){
                	params.url = vm.api.update;
                }
	            let callback = function(data){
					vm.showFlag = true;
					vm.isDetail = false;
					vm.$message.success("操作成功");
					vm.retrieve(vm.currentNo);
	            }
                vm.ax.post(params, callback);
			},
			//删除
			del(row){
				var callback = function(data){
					vm.$message.success("操作成功");
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
                            id: row.id, //用户id
                        }
                    };
	                vm.ax.post(params,callback);
	            }).catch(()=>{
	                    console.log("已取消删除");
	            });
			},
            //back to tablePage
            backTablePage(){
                vm.showFlag=true;
                vm.isAdding = false;
                vm.isEditing = false;
                vm.initialPage();//初始化页面标志
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
                    	channelId: vm.query.channelId,
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
