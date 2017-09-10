!function($){
    var _nowdate =$('#j_nowdate').val() || new Date(),
    _time_now = new Date(_nowdate),
    _time_year = _time_now.getFullYear(),
    _time_month = _time_now.getMonth() + 1,
    _time_date = _time_now.getDate(),
    _time_day= _time_now.getDay(),
    months = [31, null, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
    var page = 1;//分页
    var coach_id = $("#coach_id").val();
    var none_box = $("#none_box").val();
    //判断闰年
    function isleap(year){
        return (year%4 === 0 && year%100 !== 0) || year%400 === 0;
    };
    months[1] = isleap(_time_year) ? 29 : 28;
    function alt_cn_day(day){
        var day_cn = '';
        if(day == 1){
            day_cn = '一';
        }
        if(day == 2){
            day_cn = '二';
        }
        if(day == 3){
            day_cn = '三';
        }
        if(day == 4){
            day_cn = '四';
        }
        if(day == 5){
            day_cn = '五';
        }
        if(day == 6){
            day_cn = '六';
        }
        if(day == 7){
            day_cn = '日';
        }
        return day_cn;
    }
  
        loop_date();
    if (document.getElementById('j_datewrapper')) {
    var date_scroll = new IScroll('#j_datewrapper',{
    click: true
     });
    date_scroll.on('scrollEnd', function(){
        var _this = this;
        if(_this.y - _this.maxScrollY < 10 && _this.directionY ==1){
            loop_date();
            _this.refresh(); 
        }
    });

};

function loop_date(){
    $.post('/coach/courseHistoryAjax/',{
        'page':page,
        'coach_id':coach_id
    },function(data){
        if(data.length>=1) {
            for (var i = 0; i < data.length; i++) {
                var _time_html = '',
                    _day = alt_cn_day(new Date(data[i]).getDay());
                _time_html += '<li data-flag="1" data-send="1" class="z-notsend z-notclick">';
                _time_html += '<div class="u-datelist">';
                _time_html += '<span class="j-date">' + data[i] + '</span><span>周' + _day + '<div class="u-arrow"><div class="u-arrowc j-arrow"></div></div></span>';
                _time_html += '</div>';
                _time_html += '<div class="m-courselist j-courselist"></div>';
                _time_html += '</li>';

                $('#j_date').append(_time_html);
            }
            page++;
        }else if(page<=1){
            var _time_html = '<div class="m-nodata" ><div class="u-nodata"><img src="'+none_box+'" alt=""></div><p>亲，你目前暂无课程安排！</p></div>';
            $('#j_date').append(_time_html);
        }

    },'json');
}
    
    
}(jQuery);