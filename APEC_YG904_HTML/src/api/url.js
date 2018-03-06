/**
 * Created by gsy on 2017/6/20.
 * 接口地址配置
 */

import ossData from '~/assets/data/ossData.json'
let url={
      //登录接口
      loginUrl:'/yg-systemuser-service/login/doLogin.apec',
	  //公共参数
	  common:{
		alyserverUrl:ossData.devUrl, //aly图片服务器地址(生产地址：proUrl,开发地址：devUrl)
		alyBucket:ossData.devBucket,  //aly upload Img 参数bucket（生产参数：proBucket,开发参数：devBucket）
    	alyEndpoint:ossData.devEndpoint//aly upload Img 参数endpoint（生产参数：proEndpoint,开发参数：devEndpoint）
    },
      //会员实名认证审核
      certificate:{
           tableUrl:'/yg-user-service/user/pageUserRealNameRecord.apec', //实名认证记录查询（分页）
           approvalUrl:'/yg-user-service/user/userRealNameEntrue.apec'//实名认证审核
      },
      //客户
      member:{
        tableUrl:'/yg-user-service/user/pageUserInfo.apec',//客户列表
        regionUrl:'/yg-systemConfig-service/regionLevel/listRegionLevel.apec',//查询地区信息
        tableDetailUrl:'/yg-user-service/user/findUserInfo.apec',//table详情
        editTableUrl:'/yg-user-service/user/updateUserInfo.apec',//table编辑
        delTableUrl:'/yg-user-service/user/deleteUserInfo.apec',//table禁用/启用
        delMoretableUrl:'/yg-user-service/user/deleteUserInfoList.apec',//table批量删除
        UserAndOrgUrl:'/yg-user-service/user/pushUserAndOrg.apec',//推送组织和设置用户类型,认证开关
        orgtableUrl:'/yg-user-service/user/pageUserOrg.apec', //分页查询组织信息
        unbindUrl:'/yg-user-service/user/unBoundOrg.apec', //客户类型解绑
        cancelCertUrl:'/yg-user-service/user/closeOrgPushFlag.apec',//取消认证
        getInfo: '/yg-systemConfig-service/userTypeMenu/findMenuByUserType.apec',//获取企业需填充字段
        getRegion: '/yg-systemConfig-service/regionLevel/listRegionLevel.apec',//获取区域
        checkPhone: '/yg-user-service/user/isHasMobile.apec',//验证手机是否注册
        authen: '/yg-user-service/user/batchAuthen.apec',//认证
        export: '/yg-user-service/user/exportExcel.xls',//导出
        create: '/yg-user-service/user/addNewUserLast.apec',//新增客户（后台代注册）
        invitedUsers: '/yg-user-service/user/queryInviteUserInfo.apec'//受邀用户
      },
      //企业
      company:{
      	tableUrl:'/yg-user-service/user/pageUserOrg.apec',//列表
      	tableDetailUrl:'/yg-user-service/user/findUserOrgInfo.apec',//table详情
      	export: '/yg-user-service/user/exportExcelOrg.xls',//导出
      	setTagUrl:'/yg-user-service/user/setOrgTags.apec'//设置标签
      },
     //交易
	    transaction:{
	        uploadImgUrl:'/common/uploadImg.apec',//上传图片(old ftp)
	        authoriseUrl:'/_node_image/_oauth.apno',//上传图片授权(new aliyun)
		    //uploadImg:'/dd/authorise.apos', //阿里云
	        addNewsUrl:'/yg-society-service/societyPost/addSocietyPost.apec',//新增行情
	        tableUrl:'/yg-society-service/societyPost/societyPostPage.apec',//查询列表
	        tableDetailUrl:'/yg-society-service/societyPost/findSocietyPostById.apec',//查询列表详情
	        editNewsUrl:'/yg-society-service/societyPost/updateSocietyPostById.apec',//编辑行情
	        delNewsUrl:'/yg-society-service/societyPost/deleteSocietyPostById.apec',//删除行情
	        auditUrl:'/yg-society-service/societyPost/articleReview.apec', //行情审核
	        setTopUrl:'/yg-society-service/societyPost/stickSocietyPost.apec',//设置置顶
	        cancelTopUrl:'/yg-society-service/societyPost/closeStickArticle.apec',//取消置顶
	        moments: '/yg-society-service/societyPostReply/findSocietyPostReplyPage.apec',//获取评论
	        delMonment: '/yg-society-service/societyPostReply/deleteSocietyPostReply.apec',//删除评论
	        delSubMonment: '/yg-society-service/societyLzlReply/deleteSocietyLzlReply.apec',//删除楼中楼评论
	        reply: '/yg-society-service/societyLzlReply/addSocietyLzlReply.apec'
	    },
	    //交收单
	    settlement :{
	    	tableUrl:'/yg-voucher-service/voucher/getVoucherBSViewVO.apec',//查询列表
	    	deletUrl:'/yg-voucher-service/voucher/deleteBSVoucherInfo.apec',//删除交收单
	    	create: '/yg-voucher-service/voucher/addVoucherInfo/uploadPicture.apec',//新增交收单
	    	approve: '/yg-voucher-service/voucher/voucherReview.apec',//审核
	    	tableDetailUrl:'/yg-voucher-service/voucher/getVoucherInfoById.apec',//详情
	    	statistic: '/yg-voucher-service/voucher/getVoucherBSSumWeight.apec',//统计
	    	statisticReport: '/yg-voucher-service/voucher/staticVoucherInfo.apec'//统计报表
	    },
	    //供求管理
	    supplyDemand :{
	    	tableUrl:'/yg-product-service/product/productPage.apec',//查询列表
	    	tableDetailUrl:'/yg-product-service/product/productInfoByEsId.apec',//详情
	    	offSellUrl:'/yg-product-service/product/offSellProductByManager.apec',//下架
	    	getUserUrl:'/yg-user-service/user/listUserInfo.apec',//查询所有客户
	    	getAttrUrl:'/yg-goods-service/goods/getGoods.apec',//获取属性集合
	    	setAttrUrl:'/yg-product-service/product/addProductAttr.apec',//添加属性
	    	addUrl:'/yg-product-service/product/addNewProduct.apec',
	    	export: '/yg-product-service/product/exportExcel.xls',//导出
	    	setTag: '/yg-product-service/product/setProductTags.apec',//设置标签
	    	uploadUrl:'/common/uploadImg.apec',
	    	stickTop: '/yg-product-service/product/stickProductInfo.apec',//置顶
	    	cancelStickTop: '/yg-product-service/product/closeStickProductInfo.apec'//取消置顶s
	    },
	    //价格竞猜
	    guessing: {
	    	retrieve: '/yg-society-service/societyPost/newsQuoPage.apec',//竞猜列表
	    	userInfo: '/yg-user-service/user/queryQuotationUser.apec',//查询竞猜用户信息
	    	create: '/yg-society-service/societyPost/addQuotation.apec',//新增竞猜
	    	comment: '/yg-society-service/societyPostReply/findSocietyPostReplyPage.apec',//评论列表
	        delMonment: '/yg-society-service/societyPostReply/deleteSocietyPostReply.apec',//删除评论
	        delSubMonment: '/yg-society-service/societyLzlReply/deleteSocietyLzlReply.apec',//删除楼中楼评论
	        outLine: '/yg-society-service/societyPost/outLineQuotation.apec',//下线
	        judge: '/yg-society-service/societyPost/quotationReview.apec',//评定结果
	        edit: '/yg-society-service/societyPost/updateQuotation.apec',//修改信息
	        del: '/yg-society-service/societyPost/deleteSocietyPostById.apec',//删除
	    },
	    //举报
	    whistleBlowing: {
	    	retrieve: '/yg-systemConfig-service/feedBack/queryFeedBackPage.apec',//举报列表
	    	detail: '/yg-systemConfig-service/feedBack/queryFeedBackInfo.apec',//详情
	    	del: '/yg-systemConfig-service/feedBack/deleteFeedBackInfo.apec',//删除反馈信息
	    	delSum: '/yg-systemConfig-service/feedBack/deleteFeedBackList.apec',//批量删除反馈信息
	    },
	    //果圈动态
	    moments: {
	    	retrieve: '/yg-society-service/societyPost/societyPostPage.apec',//查询列表
	    	create: '/yg-society-service/societyPost/societyPostPage.apec',//发帖
	    	export: '/yg-society-service/societyPost/exportExcel.xls'//导出
	    },
	    //客户积分
	    customerIntegral: {
	    	retrieve: '/yg-user-service/userpoint/pageUserPoints.apec',//积分列表
	    	records: '/yg-user-service/userpoint/pageUserPointRecords.apec',//积分流水，记录
	    	update: '/yg-user-service/userpoint/reducePoint.apec',//新增竞猜
	    	pointRules: '/yg-user-service/userpoint/listUserPointRule.apec'//积分规则
	    },
	    //积分等级
	    level: {
	    	retrieve: '/yg-user-service/userpoint/pageUserLevelRules.apec',//查询列表
	    	update: '/yg-user-service/userpoint/updateUserLevelRule.apec',//编辑和禁用
	    },
	    //积分规则
	    rules: {
	    	retrieve: '/yg-user-service/userpoint/pageUserPointRule.apec',//列表
	    	update: '/yg-user-service/userpoint/updateRulePoints.apec',//修改积分规则
	    	del: '/yg-user-service/userpoint/deleteRulePoints.apec',//删除
	    	create: '/yg-user-service/userpoint/addRulePoints.apec'//新增
	    },
	    //排行榜
	    rank: {
	    	retrieve: '/yg-voucher-service/ranking/rankingPage.apec',//列表
	    	create: '/yg-voucher-service/ranking/addRankingInfo.apec',//新增
	    	out: '/yg-voucher-service/ranking/outlineRankingInfo.apec',//下线
	    	detail: '/yg-voucher-service/ranking/rankingInfo.apec',//详情
	    	member: '/yg-voucher-service/ranking/queryUserInfoForRanking.apec',//上榜人员列表
	    	update: '/yg-voucher-service/ranking/updateRankingInfo.apec'//修改
	    },
	    //广告
	    advertising: {
    		retrieve: '/yg-cms-service/cms/newsList.apec',//列表
    		category: '/yg-cms-service/cms/channelList.apec',//栏目列表
    		create: '/yg-cms-service/cms/articleCreate.apec',//新增
	    	update: '/yg-cms-service/cms/articleUpdate.apec',//更新
    		del: '/yg-cms-service/cms/articleDelete.apec',//删除
	    },
	    //抽奖
	    lottery: {
	    	retrieve: '/yg-game-service/lottery/lotteryPage.apec',//列表
	    	form: '/yg-game-service/lottery/lotteryFormList.apec',//查询抽奖形式
	    	condition: '/yg-game-service/lottery/participationConditionList.apec',//抽奖条件
	    	out: '/yg-game-service/lottery/outlineLottery.apec',//下线
	    	updateTips: '/yg-game-service/lottery/updateLotteryHoint.apec',//修改抽奖提示
	    	//抽奖活动
	    	create: '/yg-game-service/lottery/addLottery.apec',//新增
	    	createUpdate: '/yg-game-service/lottery/updateLottery.apec',//修改
	    	updateTips: '/yg-game-service/lottery/updateLotteryHoint.apec',//修改抽奖提示
	    	delTips: '/yg-game-service/lottery/deleteLotteryHoint.apec',//删除抽奖提示
	    	gameDetail: '/yg-game-service/lottery/lotteryInfo.apec',//详情
	    	//活动规则
	    	createRule: '/yg-game-service/lottery/addActiveRule.apec',//新增活动规则
	    	updateRule: '/yg-game-service/lottery/updateActiveRule.apec',//新增活动规则
	    	ruleDetail: '/yg-game-service/lottery/activeRuleInfoOfLottery.apec',//活动规则详情
	    	//奖品设置
	    	createPrize: '/yg-game-service/lottery/addLotteryPrize.apec',//新增奖品设置
	    	prizeRetrieve: '/yg-game-service/lottery/lotteryPrizePage.apec',//查奖品列表
	    	prizeUpdate: '/yg-game-service/lottery/updateLotteryPrize.apec',//修改奖品
	    	prizeDel: '/yg-game-service/lottery/deleteLotteryPrize.apec',//删除奖品
	    	prizeDetail: '/yg-game-service/lottery/lotteryPrizeInfo.apec',//奖品详情
	    	//中奖统计
	    	statistics: '/yg-game-service/lottery/lotteryPeoplePage.apec',//统计列表
	    	statisticsPopulation: '/yg-game-service/lottery/countLotteryPeople.apec',//统计人数
	    	
	    	del: '/yg-game-service/lottery/deleteLottery.apec',//删除
	    },
	    //商品分类
	    category: {
	    	retrieve: '/yg-goods-service/goods/getGoodsPage.apec',//列表
	    	create: '/yg-goods-service/goods/addGoods.apec',//新增属性
	    	update: '/yg-goods-service/goods/updateGoods.apec',//商品品类修改
	    	attrList: '/yg-goods-service/attributeName/getAttributeNamePage.apec',//属性列表
	    	asc: '/yg-goods-service/goodsAttr/riseGoodsAttrSort.apec',//升序
	    	del: '/yg-goods-service/goods/removeGoods.apec',//删除
	    	detail: '/yg-goods-service/goods/getGoods.apec',//详情
	    	desc: '/yg-goods-service/goodsAttr/downGoodsAttrSort.apec'//降序
	    },
	    //商品属性
	    attribute: {
	    	retrieve: '/yg-goods-service/attributeName/getAttributeNamePage.apec',//列表
	    	create: '/yg-goods-service/attributeName/saveAttributeName.apec',//新增属性
	    	attrCreate: '/yg-goods-service/attributeValue/saveAttributeValue.apec',//新增属性
	    	del: '/yg-goods-service/attributeName/removeAttributeName.apec',//删除
	    	attrRetrieve: '/yg-goods-service/attributeValue/findAttributeValue.apec',//属性值查询
	    	attrValueDel: '/yg-goods-service/attributeValue/removeAttributeValue.apec',//属性值
	    	detail: '/yg-goods-service/attributeName/findAttributeName.apec',//查属性值
	    	update: '/yg-goods-service/attributeName/updateAttributeName.apec'//修改
	    }
}
export default url;
