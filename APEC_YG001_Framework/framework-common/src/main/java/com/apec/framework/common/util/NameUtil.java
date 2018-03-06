package com.apec.framework.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Title: 姓名处理
 *
 * @author yirde  2017/7/17.
 */
public class NameUtil {

    private final static Map<String, Integer> MAP = new HashMap<>();

    private final static int FU_FIRST_NAME_LENGTH = 2;

    static {
        MAP.put("万俟",1);MAP.put("东方",1);MAP.put("子车",1);MAP.put("皇甫",1);
        MAP.put("司马",1);MAP.put("赫连",1);MAP.put("漆雕",1);MAP.put("宗政",1);
        MAP.put("上官",1);MAP.put("尉迟",1);MAP.put("夹谷",1);MAP.put("申屠",1);
        MAP.put("欧阳",1);MAP.put("濮阳",1);MAP.put("东郭",1);MAP.put("锺离",1);
        MAP.put("夏侯",1);MAP.put("公孙",1);MAP.put("梁丘",1);MAP.put("闾丘",1);
        MAP.put("诸葛",1);MAP.put("宇文",1);MAP.put("公羊",1);MAP.put("南宫",1);
        MAP.put("闻人",1);MAP.put("司徒",1);MAP.put("淳于",1);MAP.put("公西",1);
        MAP.put("仲孙",1);MAP.put("澹台",1);MAP.put("公冶",1);MAP.put("拓拔",1);
        MAP.put("长孙",1);MAP.put("单于",1);MAP.put("太叔",1);MAP.put("百里",1);
        MAP.put("司空",1);MAP.put("轩辕",1);MAP.put("令狐",1);MAP.put("微生",1);
        MAP.put("颛孙",1);MAP.put("慕容",1);MAP.put("鲜于",1);MAP.put("西门",1);
        MAP.put("乐正",1);MAP.put("丌官",1);MAP.put("司寇",1);MAP.put("东门",1);
        MAP.put("宰父",1);MAP.put("端木",1);MAP.put("巫马",1);MAP.put("夏侯",1);
        MAP.put("南门",1);MAP.put("壤驷",1);MAP.put("公良",1);MAP.put("羊舌",1);
        MAP.put("左丘",1);MAP.put("谷梁",1);MAP.put("段干",1);MAP.put("呼延",1);
        MAP.put("夏侯",1);
    }


     public static String getFirstName(String name){
        if(StringUtils.isNotBlank(name) && StringUtils.length(name)>= FU_FIRST_NAME_LENGTH){
            if(MAP.get(name.substring(0,FU_FIRST_NAME_LENGTH)) != null ){
                return  name.substring(0,FU_FIRST_NAME_LENGTH);
            }
            return  name.substring(0,1);
        }else{
            return name;
        }
     }


}
