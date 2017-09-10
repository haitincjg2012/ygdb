/*requirejs(['jquery',],function($){
   $('#myCarousel').carousel({
  interval: 2500
    });

})*/


/*$(function(){
    // 初始化插件
    $("#demo").hyUpload({
        width            :   "650px",                 // 宽度
        height           :   "400px",                 // 宽度
        itemWidth        :   "120px",                 // 文件项的宽度
        itemHeight       :   "100px",                 // 文件项的高度
        url              :   "/upload/UploadAction",  // 上传文件的路径
        multiple         :   true,                    // 是否可以多个文件上传
        dragDrop         :   true,                    // 是否可以拖动上传文件
        del              :   true,                    // 是否可以删除文件
        finishDel        :   false,                   // 是否在上传文件完成后删除预览

        onSelect: function(files, allFiles){                    // 选择文件的回调方法
            console.info("当前选择了以下文件：");
            console.info(files);
            console.info("之前没上传的文件：");
            console.info(allFiles);
        },
        onDelete: function(file, surplusFiles){                     // 删除一个文件的回调方法
            console.info("当前删除了此文件：");
            console.info(file);
            console.info("当前剩余的文件：");
            console.info(surplusFiles);
        },
        onSuccess: function(file){                    // 文件上传成功的回调方法
            console.info("此文件上传成功：");
            console.info(file);
        },
        onFailure: function(file){                    // 文件上传失败的回调方法
            console.info("此文件上传失败：");
            console.info(file);
        },
        onComplete: function(responseInfo){           // 上传完成的回调方法
            console.info("文件上传完成");
            console.info(responseInfo);
        }
    });
});*/


/*var title=encodeURIComponent('赫雁财富慰问广东武警官兵');
var url ='http://www.heyangroup.com/news/shownews.php?lang=cn&id=64';
var pic='http://www.heyangroup.com/upload/images/DSC_4450.jpg';
window.open('http://v.t.sina.com.cn/share/share.php?title=' + title + '&url=' + url + '&pic=' + pic, '_blank');
window.open('http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?site=www.tuan2.com&title=' + '' + '&desc=' + title + '&summary=' + '' + '&url=' + url + '&pics=' + pic, '_blank');
window.open('http://v.t.qq.com/share/share.php?title=' + title + '&url=' + url + '&pic=' + pic, '_blank');
window.open('http://widget.renren.com/dialog/share?resourceUrl=' + url + '&title=' + title + '&description=' + '' + '&pic=' + pic + '&charset=utf-8', '_blank')
window.open('http://www.douban.com/recommend/?title=' + title + '&url=' + url, '_blank');*/