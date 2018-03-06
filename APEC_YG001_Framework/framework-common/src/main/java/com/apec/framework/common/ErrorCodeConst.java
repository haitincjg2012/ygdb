package com.apec.framework.common;

/**
 * 类 编 号：
 * 类 名 称：ErrorCodeConst
 * 内容摘要：错误码常量定义
 * 创建日期：2016/11/22
 * 编码作者：
 * @author xx
 */
public interface ErrorCodeConst
{
    //====================用户===================
    /**
     * 密码长度不小于六位
     */
    String ERROR_PASSWORD = "800001";

    /**
     * 验证码无效
     */
    String ERROR_VAILDCODE = "800003";

    /**
     * 用户不能为空
     */
    String USER_NOTNULL = "800004";

    /**
     * 身份证号输入有误
     */
    String ERROR_IDNUMBER = "800005";

    /**
     * 必须上传照片
     */
    String NOT_IMG = "800006";

    /**
     * 两次输入密码不一致
     */
    String PASSWORD_NOT_EQUAL = "800007";

    /**
     * 用户已经存在
     */
    String USER_IS_EXIST = "800008";

    /**
     * 您已实名认证通过，或在认证过程中
     */
    String HAD_REALNAME =  "800009";

    /**
     * 该身份证已经被用于实名认证
     */
    String IDNUMBER_ISEXIST = "800010";

    /**
     * 实名认证不可以重复审批
     */
    String CANOT_DUMBLE_ENTRUE = "800011";

    /**
     * 未传入积分规则相关信息
     */
    String NULL_POINTRULE = "800012";

    /**
     * 用户密码输入错误
     */
    String ERROR_OLDPASSWORD = "800013";

    /**
     * 手机号不合法
     */
    String ERROR_MOBILE = "800014";

    /**
     * 抱歉，非主账号不能编辑
     */
    String ERROR_ORG_CHILDACCOUNT_EDITERR = "800015";

    /**
     * ORG　为空
     */
    String ERRPR_ORG_ISNULL = "800016";

    /**
     * 该组织已经推送
     */
    String ERRPR_ORG_ISPUSH = "800017";

    /**
     * 标签不存在
     */
    String ERRPR_TAGS = "800018";

    /**
     * 该组织已经绑定主账号
     */
    String ORG_OWN_MAIN = "800019";

    /**
     * 只有客商，冷库，代办可以进行认证操作
     */
    String ORG_PUSH_USERTYPE = "800020";

    /**
     * 只有子账号能进行解绑操作
     */
    String ONLY_CHILD_UNBOUND = "800021";

    /**
     * 主账号不能绑定组织
     */
    String MAIN_CANNOT_BOUND = "800022";

    /**
     * 组织账号类型不能变为个体账号类型
     */
    String ORG_CANNOT_IDY = "800023";

    /**
     * 个体账号只能设置为组织主账号
     */
    String IDY_ONLY_MAIN = "800024";

    /**
     * 该企业已经通过认证，且只剩下这一个账号了，请将组织取消认证或是删除该组织以达到您的操作要求
     */
    String ONLY_ONE = "800025";

    /**
     * 用户信息不完整，请先完善信息
     */
    String USERINFO_IMPERFECT = "800026";

    /**
     * 组织并未推送，不需要取消
     */
    String ORG_NOT_PUSHED = "800027";

    /**
     * 企业已经认证，不允许从个人账号变为组织账号类型
     */
    String ORG_CANNOT_PUSHED = "800028";

    //=========== DISPATCH错误码start===================
    /**
     * session超时
     */
    String ERROR_600001 = "600001";

    /**
     * 没有选择上传的图片
     */
    String ERROR_600002 = "600002";

    /**
     * 文件上传失败
     */
    String ERROR_600003 = "600003";

    /**
     * 请不要重复提交
     */
    String ERROR_600004 = "600004";



    /**
     * 服务请求异常
     */
    String REQ_SERVER_EXCEPTION = "600005";

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
    /**
     * 接收者为空
     */
    String ERROR_MESSAGE_RERECEIVER_NULL = "700001";

    String ERROR_MESSAGE_SAVEMQMESSAGE_FAIL= "700002";

    /**
     * 手机号不正确
     */
    String ERROR_SMSMESSAGE_MOBILE_NOTSURE = "700003" ;

    /**
     * 发送内容不能为空
     */
    String ERROR_MESSSAGE_CONTENT_NULL="700004";
    
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
    String ERROR_ARTICLE_PRIV="410002";
    /**
     * 已审核的不能编辑
     */
    String ERROR_EDIT = "410003";

    //===========  CMS错误码end===================
    //===========  voucher相关错误码start===================

    String VOUCHER_EXCEED_NUMBER = "900001";
    String VOUCHER_EXCEED_TIMES = "900002";
    String VOUCHER_DELIVERY_TIME_PASS = "900003";
    
    //===========  voucher相关错误码End===================

    //=========== 果友圈错误码start===================

    /**
     * 该用户已经给该帖子点过赞了
     */
    String DOUBLE_LIKE = "910001";

    /**
     * 没点过赞
     */
    String NOT_LIKE = "910002";

    /**
     * 上传的图片超过允许的最多个数
     */
    String IMAGES_OUT_LENGTH = "910003";

    /**
     * 同时间内有其他的行情竞猜在线
     */
    String ERROR_TIME = "910004";

    /**
     * 时间范围编辑有误,清重新选择正确的时间
     */
    String ERROR_EDIT_TIME = "910005";

    /**
     * 该状态下不能进行该操作
     */
    String ERROR_AUDITSTATE = "910006";

    //=========== 果友圈错误码end===================


    //=========== 货源错误码start===================
    String ERROR_GMC_ISEXIST = "500100";
    String ERROR_GMC_ISNOTEXIST = "500101";
    //=========== 货源错误码end===================

}
