/**
 * Created by gsy on 2017/6/20.
 * 公共配置
 */

module.exports={
      //登录接口
      loginUrl:'/yg-systemuser-service/login/doLogin.apec',
      //会员实名认证审核
      certificate:{
           tableUrl:'/yg-user-service/user/pageUserRealNameRecord.apec', //实名认证记录查询（分页）
           approvalUrl:'/yg-user-service/user/userRealNameEntrue.apec'//实名认证审核
      }


}


