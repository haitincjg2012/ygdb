package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

/**
 * Title: 栏目类别
 * @author yirde  2017/6/29.
 */
public enum CategoryType  implements BaseEnum {

    /**
     * 网站升级公告
     */
    NOTIFY("网站升级公告", false),

    /**
     * 首页大图
     */
    HOMEPAGE("首页大图", false),

    /**
     * 联系方式
     */
    CONTACT("联系方式", true),

    /**
     * 服务声明
     */
    DECLARATION("服务声明", true),

    /**
     * 最新公告
     */
    PUBLICATION("最新公告", false),

    /**
     * 公司简介
     */
    INTRODUCTION("公司简介", true),

    /**
     * 产品信息
     */
    PRODUCT("产品信息", false),

    /**
     * 业绩报表
     */
    REPORT("业绩报表", true),

    /**
     * 社会责任
     */
    RESPONSIBILTY("社会责任", false),

    /**
     * 合作伙伴
     */
    COOPERATION("合作伙伴", true),

    /**
     * 媒体报道
     */
    COVERAGE("媒体报道", false),

    /**
     * 行业新闻
     */
    NEWS("行业新闻", false),

    /**
     * 友情链接
     */
    LINK("友情链接", true),

    /**
     * 帮助中心
     */
    HELP("帮助中心", false),

    /**
     * 安全保障
     */
    SAFETY("安全保障", true),

    /**
     * 会员活动
     */
    ACTIVITY("会员活动", false),

    /**
     * 其他图片
     */
    IMAGE("其他图片", false),

    /**
     * 果满仓通知
     */
    WMS_NOTIFY("果满仓通知", false),

    /**
     * 其他
     */
    OTHER("其他", false);

    private final String key;

    private final boolean single;

    CategoryType(String key, boolean single) {
        this.key = key;
        this.single = single;
    }

    @Override
    public String getKey() {
        return key;
    }

    public boolean isSingle() {
        return single;
    }
}