<template>
    <div class="certificate">
        <my-header v-on:initialPage="backTablePage" :firstTitle="firsTit" :secondTitle="secondTit" :thirdTitle="thirdTit" :breadFlag="breadflag"></my-header>
        <!--搜索-->
        <div class="mysearch" v-if="showFlag">
            <el-form :inline="true" class="search clearfix">
                <el-form-item label="活动名称：">
                    <el-input v-model="query.lotteryName" placeholder="请输入"></el-input>
                </el-form-item>
                <el-button type="primary" @click="retrieve(1)">搜索</el-button>
                <el-button @click="toClear">重置</el-button>
                <el-button class="fr" @click="goCreate">新增</el-button>
            </el-form>
        </div>
        <!--表格列表-->
        <div class="tableList" v-if="showFlag" style="margin-top: 20px;">
            <el-table :data="dataList" border stripe v-loading.body="loadCircle" header-row-class-name="headerRow"  >
            	<el-table-column label="图片">
            		<template slot-scope="scope">
            			<img :src="scope.row.imageUrl+'?x-oss-process=style/_list'" v-if="scope.row.imageUrl" style="width: 60px; height: 60px;"/>
            		</template>
            	</el-table-column>
            	<el-table-column label="添加时间">
            		<template slot-scope="scope">
            			{{filters.formatDatetime(scope.row.createDate)}}
            		</template>
            	</el-table-column>
            	<el-table-column prop="lotteryName" label="活动名称">
            	</el-table-column>
            	<el-table-column label="时间范围">
            		<template slot-scope="scope">
            			{{filters.formatDatetime(scope.row.startTime)+'至'+filters.formatDatetime(scope.row.endTime)}}
            		</template>
            	</el-table-column>
            	<el-table-column prop="totalPeople" label="参与人次"></el-table-column>
            	<el-table-column prop="auditState" label="状态"></el-table-column>
                <el-table-column label="操作" fixed="right" width="180">
                    <template slot-scope="scope">
                		<el-button type="text" v-if="scope.row.auditState == '未开始'" @click="goDetail(scope.row, 'isEdit')">编辑</el-button>
                		<el-button type="text" v-if="scope.row.auditState == '进行中'" @click="out(scope.row)">下线</el-button>
                		<el-button type="text" @click="goDetail(scope.row, 'isDetail')">详情</el-button>
                		<el-button type="text" v-if="scope.row.auditState != '未开始'" @click="goStatistics(scope.row)">中奖统计</el-button>
                		<el-button type="text" v-if="scope.row.auditState != '进行中'" @click="del(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页-->
			<div class="pagination-wrapper" style="text-align: right;">
				<el-pagination layout="jumper, sizes, prev, pager, next, total" :page-count="pageCount" :currentPage="currentNo" :page-sizes="[10, 20, 30, 40]" :page-size="pageSize" :total="total" @size-change="sizeChange" @current-change="retrieve"></el-pagination>
			</div>
        </div>

        <!-- 新增 -->
        <div v-if="!showFlag">
        	<!--新增和修改-->
        	<el-row v-if="!isDetail && !isStatistics && !isEdit">
        		<el-steps :active="activeStep" style="margin: 20px; width: 1000px;">
				  <el-step title="步骤 1"  description="填写基本信息" icon="el-icon-edit"></el-step>
				  <el-step title="步骤 2"  description="用户规则" icon="el-icon-tickets"></el-step>
				  <el-step title="步骤 3"  description="奖品设置" icon="el-icon-star-on"></el-step>
				  <el-step title="步骤 4"  description="完成" icon="el-icon-circle-check"></el-step>
				</el-steps>
				<!--新增活动-->
				<el-row class="clearfix" style="width: 1000px;" v-if="activeStep==1">
		            <el-form :inline="true" label-width="100px" style="width: 600px; padding: 20px;float: left;">
		                <el-form-item label="抽奖形式：">
		                    <span v-for="item in formList" style="margin: 0 10px;">
		                    	<el-radio v-model="add.lotteryFormType" @change="formChange" :label="item.lotteryFormType">{{item.lotteryFormTypeKey}}</el-radio>
		                    </span>
		                </el-form-item>
		                <el-form-item label="活动名称：">
		                    <el-input style="width: 461px;" v-model="add.lotteryName"></el-input>
		                </el-form-item>
		                <el-form-item label="图片：">
			                <!--aliyun方式上传图片-->
			                <el-upload class="avatar-uploader" :headers="{'token':token}" :data="{}" :http-request="submitImg" :action="noUse" :show-file-list="false"  :before-upload="beforeLoad">
			                    <img v-if="add.imageUrl" :src="add.imageUrl+'?x-oss-process=style/_list'" alt="上传图片">
			                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
			                </el-upload>
			                <!--<div>
			                    <img :src="add.imageUrl+'?x-oss-process=style/_list'" v-if="add.imageUrl">
			                    <span v-if="!add.imageUrl">无</span>
			                </div>-->
		                </el-form-item>
		                <el-form-item label="HTML地址：">
							<el-input style="width: 461px;" v-model="add.url"></el-input>
		                </el-form-item>
		                <el-form-item label="时间范围：">
		                    <el-date-picker type="datetime" placeholder="开始时间" format="yyyy-MM-dd HH:mm:ss" v-model="add.startTime"></el-date-picker>
		                    <span>-</span>
		                    <el-date-picker type="datetime" placeholder="结束时间" format="yyyy-MM-dd HH:mm:ss" v-model="add.endTime"></el-date-picker>
		                </el-form-item>
		                <el-form-item label="备注：">
							<el-input style="width: 461px;" type="textarea" v-model="add.remark"></el-input>
		                </el-form-item>
		            </el-form>
		            <el-form :inline="true" label-width="100px" style="width: 300px; padding: 20px;float: left;border-left: solid 1px #e4e4e4;margin-top: 30px;">
		                <h1>提示：</h1>
		                <el-form-item label="没中奖：">
		                    <el-input v-model="add.lotteryHintVOS[0].remark"></el-input>
		                </el-form-item>
		                <el-form-item label="中奖：">
		                    <el-input v-model="add.lotteryHintVOS[1].remark"></el-input>
		                </el-form-item>
		                <el-form-item label="不达条件：">
		                    <el-input v-model="add.lotteryHintVOS[2].remark" type="textarea"></el-input>
		                </el-form-item>
		            </el-form>
		        </el-row>
		        <!--活动规则-->
				<el-row class="clearfix" style="width: 1000px;" v-if="activeStep==2">
		            <el-form :inline="true" label-width="120px" style="width: 600px; padding: 20px;">
		                <el-form-item label="参与条件：">
			                <el-select v-model="rule.conditionId">
			                	<el-option
			                		v-for="(item, index) in conditionList"
			                		:key="index"
			                		:value="item.id"
			                		:label="item.conditionName"
			                	></el-option>
			                </el-select>
		                </el-form-item>
		                <el-form-item label="参与次数：">
		                    <span v-for="item in numType" style="margin: 0 10px;">
		                    	<el-radio v-model="rule.participationNumType" :label="item.value">{{item.name}}</el-radio>
		                    </span>
		                </el-form-item>
		                <el-form-item label="消耗积分：">
							<el-input style="width: 461px;" v-model="rule.points"></el-input>
		                </el-form-item>
		                <el-form-item label="参与赠送积分：">
							<el-input style="width: 461px;" v-model="rule.participationPoints"></el-input>
							<p><el-checkbox v-model="rule.checkUnPrize" style="width: 100px;">仅送给未中奖的用户</el-checkbox></p>
		                </el-form-item>
		            </el-form>
		        </el-row>
		        <!--奖品设置-->
				<el-row class="clearfix" style="width: 1000px;" v-if="activeStep==3">
		            <h1 class="clearfix">
		            	<div class="fl">
		            		<span style="margin: 0 10px;">
		                    	<el-radio v-model="prize.checkPrizePoint" :label="false" @change="getPrizeList(1)">商品</el-radio>
		                    	<el-radio v-model="prize.checkPrizePoint" :label="true" @change="getPrizeList(1)">积分</el-radio>
		                    </span>
		            	</div>
		            	<div class="fr">
		            		<el-button type="primary" @click="prizeCreate">增加奖品</el-button>
		            	</div>
		            </h1>
		            <el-table :data="prizeList" border stripe v-loading.body="loadCircle" header-row-class-name="headerRow"  >
		            	<el-table-column label="奖品图片">
		            		<template slot-scope="scope">
		            			<img :src="scope.row.imageUrl" v-if="scope.row.imageUrl" style="width: 60px; height: 60px;"/>
		            		</template>
		            	</el-table-column>
		            	<el-table-column prop="prizeNobelKey" label="奖项"></el-table-column>
		            	<el-table-column prop="totalNum" label="数量"></el-table-column>
		            	<el-table-column prop="winningRate" label="中奖率">
		                    <template slot-scope="scope">
		                		{{scope.row.winningRate}}%
		                    </template>
		            	</el-table-column>
		            	<el-table-column prop="limitedAward" label="中奖限制"></el-table-column>
		                <el-table-column label="操作">
		                    <template slot-scope="scope">
		                		<el-button type="text" @click="prizeEdit(scope.row)">修改</el-button>
		                		<el-button type="text" @click="prizeDel(scope.row)">删除</el-button>
		                    </template>
		                </el-table-column>
		            </el-table>
		            <!--分页-->
					<div class="pagination-wrapper" style="text-align: right;">
						<el-pagination layout="jumper, sizes, prev, pager, next, total" :page-count="pageCount2" :currentPage="currentNo2" :page-sizes="[10, 20, 30, 40]" :page-size="pageSize2" :total="total2" @size-change="sizeChange2" @current-change="getPrizeList"></el-pagination>
					</div>
		        </el-row>
		        <!--完成-->
		        <el-row class="clearfix" style="width: 1000px;" v-if="activeStep==4">
		            <h1 class="clearfix" style="text-align: center; line-height: 70px; margin: 80px 0;font-size: 20px;" v-if="isEdit">
		            	<i class="el-icon-success" style="font-size: 50px; color: #008000;vertical-align: middle;margin-right: 10px;"></i><span style="vertical-align: middle;">编辑完成</span>
		            </h1>
		            <h1 class="clearfix" style="text-align: center; line-height: 70px; margin: 80px 0;font-size: 20px;" v-else>
		            	<i class="el-icon-success" style="font-size: 50px; color: #008000;vertical-align: middle;margin-right: 10px;"></i><span style="vertical-align: middle;">抽奖活动已生成</span>
		            </h1>
		        </el-row>
		        <div style="width: 1000px;">
				  	<el-row class="text-center" v-if="activeStep == 4">
	                    <el-button @click="backTablePage">完成</el-button>
				  	</el-row>
				  	<el-row class="text-center" v-else>
	                	<el-button @click="goNext">下一步</el-button>
	                    <el-button @click="backTablePage">返回</el-button>
				  	</el-row>
		        </div>
        	</el-row>
        	
        	<!--详情-->
        	<el-row v-if="isDetail || isEdit">
				<!--活动-->
				<el-row class="clearfix" style="width: 1000px;">
		            <el-form :inline="true" label-width="100px" style="width: 600px; padding: 20px;float: left;">
		                <el-form-item label="抽奖形式：">
		                    <span v-for="item in formList" style="margin: 0 10px;">
		                    	<el-radio v-model="add.lotteryFormType" :disabled="!isEdit" :label="item.lotteryFormType">{{item.lotteryFormTypeKey}}</el-radio>
		                    </span>
		                </el-form-item>
		                <el-form-item label="活动名称：">
		                    <el-input style="width: 461px;" :disabled="!isEdit" v-model="add.lotteryName"></el-input>
		                </el-form-item>
		                <el-form-item label="图片：">
			                <!--aliyun方式上传图片-->
			                <el-upload class="avatar-uploader" :headers="{'token':token}" :data="{}" :http-request="submitImg" :action="noUse" :show-file-list="false"  :before-upload="beforeLoad">
			                    <img style="max-height: 300px; max-width: 300px;" v-if="add.imageUrl" :src="add.imageUrl+'?x-oss-process=style/_stylist'" alt="上传图片">
			                    <span v-else>
			                    	<i v-if="isEdit" class="el-icon-plus avatar-uploader-icon"></i>
			                    </span>
			                </el-upload>
			                <!--<div>
			                    <img :src="add.imageUrl+'?x-oss-process=style/_list'" v-if="add.imageUrl">
			                    <span v-if="!add.imageUrl">无</span>
			                </div>-->
		                </el-form-item>
		                <el-form-item label="HTML地址：">
							<el-input style="width: 461px;" :disabled="!isEdit" v-model="add.url"></el-input>
		                </el-form-item>
		                <el-form-item label="时间范围：">
		                    <el-date-picker type="datetime" :disabled="!isEdit" placeholder="开始时间" format="yyyy-MM-dd HH:mm:ss" v-model="add.startTime"></el-date-picker>
		                    <span>-</span>
		                    <el-date-picker type="datetime" :disabled="!isEdit" placeholder="结束时间" format="yyyy-MM-dd HH:mm:ss" v-model="add.endTime"></el-date-picker>
		                </el-form-item>
		                <el-form-item label="备注：">
							<el-input style="width: 461px;" :disabled="!isEdit" type="textarea" v-model="add.remark"></el-input>
		                </el-form-item>
		            </el-form>
		            <el-form :inline="true" label-width="100px" style="width: 300px; padding: 20px;float: left;border-left: solid 1px #e4e4e4;margin-top: 30px;">
		                <h1>提示：</h1>
		                <el-form-item label="没中奖：">
		                    <el-input v-model="add.lotteryHintVOS[0].remark" :disabled="!isEdit" @blur="tipsBlur(add.lotteryHintVOS[0])"></el-input>
		                </el-form-item>
		                <el-form-item label="中奖：">
		                    <el-input v-model="add.lotteryHintVOS[1].remark" :disabled="!isEdit" @blur="tipsBlur(add.lotteryHintVOS[1])"></el-input>
		                </el-form-item>
		                <el-form-item label="不达条件：">
		                    <el-input v-model="add.lotteryHintVOS[2].remark" :disabled="!isEdit" @blur="tipsBlur(add.lotteryHintVOS[2])" type="textarea"></el-input>
		                </el-form-item>
		            </el-form>
		        </el-row>
			  	<el-row class="text-center" style="width: 1000px;">
                	<el-button type="primary" v-if="isEdit" @click="editActivitiy">保存</el-button>
			  	</el-row>
		        <!--活动规则-->
				<el-row class="clearfix" style="width: 1000px;">
		            <el-form :inline="true" label-width="120px" style="width: 600px; padding: 20px;">
		                <el-form-item label="参与条件：">
			                <el-select v-model="rule.conditionId" :disabled="!isEdit">
			                	<el-option
			                		v-for="(item, index) in conditionList"
			                		:key="index"
			                		:value="item.id"
			                		:label="item.conditionName"
			                	></el-option>
			                </el-select>
		                </el-form-item>
		                <el-form-item label="参与次数：">
		                    <span v-for="item in numType" style="margin: 0 10px;">
		                    	<el-radio v-model="rule.participationNumType" :disabled="!isEdit" :label="item.value">{{item.name}}</el-radio>
		                    </span>
		                </el-form-item>
		                <el-form-item label="消耗积分：">
							<el-input style="width: 461px;" :disabled="!isEdit" v-model="rule.points"></el-input>
		                </el-form-item>
		                <el-form-item label="参与赠送积分：">
							<el-input style="width: 461px;" :disabled="!isEdit" v-model="rule.participationPoints"></el-input>
							<p><el-checkbox v-model="rule.checkUnPrize" :disabled="!isEdit" style="width: 100px;">仅送给未中奖的用户</el-checkbox></p>
		                </el-form-item>
		            </el-form>
		        </el-row>
			  	<el-row class="text-center" style="width: 1000px;">
                	<el-button type="primary" v-if="isEdit" @click="editRules">保存</el-button>
			  	</el-row>
		        <!--奖品设置-->
				<el-row class="clearfix" style="width: 1000px;">
		            <h1 class="clearfix">
		            	<div class="fl">
		            		<span style="margin: 0 10px;">
		                    	<el-radio v-model="prize.checkPrizePoint" :label="false" @change="getPrizeList(1)">商品</el-radio>
		                    	<el-radio v-model="prize.checkPrizePoint" :label="true" @change="getPrizeList(1)">积分</el-radio>
		                    </span>
		            	</div>
		            	<div class="fr">
		            		<el-button type="primary" @click="prizeCreate" v-if="isEdit">增加奖品</el-button>
		            	</div>
		            </h1>
		            <el-table :data="prizeList" border stripe v-loading.body="loadCircle" header-row-class-name="headerRow"  >
		            	<el-table-column label="奖品图片">
		            		<template slot-scope="scope">
		            			<img :src="scope.row.imageUrl+'?x-oss-process=style/_list'" v-if="scope.row.imageUrl" style="width: 60px; height: 60px;"/>
		            		</template>
		            	</el-table-column>
		            	<el-table-column prop="prizeNobelKey" label="奖项"></el-table-column>
		            	<el-table-column prop="totalNum" label="数量"></el-table-column>
		            	<el-table-column prop="winningRate" label="中奖率">
		                    <template slot-scope="scope">
		                		{{scope.row.winningRate}}%
		                    </template>
		            	</el-table-column>
		            	<el-table-column prop="limitedAward" label="中奖限制"></el-table-column>
		                <el-table-column label="操作" v-if="isEdit">
		                    <template slot-scope="scope">
		                		<el-button type="text" @click="prizeEdit(scope.row)">修改</el-button>
		                		<el-button type="text" @click="prizeDel(scope.row)">删除</el-button>
		                    </template>
		                </el-table-column>
		            </el-table>
		            <!--分页-->
					<div class="pagination-wrapper" style="text-align: right;">
						<el-pagination layout="jumper, sizes, prev, pager, next, total" :page-count="pageCount2" :currentPage="currentNo2" :page-sizes="[10, 20, 30, 40]" :page-size="pageSize2" :total="total2" @size-change="sizeChange2" @current-change="getPrizeList"></el-pagination>
					</div>
		        </el-row>
		        <div style="width: 1000px;">
				  	<el-row class="text-center">
	                    <el-button @click="backTablePage">返回</el-button>
				  	</el-row>
		        </div>
        	</el-row>
        	
        	<!--中奖统计-->
        	<el-row v-if="isStatistics">
	        	<el-form :inline="true" class="search clearfix addingform">
	                <el-form-item>
	                	<template slot-scope="scope">
	                		参与人数： <a>{{participatePopulation}}</a>
	                	</template>
					</el-form-item>
	                <el-form-item>
	                	<template slot-scope="scope">
	                		中奖人数： <a>{{winPopulation}}</a>
	                	</template>
					</el-form-item>
	            </el-form>
	        	<el-form :inline="true" class="search clearfix addingform">
	                <el-form-item label="奖项：">
		                <el-select v-model="prizeNobel" @change="prizeNobelChange">
		                	<el-option
		                		key="0"
		                		value=""
		                		label="全部"
		                	></el-option>
		                	<el-option
		                		v-for="(item, index) in prizeLevelList"
		                		:key="index"
		                		:value="item.value"
		                		:label="item.name"
		                	></el-option>
		                </el-select>
	                </el-form-item>
	            </el-form>
	            <el-table :data="statisticsList" border stripe>
	            	<el-table-column prop="issue" label="头像">
	            		<template slot-scope="scope">
	            			<img style="height: 35px;width: 35px;" v-if="scope.row.imgUrl" :src="scope.row.imgUrl"/>
	                		<img style="height: 35px;width: 35px;" v-else src="../../assets/images/DBKS.png"/>
	                	</template>
	            	</el-table-column>
	            	<el-table-column prop="name" label="中奖人"></el-table-column>
	            	<el-table-column prop="mobile" label="电话"></el-table-column>
	            	<el-table-column prop="userTypeKey" label="身份"></el-table-column>
	            	<el-table-column prop="prizeNobelKey" label="奖项"></el-table-column>
	            	<el-table-column prop="prizeAwardName" label="奖品"></el-table-column>
	            </el-table>
	            <!--分页-->
				<div class="pagination-wrapper" style="text-align: right;">
					<el-pagination layout="jumper, sizes, prev, pager, next, total" :page-count="pageCount3" :currentPage="currentNo3" :page-sizes="[5, 10, 20, 30]" :page-size="pageSize3" :total="total3" @size-change="sizeChange3" @current-change="getStatistics(1)"></el-pagination>
				</div>
		        <div style="width: 1000px;">
				  	<el-row class="text-center">
	                    <el-button @click="backTablePage">返回</el-button>
				  	</el-row>
		        </div>
        	</el-row>
        </div>
        
        <!--新增奖品设置-->
        <el-dialog class="dialogTable" width="700px" title="设置奖品" :visible.sync="prize.create">
            <el-form :inline="true" label-width="120px" style="width: 370px; padding: 20px;display: inline-block;">
                <!--<el-form-item label="">
            		<span style="margin: 0 10px;">
                    	<el-radio v-model="prize.checkPrizePoint" :label="false" @change="getPrizeList(1)">商品</el-radio>
                    	<el-radio v-model="prize.checkPrizePoint" :label="true" @change="getPrizeList(1)">积分</el-radio>
                    </span>
                </el-form-item>-->
                <el-form-item label="奖品：">
					<el-input style="width: 200px;" v-model="prize.prizeName"></el-input>
                </el-form-item>
                <el-form-item label="奖项：">
	                <el-select v-model="prize.prizeNobel">
	                	<el-option
	                		v-for="(item, index) in prizeLevelList"
	                		:key="index"
	                		:value="item.value"
	                		:label="item.name"
	                	></el-option>
	                </el-select>
                </el-form-item>
                <el-form-item label="数量：">
					<el-input style="width: 200px;" v-model="prize.num"></el-input>
                </el-form-item>
                <el-form-item label="总数量：">
					<el-input style="width: 200px;" v-model="prize.totalNum"></el-input>
                </el-form-item>
                <el-form-item label="中奖率：">
					<el-input style="width: 200px;" v-model="prize.winningRate"></el-input>%
                </el-form-item>
                <el-form-item label="中奖限制：">
					每人每次只能得<el-input style="width: 50px;margin: 0 5px;" v-model="prize.limitedAward"></el-input>件
					<!--<el-checkbox v-model="rule.limitedAward" >每人每次只能得<el-input style="width: 50px;margin: 0 5px;" v-model="prize.num"></el-input>件</el-checkbox>-->
                </el-form-item>
            </el-form>
            <span style="display: inline-block; vertical-align: top;" v-if="!prize.checkPrizePoint">
            	<el-upload class="avatar-uploader" :headers="{'token':token}" :data="{}" :http-request="submitImgPrize" :action="noUse" :show-file-list="false"  :before-upload="beforeLoad">
                    <img v-if="prize.imageUrl" :src="prize.imageUrl+'?x-oss-process=style/_list'" alt="上传图片">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
            </span>
		  	<el-row class="text-center">
            	<el-button @click="prizeCreateSave('edit')" v-if="prize.edit" type="primary">保存</el-button>
            	<el-button @click="prizeCreateSave('create')" v-else type="primary">保存</el-button>
                <el-button @click="prizeCreateCancel">取消</el-button>
		  	</el-row>
        </el-dialog>
        
    </div>
</template>

<script>
    import header from '~/components/header/header.vue'
    import encrypt from '~/assets/js/oss/encrypt'
    
    //查询构造函数
    function Query(){
    	this.lotteryName = '';
    }
    //详情构造函数
    function Add(){
    	this.lotteryName = '';
    	this.lotteryFormType = 'BIG_WHEEL';
    	this.url = 'https://yg.ap88.com/#/activePage_one';
    	this.startTime = '';
    	this.endTime = '';
    	this.imageUrl = '';
    	this.remark = '';
    	this.lotteryHintVOS = [
    		{participationState: 'NOT_GET_PRIZE', remark: ''},
    		{participationState: 'GET_PRIZE', remark: ''},
    		{participationState: 'UNABLE_PARTICIPATE', remark: ''}
    	];
    }
    //活动规则构造函数
    function Rule(){
    	this.conditionId = '';
    	this.participationNumType = 'DAILY_ONE';
    	this.points = '';
    	this.participationPoints = '';
    	this.checkUnPrize = false;
    	this.lotteryId = '';
    }
    //奖品构造函数
    function Prize(){
    	this.checkPrizePoint = false;
    	this.prizeName = '';
    	this.prizeNobel = '';
    	this.num = '';
    	this.totalNum = '';
    	this.lotteryId = '';
    	this.winningRate = '';
    	this.limitedAward = '';
    	this.imageUrl = '';
    	this.remark = '';
    	
    	//删除项
    	this.create = false;
    	this.edit = false;
    }
    let vm;
    export default{
        data(){
            return {
            	api: this.apiUrl.lottery,
                //breadcrumb
                firsTit:'运营',
                secondTit:'抽奖活动',
                thirdTit:'',
                breadflag:false,
                /*header参数*/
                viewFlag:false,
                showFlag:true, //true 显示table页; false 显示form页
                loadCircle:false,//加载显示
                dataList:[],

				//查询
				query: new Query(),
				
				//新增
                isAdding: false,
                //新增活动
                add: new Add(),
                rule: new Rule(),//活动规则
                prize: new Prize(),//奖品设置
                prizeList: [],
                conditionList: [],
                formList: [],
                activeStep: 1,
                lotteryId: '',
                numType: this.keys.operation.lottery.numType,//次数类型
                participateCon: this.keys.operation.lottery.numType,//参与条件
                prizeLevelList: this.keys.operation.lottery.prizeLevel,//奖项
                //header token
                token:this.$store.state.authToken || this.commonjs.getValue("authToken"),
                noUse:'',//因为ele-upload,action必传参数
                
                //编辑
                isEdit: false,

				//详情
                isDetail: false,
                
                //中奖统计
                isStatistics: false,
                statisticsList: [],
                prizeNobel: '',
                participatePopulation: '',
                winPopulation: '',
                
//				分页容器
				currentNo: 1,
				pageCount: 1,
				pageSize: 10,
				total: 0,
				currentNo2: 1,
				pageCount2: 1,
				pageSize2: 10,
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
            vm.backTablePage();
            vm.getCondition();
            encrypt.getAuthorise();
            vm.getForm();
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
				vm.getPrizeList(vm.currentNo2, size);
			},
			sizeChange3(size) {
				vm.getStatistics(vm.currentNo3, size);
			},
			//查看中奖统计
			goStatistics(row){
				vm.showFlag = false;
				vm.isStatistics = true;
				vm.lotteryId = row.id;
				vm.getStatistics();
                let params={
                    url:vm.api.statisticsPopulation,
                    data:{
                    	id: row.id
                    }
                }
	            let callback = function(data){
					vm.participatePopulation = data.data.totalPeople;
					vm.winPopulation = data.data.lackPeople;
	            }
                vm.ax.post(params, callback);
			},
			//奖项切换
			prizeNobelChange(){
				vm.getStatistics();
			},
			//抽奖形式切换
			formChange(){
				vm.formList.map(function(item){
					if(vm.add.lotteryFormType == item.lotteryFormType){
						vm.add.url = item.url;
					}
				})
			},
			//中奖统计
			getStatistics(currentNo, size){
				if (size) {
					vm.pageSize2 = size;
				}
				vm.currentNo2 = currentNo;
                let params={
                    url:vm.api.statistics,
                    data:{
                        pageNumber: vm.currentNo2,
                        pageSize: vm.pageSize2,
                    	prizeNobel: vm.prizeNobel,
                    	lotteryId: vm.lotteryId,
                    	lackPeople: true
                    }
                }
	            let callback = function(data){
	                vm.statisticsList = data.data.rows;
					vm.currentNo3 = data.data.currentNo;
					vm.total3 = data.data.totalElements;
	            }
                vm.ax.post(params, callback);
			},
			//编辑 和详情
			goDetail(row, name){
				vm.lotteryId = row.id;
				vm.showFlag = false;
				vm[name] = true;
				//获取详情 抽奖活动
                let params={
                    url: vm.api.gameDetail,
                    data: {id: row.id}
                }
	            let callback = function(data){
					vm.add = data.data?data.data:new Add();
	            }
                vm.ax.post(params, callback);
				//获取详情 规则
				let params2 = {
                    url: vm.api.ruleDetail,
                    data: {lotteryId: vm.lotteryId}
                }
				let callback2 = function(data){
					vm.rule = data.data?data.data:new Rule();
					vm.rule.lotteryId = row.id;
	            }
				vm.ax.post(params2, callback2);
				//获取详情 奖品
				vm.prize = new Prize();
				vm.getPrizeList();
			},
			//获取活动条件
			getCondition(){
                let params={
                    url:vm.api.condition,
                    data: {}
                }
	            let callback = function(data){
					vm.conditionList = data.data;
	            }
                vm.ax.post(params, callback);
			},
			//修改抽奖
			editActivitiy(){
				let params = {
                    url: vm.api.createUpdate,
                    data: vm.add
                }
				let callback = function(data){
					vm.$message.success("保存成功");
	            }
                vm.ax.post(params, callback);
			},
			//修改规则
			editRules(){
				let params = {
                    url: vm.api.updateRule,
                    data: vm.rule
                }
				if(!vm.rule.id)params.url = vm.api.createRule;
				let callback = function(data){
					vm.$message.success("保存成功");
	            }
                vm.ax.post(params, callback);
			},
			//提示修改
			tipsBlur(row){
				let params = {
                    url: vm.api.updateTips,
                    data: row
                }
				let callback = function(data){
	            }
                vm.ax.post(params, callback);
			},
			//下一步
			goNext(){
                let params = {};
	            let callback;
				switch(vm.activeStep){
					case 1:
						params = {
		                    url: vm.api.create,
		                    data: vm.add
		                }
						callback = function(data){
							vm.lotteryId = data.data.id;
							vm.rule = new Rule();
							vm.rule.lotteryId = data.data.id;
							vm.activeStep = 2;
			            }
						/*//编辑情况下
						if(vm.isEdit){
							params.url = vm.api.createUpdate;
							callback = function(data){
								vm.activeStep = 2;
								//如果是编辑  查询编辑信息
								let params2 = {
				                    url: vm.api.ruleDetail,
				                    data: {lotteryId: vm.lotteryId}
				                }
								let callback2 = function(data){
									vm.rule = data.data;
					            }
								vm.ax.post(params2, callback2);
				            }
						}else{
							callback = function(data){
								vm.lotteryId = data.data.id;
								vm.rule = new Rule();
								vm.rule.lotteryId = data.data.id;
								vm.activeStep = 2;
				            }
						}*/
						break;
					case 2:
						params = {
		                    url: vm.api.createRule,
		                    data: vm.rule
		                }
						callback = function(data){
							vm.activeStep = 3;
//							vm.getPrizeList();
			            }
						break;
					case 3:
						vm.activeStep = 4;
						return;
						break;
				}
				vm.ax.post(params, callback);
			},
			//获取活动形式
			getForm(){
                let params={
                    url:vm.api.form,
                    data: {}
                }
	            let callback = function(data){
					vm.formList = data.data;
	            }
                vm.ax.post(params, callback);
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
	                 vm.add.imageUrl = vm.apiUrl.common.alyserverUrl+result.name;
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
    		//新增
    		goCreate(){
	            vm.showFlag = false;//显示form 隐藏table
                vm.isAdding = true;
                vm.add = new Add();
    		},
    		//下线
			out(row){
	            let callback = function(data){
	                vm.$message.success('操作成功!');
	                vm.retrieve(vm.currentNo);
	            }
                //确认删除框
                vm.$confirm('确认下线吗？','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(()=>{
	                let params={
	                    url:vm.api.out,
	                    data: {id: row.id}
	                }
	                vm.ax.post(params,callback);
	            }).catch(()=>{
	                console.log("已取消下线");
	            });
			},
			//删除
			del(row){
	            let callback = function(data){
	                vm.$message.success('删除成功!');
	                vm.retrieve(vm.currentNo);
	            }
                //确认删除框
                vm.$confirm('确认删除吗？','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(()=>{
	                let params={
	                    url:vm.api.del,
	                    data: {id: row.id}
	                }
	                vm.ax.post(params,callback);
	            }).catch(()=>{
	                console.log("已取消删除");
	            });
			},
			/**奖品设置开始**/
			//获取列表
			getPrizeList(currentNo, size){
				if (size) {
					vm.pageSize2 = size;
				}
				vm.currentNo2 = currentNo;
                let params={
                    url:vm.api.prizeRetrieve,
                    data:{
                        pageNumber: vm.currentNo2,
                        pageSize: vm.pageSize2,
                    	lotteryId: vm.lotteryId,
                    	checkPrizePoint: vm.prize.checkPrizePoint
                    }
                }
	            let callback = function(data){
	                vm.prizeList = data.data.rows;
					vm.currentNo2 = data.data.currentNo;
					vm.total2 = data.data.totalElements;
	            }
                vm.ax.post(params, callback);
			},
            //奖品设置上传图片
            submitImgPrize(data){
                var vm = this;
                console.log("submitImg:" + data);
                //gsy
                return encrypt.ossUpload(data.file,function(result){
	                 vm.$message.success('上传图片成功!');
	                 vm.prize.imageUrl = vm.apiUrl.common.alyserverUrl+result.name;
	                 return true;
                },function(err){
	                 console.log("upload image failed by oss,"+ err);
	                 vm.$message.error('上传图片失败!');
	                 return false;
                });
            },
			//新增
			prizeCreate(){
				let checkPrizePoint = vm.prize.checkPrizePoint;
				vm.prize = new Prize();
				vm.prize.lotteryId = vm.lotteryId;
				vm.prize.checkPrizePoint = checkPrizePoint;
				vm.prize.create = true;
			},
			//新增保存
			prizeCreateSave(name){
				let prize = vm.objCopy(vm.prize);
				delete prize.create;
				delete prize.edit;
                let params={
                    url:vm.api.createPrize,
                    data: prize
                }
                if(name == 'edit'){
                	params.url = vm.api.prizeUpdate;
                }
	            let callback = function(data){
	                vm.$message.success('操作成功!');
					vm.prize.create = false;
	                vm.getPrizeList();
	            }
                vm.ax.post(params, callback);
			},
			//新增取消
			prizeCreateCancel(){
				vm.prize.create = false;
			},
			//编辑
			prizeEdit(row){
				//将属性逐个赋值
				for(var str in vm.prize){
					if(row.hasOwnProperty(str)){
						vm.prize[str] = row[str];
					}
				}
				vm.prize.id = row.id;
				vm.prize.create = true;
				vm.prize.edit = true;
			},
			//删除
			prizeDel(row){
                let params={
                    url:vm.api.prizeDel,
                    data: {id: row.id}
                }
	            let callback = function(data){
	                vm.$message.success('操作成功!');
	                vm.getPrizeList();
	            }
                vm.ax.post(params, callback);
			},
			/**奖品设置结束**/
            //back to tablePage
            backTablePage(){
                vm.showFlag=true;
                vm.breadflag=false;
                vm.isAdding = false;
                vm.isDetail = false;
                vm.isStatistics = false;
				vm.isEdit = false;
				vm.activeStep = 1;
                vm.retrieve(vm.currentNo);
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
                    	lotteryName: vm.query.lotteryName,
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
