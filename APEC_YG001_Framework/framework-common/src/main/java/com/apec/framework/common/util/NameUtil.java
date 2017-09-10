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

    private final static Map<String, Integer> map = new HashMap<>();

    static {
        map.put("万俟",1);map.put("东方",1);map.put("子车",1);map.put("皇甫",1);
        map.put("司马",1);map.put("赫连",1);map.put("漆雕",1);map.put("宗政",1);
        map.put("上官",1);map.put("尉迟",1);map.put("夹谷",1);map.put("申屠",1);
        map.put("欧阳",1);map.put("濮阳",1);map.put("东郭",1);map.put("锺离",1);
        map.put("夏侯",1);map.put("公孙",1);map.put("梁丘",1);map.put("闾丘",1);
        map.put("诸葛",1);map.put("宇文",1);map.put("公羊",1);map.put("南宫",1);
        map.put("闻人",1);map.put("司徒",1);map.put("淳于",1);map.put("公西",1);
        map.put("仲孙",1);map.put("澹台",1);map.put("公冶",1);map.put("拓拔",1);
        map.put("长孙",1);map.put("单于",1);map.put("太叔",1);map.put("百里",1);
        map.put("司空",1);map.put("轩辕",1);map.put("令狐",1);map.put("微生",1);
        map.put("颛孙",1);map.put("慕容",1);map.put("鲜于",1);map.put("西门",1);
        map.put("乐正",1);map.put("丌官",1);map.put("司寇",1);map.put("东门",1);
        map.put("宰父",1);map.put("端木",1);map.put("巫马",1);map.put("夏侯",1);
        map.put("南门",1);map.put("壤驷",1);map.put("公良",1);map.put("羊舌",1);
        map.put("左丘",1);map.put("谷梁",1);map.put("段干",1);map.put("呼延",1);
        map.put("夏侯",1);
    }


     public static String getFirstName(String name){
        if(StringUtils.isNotBlank(name) && StringUtils.length(name)>= 2){
            if(map.get(name.substring(0,2)) != null ){
                return  name.substring(0,2);
            }
            return  name.substring(0,1);
        }else{
            return name;
        }
     }


}
