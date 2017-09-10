<template>
  <div id="app123">
     <nav class="navbar navbar-inverse navbar-fixed-top">
       <div class="navbar-header">
           <a class="navbar-brand" href="#">优惠券相关操作</a>
       </div>
       <div id="navbar" class="collapse navbar-collapse">
                <ul class="nav navbar-nav">
                    <li class="active"><a href="index.html">新增</a></li>
                    <li><a href="find.html">查询</a></li>
                    <li><a href="list.html">列表</a></li>
                    <li><a href="good.html">商品查询</a></li>
                    <li><a href="send.html">派发</a></li>
                    <li><a href="used.html">使用情况</a></li>
                </ul>
       </div>
     </nav>
      <div id="add" class="main">
          <div class="center page-header"></div>
          <div class="form-group">
                <div class="third">
                    <label for="couponName">优惠券名字</label> <input type="text"
                        class="form-control" v-model="coupon.couponName" id="couponName" />
                </div>
          </div>
      </div>
  </div>

</template>
<script>
</script>


<style>
body {
  font-family: Helvetica, sans-serif;
}
</style>
