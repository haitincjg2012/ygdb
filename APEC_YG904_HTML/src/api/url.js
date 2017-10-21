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
        cancelCertUrl:'/yg-user-service/user/closeOrgPushFlag.apec'//取消认证
      },
      //企业
      company:{
      	tableUrl:'/yg-user-service/user/pageUserOrg.apec',//列表
      	tableDetailUrl:'/yg-user-service/user/findUserOrgInfo.apec',//table详情
      	setTagUrl:'/yg-user-service/user/setOrgTags.apec'//设置标签
      },
     //交易
	    transaction:{
	       uploadImgUrl:'/common/uploadImg.apec',//上传图片(old ftp)
	       authoriseUrl:'/_node_image/_oauth.apno',//上传图片授权(new aliyun)
		   //uploadImg:'/dd/authorise.apos', //阿里云
	       addNewsUrl:'/yg-cms-service/cms/articleCreate.apec',//新增行情
	       tableUrl:'/yg-cms-service/cms/newsList.apec',//查询列表
	       tableDetailUrl:'/yg-cms-service/cms/articleQueryById.apec',//查询列表详情
	       editNewsUrl:'/yg-cms-service/cms/articleUpdate.apec',//编辑行情
	       delNewsUrl:'/yg-cms-service/cms/articleDelete.apec'//删除行情
	    },
	    //交收单
	    settlement :{
	    	tableUrl:'/yg-voucher-service/voucher/getVoucherBSViewVO.apec',//查询列表
	    	deletUrl:'/yg-voucher-service/voucher/deleteBSVoucherInfo.apec',//删除交收单
	    	tableDetailUrl:'/yg-voucher-service/voucher/getVoucherInfoById.apec'//详情
	    },
	    //供求管理
	    supplyDemand :{
	    	tableUrl:'/_node_product/_all.apno',//查询列表
	    	tableDetailUrl:'/_node_product/_info.apno',//详情
	    	offSellUrl:'/yg-product-service/product/offSellProductByManager.apec',//下架
	    	getUserUrl:'/yg-user-service/user/listUserInfo.apec',//查询所有客户
	    	getAttrUrl:'/yg-goods-service/goods/getGoods.apec',//获取属性集合
	    	setAttrUrl:'/yg-product-service/product/addProductAttr.apec',//添加属性
	    	addUrl:'/yg-product-service/product/addNewProduct.apec',
	    	uploadUrl:'/common/uploadImg.apec'
	    }
}
export default url;
