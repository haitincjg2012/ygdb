!function($){
    var _nowdate =$('#j_nowdate').val() || new Date(),
    _time_now = new Date(_nowdate),
    _time_year = _time_now.getFullYear(),
    _time_month = _time_now.getMonth() + 1,
    _time_date = _time_now.getDate(),
    _time_day= _time_now.getDay(),
    months = [31, null, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
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

    for (var i = 0; i < 7; i++) {
        var _time_html = '',
        _day = alt_cn_day(_time_day);
        _time_html +='<li>';
        _time_html +='<div class="u-date">';
        _time_html +='<div class="u-datetext u-datetext-init j-datetextinit">'+_day+'</div>';
        if(i==0){
        _time_html +='<div class="u-datetext u-datetext-crt j-datetextcrt z-crt">';
        }else{
        _time_html +='<div class="u-datetext u-datetext-crt j-datetextcrt">'; 
        }
        _time_html +='<div class="datetext">';
        _time_html +='<span class="u-datenumber">'+_time_month+'.'+_time_date+'</span>';
        _time_html +='<span class="u-daynumber">周'+_day+'</span>';
        _time_html +='</div>';
        _time_html +='</div>';
        _time_html +='</div>';
        _time_html +='</li>';
        $('.j-wrapper-'+i).append('<input type="hidden" value="'+_time_year+'-'+_time_month+'-'+_time_date+'" class="cur_date"/>'); 
        if(_time_day<7){
            ++_time_day;
        }else{
            _time_day=1;
        }

       if(_time_month ==1 || _time_month ==3 || _time_month ==5 || _time_month ==7 || _time_month ==8 || _time_month ==10 || _time_month ==12){
            if(_time_date ==31){
                if(_time_month == 12){
                    _time_month = 1;
                    _time_year++;
                    _time_date = 1;
                }else{
                    _time_month++;
                    _time_date = 1;
                }
            }else{
                _time_date++;
            }
        }else if(_time_month ==2){
                if(isleap(_time_year)){
                    if(_time_date == 28){
                        _time_month++;
                        _time_date = 1;
                    }else{
                        _time_date++;
                    }
                }else{
                    if(_time_date == 29){
                        _time_month++;
                        _time_date = 1;
                    }else{
                        _time_date++;
                    }
                }
        }else{
            if(_time_date ==30){
                _time_date = 1;
                _time_month++;
                }else{
                _time_date++;
                }
            } 
           
      $('#j_date').append(_time_html);
    }
    function pc_html(a){
        var _html = '';
        _html +='<li>';
        _html +='<div class="u-date">';
        _html +='<div class="u-datetext u-datetext-init j-datetextinit">'+a+'</div>';
        _html +='<div class="u-datetext u-datetext-crt j-datetextcrt">'; 
        _html +='<div class="datetext">';
        _html +='<div class="datetextc">'+a+'</div>';
        _html +='</div>';
        _html +='</div>';
        _html +='</div>';
        _html +='</li>';
        return _html;
    }
    var _personal_html=pc_html('私教'),
    camp_html=pc_html('CAMP');
    $('#j_date').append(_personal_html);
    $('#j_date').append(camp_html);
}(jQuery);