/**
 * Created by gsy on 2017/8/23.
 * 公共Filter
 */
import Vue from 'vue'
import commonJs from '~/assets/js/common'

//数据为空时显示“无”
Vue.filter('noFilter',function(val){
    return val?val:"无";
});
//数据为空时，显示“0”
Vue.filter('zeroFilter',function(val){
    return val?val:0;
});
//年月日过滤器（yyyy-MM-dd HH:mm:ss 取 yyyy-MM-dd）
Vue.filter('ymdFilter',function(val){
    return val?val.spilt(" ")[0]:"";
});
//将时间戳转为yyyy-MM-dd
Vue.filter('timeFilter',function(val){
    val=val?commonJs.timePattern(new Date(val),'yyyy-MM-dd'):"";
    return val;
});
