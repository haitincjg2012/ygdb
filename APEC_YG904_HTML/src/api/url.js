/**
 * Created by gsy on 2017/6/20.
 * 接口配置
 */

let url={
      //登录接口
      loginUrl:'/yg-systemuser-service/login/doLogin.apec',
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
        orgtableUrl:'/yg-user-service/user/pageUserOrg.apec' //分页查询组织信息
      },
     //交易
    transaction:{
       uploadImgUrl:'/common/uploadImg.apec',//上传图片
       addNewsUrl:'/yg-cms-service/cms/articleCreate.apec',//新增行情
       tableUrl:'/yg-cms-service/cms/newsList.apec',//查询列表
       tableDetailUrl:'/yg-cms-service/cms/articleQueryById.apec',//查询列表详情
       editNewsUrl:'/yg-cms-service/cms/articleUpdate.apec',//编辑行情
       delNewsUrl:'/yg-cms-service/cms/articleDelete.apec'//删除行情
    }



}
export default url;


