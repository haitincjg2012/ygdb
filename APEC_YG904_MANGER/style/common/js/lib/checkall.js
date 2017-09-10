define(['jquery'],function($) {
    $.fn.checkAll = function(checkbox) { 
        var $cAll = $(this),
            $cBox = $(checkbox);
        $cAll.on("change",function() {
            $cBox.prop("checked", $(this).prop("checked"));
            if($cAll.prop("checked")){
                $cBox.each(function(){
                    $(this).parent().addClass('z-crt');
                }); 
            }else{
                $cBox.each(function(){
                    $(this).parent().removeClass('z-crt');
                }); 
            } 
        });
        $cBox.on("change",function() {
            var len = $cBox.length,
                trueLen = $cBox.filter(":checked").length;
                $cAll.prop("checked", len === trueLen);
                len === trueLen ? $cAll.parent().addClass('z-crt') : $cAll.parent().removeClass('z-crt');

            
        });
    }

})

