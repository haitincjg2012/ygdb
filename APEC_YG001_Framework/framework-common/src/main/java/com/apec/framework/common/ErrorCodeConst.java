package com.apec.framework.common;

/**
 * 类 编 号：
 * 类 名 称：ErrorCodeConst
 * 内容摘要：错误码常量定义
 * 创建日期：2016/11/22
 * 编码作者：
 */
public interface ErrorCodeConst
{
    //====================用户===================
    String ERROR_PASSWORD = "800001";//密码长度不小于六位
    String ERROR_VAILDCODE = "800003";//验证码无效
    String USER_NOTNULL = "800004";//用户不能为空
    String ERROR_IDNUMBER = "800005";//身份证号输入有误
    String NOT_IMG = "800006";//必须上传照片
    String PASSWORD_NOT_EQUAL = "800007";//两次输入密码不一致
    String USER_IS_EXIST = "800008";//用户已经存在
    String HAD_REALNAME =  "800009";//您已实名认证通过，或在认证过程中
    String IDNUMBER_ISEXIST = "800010";//该身份证已经被用于实名认证
    String CANOT_DUMBLE_ENTRUE = "800011";//实名认证不可以重复审批
    String NULL_POINTRULE = "800012";//未传入积分规则相关信息
    String ERROR_OLDPASSWORD = "800013";//用户密码输入错误
    String ERROR_MOBILE = "800014";//手机号不合法
    String ERROR_ORG_CHILDACCOUNT_EDITERR = "800015";//抱歉，非主账号不能编辑

    //=========== DISPATCH错误码start===================
    String ERROR_600001 = "600001";//session超时
    String ERROR_600002 = "600002";//没有选择上传的图片
    String ERROR_600003 = "600003";//文件上传失败
    String ERROR_600004 = "600004";//请不要重复提交
    String REQ_SERVER_EXCEPTION = "600005";//服务请求异常
    String ERROR_FTP_CONNECT_FAILD = "600006";
    String ERROR_FTP_LOGIN_FAILD = "600007";
    String ERROR_FTP_FILETYPE_FAILD = "600008";
    String ERROR_FTP_WORKINGDIR_FAILD = "600009";
    String ERROR_FTP_UPLOAD_FAILD = "600010";
    String ERROR_FTP_DOWNLOAD_FAILD = "600011";
    //=========== DISPATCH错误码end===================
    //=========== ftp上传错误码start===================

    //=========== ftp上传错误码end===================
    //=========== 用户相关错误码start===================
    String ERROR_USER_LOGIN_FAILED = "200001";
    String ERROR_USER_LOGIN_COUNT_OUT = "200002";
    String ERROR_USER_LOGIN_LOCK = "200003";
    String ERROR_USER_INFO_ISNULL = "200004";
    String PASSWORD_ERROR = "200005";
    String USER_NOT_EXIST_ERROR = "200006";

    //===========  用户相关错误码end===================

    //===========  Message相关错误码start===================
    String ERROR_MESSAGE_RERECEIVER_NULL = "700001"; //接收者为空
    String ERROR_MESSAGE_SAVEMQMESSAGE_FAIL= "700002";
    String ERROR_SMSMESSAGE_MOBILE_NOTSURE = "700003" ; //手机号不正确
    String ERROR_MESSSAGE_CONTENT_NULL="700004";//发送内容不能为空
    
    //===========  Message相关错误码End===================

    //===========  MQMessage相关错误码Start===================
    String  ERROR_MQ_CONSUMER_FAIL="400002";

    //===========  MQMessage相关错误码End===================
    //===========  Product相关错误码Start===================
    String ERROR_PRODUCT_CREATEPRO_OVER = "500001";
    String ERROR_PRODUCT_CREATEATTR_CONFIG = "500002";
    String ERROR_PRODUCT_NOT_FOUND = "500003";
    String ERROR_PRODUCT_AMOUNT_RANGE = "500005";


    //===========  Product相关错误码End===================
    //===========  CMS错误码Start===================
    String ERROR_CMS_CHANNELCODE_EXISTS="410001";

    //===========  CMS错误码end===================
    //===========  voucher相关错误码start===================
    String VOUCHER_EXCEED_NUMBER = "900001";
    String VOUCHER_EXCEED_TIMES = "900002";
    
    //===========  voucher相关错误码End===================
}
