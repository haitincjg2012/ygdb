import moment from 'moment';
var getVal = function(key, arr) {
	for(var i = 0; i <= arr.length; i++){
		if(arr[i].value == key){
			return arr[i].name;
		}
	}
}
var filters = {
    userType:function(val){
    	var arr = [
	    	{value: 'DB', name: '代办'},
	    	{value: 'KS', name: '客商'},
	    	{value: 'LK', name: '冷库'},
	    	{value: 'ZZH', name: '种植户'},
	    	{value: 'HZS', name: '合作社'}
				//HZS("合作社");
	    ];
	    return getVal(val, arr);
    },
    editIntegral:function(val){
    	var arr = [
        	{value: 'REGISTER_LOGIN', name: 'PLUS'},
        	{value: 'DAY_FIRST_LOGIN', name: 'PLUS'},
        	{value: 'DAY_LOGIN_HOUR', name: 'PLUS'},
        	{value: 'VERIFIED_INFO', name: 'PLUS'},
        	{value: 'MEACH_SEND_ORDER', name: 'PLUS'},
        	{value: 'SINGLE_ONCE_SEND_REQUEST', name: 'PLUS'},
        	{value: 'SINGLE_ONE_SIGN_IN', name: 'PLUS'},
        	{value: 'SUCCESS_QUOTATION', name: 'PLUS'},
        	{value: 'SHARE_PROMOTION', name: 'PLUS'},
        	{value: 'LOTTERY_LUCK', name: 'PLUS'},
        	{value: 'EXCHANGE_PRODUCT', name: 'REDUCTION'},
        	{value: 'LOTTERY_PRODUCT', name: 'REDUCTION'},
        	{value: 'TRIGGER_DISABLED', name: 'REDUCTION'},
        	{value: 'GOODS_NOT_AGREE_FACT', name: 'REDUCTION'},
        	{value: 'EMBEZZLE_OTHERS_INFOMATION', name: 'REDUCTION'},
        	{value: 'DEFAME_OTHERS_GOODS', name: 'REDUCTION'},
        	{value: 'CUMULATIVE_ASSESSMENT', name: 'REDUCTION'},
        	{value: 'USER_REPORT', name: 'REDUCTION'},
        	{value: 'PUBLISH_FALSE_INFORMATION', name: 'REDUCTION'},
        	{value: 'REDUCE_VOCHER', name: 'REDUCTION'}
	    ];
	    return getVal(val, arr);
    },
    whistleBlowingType: function(val){
    	var arr = [
    		{name:"举报",value:"JB"},
            {name:"反馈",value:"FK"}
	    ];
	    return getVal(val, arr);
    },
    whistleBlowingRealm: function(val){
    	var arr = [
            {name:"交收单",value:"VOCHER"},
            {name:"帖子",value:"SOCIETYPOST"},
            {name:"供求",value:"PRODUCT"},
            {name:"行情",value:"ARTICLE"}
	    ];
	    return val?getVal(val, arr):"";
    },
    formatDate:function(val){
    	return val?moment(val).format("YYYY-MM-DD"):'';
    },
    formatDatetime:function(val){
    	return val?moment(val).format("YYYY-MM-DD HH:mm:ss"):'';
    },
    //比较时间
    compareTime:function(time1, time2){
    	if(time1)var getTime1 = new Date(time1).getTime();
    	if(time2)var getTime2 = new Date(time2).getTime();
    	var re;
    	if(time1&&time2){
    		re = getTime1<getTime2?true:false;
    	}
    	return re;
    },
}
export default filters;
