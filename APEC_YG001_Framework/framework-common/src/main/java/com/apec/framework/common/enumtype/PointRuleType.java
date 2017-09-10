package com.apec.framework.common.enumtype;

import com.apec.framework.common.enums.BaseEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Title: 规则类型
 *
 * @author yirde  2017/6/28.
 */
public enum PointRuleType  implements BaseEnum{

    /**
     * 登录
     */
    REGISTER_LOGIN("注册登录",RuleGroup.LOGIN),
    DAY_FIRST_LOGIN("每日首次登陆",RuleGroup.LOGIN),
    DAY_LOGIN_HOUR("在线时长",RuleGroup.LOGIN),

    /**
     * 完善用户信息
     */
    VERIFIED_INFO("实名认证信息",RuleGroup.COMPLETE_INFO),
//    PROFILE_INFO("完善用户资料信息",RuleGroup.COMPLETE_INFO),

    /**
     * 上传单据
     */
//    SINGLE_ONCE_SEND_ORDER("上传单据即送",RuleGroup.SEND_ORDER),
//    MONTHLY_ONCE_SEND_ORDER("月累计上传次数每满即送",RuleGroup.SEND_ORDER),
    MEACH_SEND_ORDER("累计上传吨位每满百吨即送",RuleGroup.SEND_ORDER),

    /**
     * 发布供求
     */
    SINGLE_ONCE_SEND_REQUEST("发布需求即送",RuleGroup.SEND_REQUEST),
//    MONTHLY_ONCE_SEND_REQUEST("月累计发布次数每满即送",RuleGroup.SEND_REQUEST),

    /**
     * 每天签到
     */
    SINGLE_ONE_SIGN_IN("每天签到",RuleGroup.SIGN_IN),


    /**
     * 推广
     */
    SHARE_PROMOTION("分享推广",RuleGroup.PROMOTION),


    /**分界线：分界线以上的为积分规则，分界线已下的为积分消耗规则**/

    /**
     * 兑换
     */
//    EXCHANGE_PRODUCT("积分兑换商品",RuleGroup.EXCHANGE),

    /**
     * 抽奖
     */
//    LOTTERY_PRODUCT("抽奖兑换商品",RuleGroup.LOTTERY),

    /**
     * 触发条件
     */
//    TRIGGER_DISABLED("触发条件用户禁用",RuleGroup.TRIGGER),

    /**
     * 处罚用户，扣减积分
     */
    GOODS_NOT_AGREE_FACT("货物品质与价格描述跟事实严重不符",RuleGroup.PUNISHMENT),
    EMBEZZLE_OTHERS_INFOMATION("盗用其他用户资质，货品介绍，货品图片等图片，视频信息",RuleGroup.PUNISHMENT),
    DEFAME_OTHERS_GOODS("恶意诋毁他人货品",RuleGroup.PUNISHMENT),
    CUMULATIVE_ASSESSMENT("差评累积",RuleGroup.PUNISHMENT),
    USER_REPORT("举报",RuleGroup.PUNISHMENT),
    PUBLISH_FALSE_INFORMATION("恶意发布无关信息、虚假信息、虚假单据等",RuleGroup.PUNISHMENT);

    private final String key;
    private final RuleGroup group;

    //规则组+规则S 一次初始化 为常亮
    private static final Map<RuleGroup, List<PointRuleType>> ruleMap = new HashMap<>();
    //规则map
    private static final Map<String, PointRuleType> keyMap = new HashMap<>();

    private PointRuleType(String key,RuleGroup group){
        this.key = key;
        this.group = group;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    public RuleGroup getGroup() {
        return this.group;
    }
    static {
        for (RuleGroup group : RuleGroup.values()) {
            ruleMap.put(group, new ArrayList<PointRuleType>());
        }

        for (PointRuleType rule : PointRuleType.values()) {
            ruleMap.get(rule.getGroup()).add(rule);

            //key 和 rule 一一对应
            keyMap.put(rule.getKey(), rule);
        }
    }

    /**
     * 根据规则名 获取所有的规则描述
     * @param RuleGroup
     * @return
     */
    public static List<PointRuleType> listByGroup(RuleGroup RuleGroup) {
        return ruleMap.get(RuleGroup);
    }

    /**
     * 根据规则描述的键获得规则描述
     *
     * @param key
     * @return
     */
    public static PointRuleType byKey(String key) {
        return keyMap.get(key);
    }
}
