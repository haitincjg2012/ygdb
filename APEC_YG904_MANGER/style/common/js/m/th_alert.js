;(function($){
        $.extend({
            thalert : function(options){
            var _this = this;
            var settings = $.extend({
                title: '温馨提示',
                content : '确定要取消预约吗？',
                btnNum : 2 ,
                okBtn : '确认',
                cancelBtn : '取消',
                ok : null,
                cancel : null
            },options),
            alert_num = 0,
            _dotElem = document.documentElement,
            _html = '';
            _html +=  '<div class="m-th-alert z-crt" id="j_th_alert_'+alert_num+'">';
            _html +=  '<div class="m-alert">';
            if(options.title != undefined){
            _html +=  '<div class="m-alert-tt">'+settings.title+'</div>';
            }
            _html +=  '<div class="m-alert-con">'+settings.content+'</div>';
            if(options.btnNum == undefined || options.btnNum == 2 ){
            _html +=  '<div class="m-op bordert"><span class="j-yes borderr" id = "j_thalert_yes_'+alert_num+'">'+settings.okBtn+'</span><span class="j-no" id = "j_thalert_no_'+alert_num+'">'+settings.cancelBtn+'</span></div>';
            }
            if (options.btnNum == 1){
              _html +=  '<div class="m-op clearfix"><span class="j-yes u-confirm" id = "j_thalert_yes_'+alert_num+'">'+settings.okBtn+'</span></div>';  
            }
            _html +=  '</div>';
            _html +=  '<div class="shadow"></div>';
            _html +=  '</div>';
            $(document.body).append(_html);
            _dotElem.className +=' z-hidden';
            $(document).on('click',function(e){
                if(e.target.id == 'j_thalert_yes_'+alert_num+''){
                    $(document.body).find('#j_th_alert_'+alert_num+'').remove();
                    _dotElem.className = _dotElem.className.replace(/ z-hidden/,'');
                    alert_num++;  
                    if(options.ok != undefined){
                        settings.ok();
                        settings.ok = null;
                    }
                }
                if(e.target.id == 'j_thalert_no_'+alert_num+''){
                    $(document.body).find('#j_th_alert_'+alert_num+'').remove();
                    _dotElem.className = _dotElem.className.replace(/ z-hidden/,'');
                     alert_num++; 
                     if(options.cancel != undefined){
                        options.cancel();
                        settings.cancel = null;
                    }
                }
            });
                   
        }
        });
})(jQuery);