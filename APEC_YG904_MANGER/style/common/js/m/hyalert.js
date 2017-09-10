  define(['jquery','global','dialog'],function($,global){
    var mod={};
    isLoad=true;
    mod.init=function(opt){
        var option=$.extend({
            title : '温馨提示',
            content : ''
        },opt);
        if(isLoad!=false){
            var cssUrl =global.static_url+"common/css/ui-dialog.css";
            var cssUrl2 =global.static_url+"common/css/hyalert.css";
            global.loadCSS(cssUrl);
            global.loadCSS(cssUrl2);
            isLoad=false;
        }
            art.dialog({
                title: option.title,
                content: option.content
            });
            $('<div class="shadow"></div>').appendTo(document.body);
            $(".aui_close").on("click",function(){
                $(".shadow").remove();
            });
            $(".cancel",document.body).on("click",function(){
                $(".aui_close").trigger("click");
                $(this).parents(".aui_state_focus").remove();
                
            })

    }
    return mod;
        
  })
