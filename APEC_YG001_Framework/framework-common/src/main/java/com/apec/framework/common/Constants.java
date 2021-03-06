/**
 * 版权所有：版权所有(C) 2016，中农网
 * 文件编号：BL_PU6020301_Constants
 * 文件名称：Constants.java
 * 系统编号：
 * 系统名称：
 * 组件编号：ICT_CJ002
 * 组件名称：
 * 设计作者：
 * 完成日期：2016-07-14
 * 设计文档：
 * 内容摘要：框架常量定义
 */
package com.apec.framework.common;

import java.nio.charset.Charset;

/**
 * 类 编 号：BL_PU1010202_PageJSON
 * 类 名 称：Constants
 * 内容摘要：常量定义
 * 完成日期：2016-07-14
 * 编码作者：
 * @author xxx
 */
public interface Constants
{
    /** -----通用定义开始(从100001到100999)------*/
    /**
     * // 通用成功标识
     */
    String RETURN_SUCESS = "100001";

    /**
     * // 系統异常
     */
    String SYS_ERROR = "100002";

    /**
     * //参数不能为空
     */
    String ERROR_100003 = "100003";

    /**
     * //数据执行错误
     */
    String ERROR_100004 = "100004";

    //非法请求
    String ERROR_100008 = "100008";

    /**
     * //服务调用服务异常
     */
    String SERVER_RESEST_EXCEPTION = "100061";

    String ENABLE_NOT_NULL = "100011";

    /**
     * //主键不能为空
     */
    String ID_NOTNULL = "100012";

    String REPEAT_DO_ERROR = "100006";

    String DATA_ISNULL = "100007";

    /**
     * // 数据类型匹配异常错误码
     */
    String ERROR_CODE_DATA_TYPE_NO_MATCH = "100009";

    /**
     * // 时间类型匹配异常错误码
     */
    String DATE_TYPE_NO_MATCH = "100010";

    /**
     * // 参数不完整
     */
    String COMMON_MISSING_PARAMS = "100021";

    /**
     * // 参数格式错误
     */
    String COMMON_ERROR_FORMAT_PARAMS = "100022";

    /**
     * //参数错误
     */
    String COMMON_ERROR_PARAMS = "100023";
    
    String COMMON_IS_EXIST = "100024";


    /**
     * //账号密码错误
     */
    String PASSWORD_ERROR = "200001";
    String USER_NOT_EXIST_ERROR = "200002";

    /**
     * //DAO操作异常
     */
    String DAO_EXCEPTION = "100030";
    String DAO_SAVE_EXCEPTION = "100031";
    String DAO_DELETE_EXCEPTION = "100032";
    String DAO_UPDATE_EXCEPTION = "100033";
    String DAO_GET_EXCEPTION = "100034";

    String SAVE_EXCEPTION = "100041";
    String DELETE_EXCEPTION = "100042";
    String UPDATE_EXCEPTION = "100043";
    String GET_EXCEPTION = "100044";

    String PROCEDURE_EXCEPTION = "100045";

    //==================================CACHE操作异常================

    String CACHE_EXCEPTION = "100050";
    String CACHE_SAVE_EXCEPTION = "100051";
    String CACHE_DELETE_EXCEPTION = "100052";
    String CACHE_UPDATE_EXCEPTION = "100053";
    String CACHE_GET_EXCEPTION = "100054";

    //======================RocketMq ========

    String DEFAULT_ROCKETMQ_TOPIC="YIGUO_TOPIC";


    //=========== 客户信息错误码===================

    /**
     * //客户不在同一维度
     */
    String CUSTOMER_NOT_DIMENSION = "300001";

    /**
     * //客户名称不能为null
     */
    String CUSTOMER_NAME_NOTNULL = "300002";

    /**
     * //客户类型不能为null
     */
    String CUSTOMER_TYPE_DIMENSION = "300003";

    /**
     * //客户编号
     */
    String CUSTOMER_NO_NOTNULL = "300004";

    /**
     * //根据客户编号查询的客户为空
     */
    String CUSTOMER_NOT_EXIST = "300005";
    String CUSTOMER_NOT_EXIST_BYUSERNO =  "300006";
    //=========== 客户信息错误码===================

    //============== ES错误码==================

    /**
     * 索引新建
     */
    String ERROR_ES_SINGLE_CREATE_INDEX = "700001";

    /**
     * 索引批量新建
     */
    String ERROR_ES_BATCH_CREATE_INDEX = "700002";

    /**
     * 索引删除
     */
    String ERROR_ES_SINGLE_DELETE_INDEX = "700011";

    /**
     * 索引批量删除
     */
    String ERROR_ES_BATCH_DELETE_INDEX = "700012";

    /**
     * 索引更新
     */
    String ERROR_ES_SINGLE_UPDATE_INDEX = "700021";

    /**
     * 索引批量更新
     */
    String ERROR_ES_BATCH_UPDATE_INDEX = "700022";

    /**
     * 统计
     */
    String ERROR_ES_COUNT_SEARCH_INDEX = "700041";

    String ERROR_ES_COUNT_SEARCH_BY_ID = "700042";


    String WORKER_ID = "workerId";

    String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    String MAX_CONNECTIONS_PERHOST = "maxConnectionsPerHost";

    String MAX_TOTAL_CONNECTIONS = "maxTotalConnections";

    String SYSTEM_GENERATOR = "system_generator ";

    String IDENTITY = "identity";

    /**
     * 自定义ID
     */
    String ASSIGNED = "assigned";

    String UUID = "uuid";

    /**
     * 角色搜索限定
     */
    String ROLE_SEARCH_LIMIT = "limit";

    String USER_ID = "userId";

    /**
     * 角色拥有索引库集合
     */
    String ROLE_SEARCH_INDEX = "indexList";

    String DEFAULT_LANG = "zh-CN";

    String EN_DEFAULT_LANG = "en-US";

    String POST = "POST";

    String GET = "GET";

    String HTTP_COLON = "http:";

    String APPLICATION_JSON = "application/json";

    String DOUBLE_SLASH = "//";

    String SINGLE_SLASH = "/";

    String DOT = ".";

    String SYSTEM_ENCODING = "UTF-8";

    String QUESTION_MARK = "?";

    String COMMA = ",";

    String UNDERLINE = "_";

    Charset SYSTEM_CHARSET = Charset.forName( Constants.SYSTEM_ENCODING );

    String SESSION_ID = "sessionId";

    String SOURCE= "source";

    String SESSION_IP = "ip";

    String TOKEN = "token";

    /**
     * 用户编号
     */
    String USER_NO = "userNo";

    String ORG_CODE = "orgCode";

    String DEFAULT_GOODS_NAME = "苹果";

    /**
     * 图片属性集合
     */
    String IMAGE_ITEMS = "imageItems";

    /**
     * 页面传过来的缩略图的长,宽
     */
    String TINY = "tiny";

    // 缓存公用key
    /**************************** 客户 ***************************/
    /**
     * 统一前缀
     */
    String CACHE_CUSTOMER_PREFIX = "customer_";

    /**
     * 单个客户对象：id--->goods object
     */
    String CACHE_CUSTOMER_ITEM = CACHE_CUSTOMER_PREFIX + "item";

    /**************************** 客户联系人 ***************************/
    /**
     * 统一前缀
     */
    String CACHE_CONTACT_PREFIX = "contact_";
    /**
     * 单个客户对象：id--->goods object
     */
    String CACHE_CONTACT_ITEM = CACHE_CONTACT_PREFIX + "item";

    /**********************用户*******************************/

    String USER_INFO = "userInfo";

    String CACHE_USERINFO_PREFIX = "userInfo_";

    String CACHE_CAPTCHA_PREFIX = "captcha_";

    String CACHE_USERINFO_ITEM = CACHE_USERINFO_PREFIX + "item";


    String CACHE_USERDETAIL_PREFIX = "userDetail_";

    String CACHE_USERDETAIL_ITEM = CACHE_USERDETAIL_PREFIX + "item";


    String CACHE_USRDEPARTMENT_PREFIX = "usrDepartment_";

    String CACHE_USRDEPARTMENT_ITEM = CACHE_USRDEPARTMENT_PREFIX + "item";

    String CLIENT_TYPE_PARAM = "cc";

    String ANDROID = "android";

    String H5 = "H5";

    String UA = "UA";

    String IMEI = "IMEI";

    String ID = "id";

    String WEB = "web";

    String IOS = "ios";

    String EMP_STR = " ";

    /**
     * 客户端重复提交参数
     */
    String CLIENT_DUPLICATE_ACT_PARAM = "_d";

    /**
     * 重复提交缓存前缀
     */
    /**
     * 重复提交键的前缀
     */
    String PREFIX_REPEAT = "repeat_";

    String REPEAT_UUID = "repeat_uuid";
    String REPEAT_KEY = "repeatKey";

    /*------------iweb中分页定义开始--------------*/
    /**
     * 默认每页显示记录数
     */
    int DEFAULT_FETCH_SIZE = 10;

    /**
     * 默认起始页
     */
    int DEFAULT_START_PAGE = 1;

    /**
     * 默认每页显示记录数
     */
    String DEFAULT_FETCHSIZE = "10";

    /**
     * 每页最大长度
     */
    String MAX_FETCHSIZE = "1000";

    /**
     * 默认起始页
     */
    String DEFAULT_RANGESTART = "1";
    /*------------iweb中分页定义结束------------*/

    /********************* 客户拜访 ***********************/
    /**
     * 编号
     */
    String VISIT_RECORD_NO_PREFIX = "VR";
    /**
     * 缓存前缀
     */
    String CACHE_VISIT_RECORD_PREFIX = "visitRecord_";
    /**
     * 单个对象 key:CACHE_VISIT_RECORD_ITEM + NO
     */
    String CACHE_VISIT_RECORD_ITEM = CACHE_VISIT_RECORD_PREFIX + "item_";

    /********************* 角色菜单 ***********************/
    /**
     * 缓存前缀
     */
    String CACHE_ROLE_MENU_PREFIX = "roleMenu_";
    /**
     * 单个对象 key:CACHE_ROLE_MENU_ITEM_PREFIX + NO; value:Object
     */
    String CACHE_ROLE_MENU_ITEM_PREFIX = CACHE_ROLE_MENU_PREFIX + "item_";
    /**
     * 用户角色对应的菜单树 key:CACHE_ROLE_MENU_TREE_PREFIX + roleNO; value:menu tree
     */
    String CACHE_ROLE_MENU_TREE_PREFIX = CACHE_ROLE_MENU_PREFIX + "tree_";

    /********************* 地区 ***********************/
    /**
     * 缓存前缀
     */
    String CACHE_REGION_PREFIX = "region_";
    /**
     * 单个对象 key:CACHE_REGION_ITEM_PREFIX + ID; value:Object
     */
    String CACHE_REGION_ITEM_PREFIX = CACHE_REGION_PREFIX + "item_";
    /**
     * 地区树 key:CACHE_REGION_TREE_PREFIX + id; value:region tree
     */
    String CACHE_REGION_TREE_PREFIX = CACHE_REGION_PREFIX + "tree_";
    /**
     * 子地区 key:CACHE_REGION_SON_LIST_PREFIX + pid
     */
    String CACHE_REGION_SON_LIST_PREFIX = CACHE_REGION_PREFIX + "sonList_";

    /********************* 目录 ***********************/

    /**
     * 目录前缀
     */
    String CACHE_MENU_PREFIX = "menu_";

    String CACHE_MENU_ITEM_PREFIX = CACHE_MENU_PREFIX + "item_";

    /**************************** 物流 ***************************/
    /**
     * 统一前缀
     */
    String CACHE_LOGISTICS_ORDER_PREFIX = "logistics_order";

    /**
     * 单个对象：id
     */
    String CACHE_LOGISTICS_ORDER_ITEM = CACHE_LOGISTICS_ORDER_PREFIX + "wxscitem_logistics_order";

    /**
     * 物流详情统一前缀
     */
    String CACHE_LOGISTICS_DETAIL_PREFIX = "logistics_detail";

    /**
     * 单个对象：
     */
    String CACHE_LOGISTICS_DETAIL_ITEM = CACHE_LOGISTICS_DETAIL_PREFIX + "wxscitem_";

    /********************* 图片 ***********************/
    /**
     * 编号
     */
    String IMAGE_NO_PREFIX = "IMG";

    String CACHE_IMAGE_PREFIX = "image_";

    String CACHE_IMAGE_ITEM_PREFIX = CACHE_IMAGE_PREFIX + "item_";

    /********************* 购物车 ***********************/

    /**
     * Lock异常
     */
    String LOCK_EXCEPTION = "100060";
    String LOCK_EXISTS_EXCEPTION = "100061";
    String LOCK_NOT_HELD_EXCEPTION = "100062";
    String NO_SUCK_LOCK_EXCEPTION = "100063";
    /**
     * 锁前缀
     */
    String DEFAULT_LOCK_PREFIX = "apec.lock.";

    /**
     * 加鹽常量
     */
    String SALT = "YIGUO_DAIBAN";


    /***********************积分****************************/
    String DEDUCT_POINT_EXCEED = "扣除的积分超过拥有的积分，执行清零操作";




    /************************成功**************************/
    String ISSUCCESS = "Y";
    String ISNOTSUCCESS = "N";

    /*************************提示信息*****************************/

    /*************************上传凭据*****************************/
    /**
     * 上传凭据缓存前缀
     */
    String VOUCHER = "voucher_";
    String VOUCHER_NOT_EXCEED = "上传累加没满100吨";
    /**
     * 上传单据每满100吨即200000斤加积分
     */
    String VOUCHER_POINT_WEIGHT = "200000";
    /**
     * 上传总数量缓存前缀
     */
    String TOTALNUMBER = "totalNumber_";
    
    /********************站内信发送人为系统**********************/
    String MESSAGE_SENDER_SYSTEM = "messageSenderSystem";

    /************************字典表相关数据编码*****************************/
    String FORMAT_CODE = "1001";
    String VARIETY_CODE = "1002";
    String AREA_CODE = "1003";

    /**
     * 用户密码的最小长度
     */
    String PASSWORD_MIN_LENGTH = "6";

    /**
     * 发布帖子时上传图片的最多个数
     */
    String POST_IMAGES_LENGTH = "6";

    String ZERO = "0";

    String DEFAULT_PASSWORD = "000000";

    String SYSTEM_ADMIN = "易果小辛";

    /**
     * 未完善用户信息可以提示的总次数
     */
    Integer TIP_NUM = 3;

}
