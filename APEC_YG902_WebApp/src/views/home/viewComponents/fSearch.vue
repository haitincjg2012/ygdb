<template>
    <div>
        <div class="z-header-T1 clearfix">
            <div class="retT" @click="back">
                <img src="../../../assets/img/ret.png">
            </div>
            <div class="z-write">
                <input type="text"  placeholder="" id="searchInput"/>
                <span class="x-del" @click="empty">
                </span>
            </div>
            <div class="z-s" @click="searchInfo">
                 搜索
            </div>
        </div>
        <div class="z-column-com">
             <div class="clearfix">
                 <div class="z-text-com">历史搜索</div>
                 <div v-if="HideO" class="del-com" @click="clear">
                   <img src="../../../assets/img/delete.png" />
                 </div>
             </div>
             <div class="z-list-com" v-if="dataHis">
                  <ul class="clearfix" data-index="0">
                    　<li v-for="todo in history"
                         class="li-com-two"
                         @click="searchO">
                            <div>{{ todo.keyword }}</div>
                    　　　　
                    　</li>
                  </ul>
             </div>
        </div>
        <div class="z-column-com">
            <div>
                 <div class="clearfix">
                      <div class="z-text-com">热门搜索</div>
                      <!--<div v-if="HideT" class="del-com">x</div>-->
                 </div>
            </div>
            <div class="z-list-com">
                <ul class="clearfix" data-index = "1">
                  　<li v-for="todo in hot"
                       class="li-com-two"
                        @click="searchO">
                  　　　　<div>{{ todo.keyword }}</div>
                  　</li>
                </ul>
            </div>
        </div>
    </div>
</template>
<style scoped>
@import "../../../assets/css/homeSearch.css";
</style>
<script>
  import API from '../../../api/api'
  const api = new API();
  var fn = {
      history:function (data) {
        if(!data.succeed){
          return;
        }
          var dt = data.data;
          if(dt){
              this.HideO = true;
          }else{
            this.HideO = false;
            return;
          }

          var arr = [];
          for(var key in dt){
              var obj = {};
              obj.keyword = dt[key];
              arr.push(obj)
          }
          this.history = arr;
      },
      hot:function (data) {
        if(!data.succeed){
          return;
        }
        var dt = data.data;
        this.hot = dt;
      },
      record:function (data) {
        if(!data.succeed){
          return;
        }
         this.$store.state.searchData = data;
        this.$router.push({name: "buy"})
      },
      empty:function (data) {
        if(!data.succeed){
          return;
        }
      }
  }
  export default{
      data(){
        return {
          show:'',
          HideO:true,
          HideT:true,
          dataHis:true,
          history:[],
          hot:[],
          keyword:""
        }
      },
    methods:{
        back(){
//          this.$router.go(-1);
          this.$router.push("/home?flag=0")
        },
        update(){
        },
        empty(){
          var el = document.getElementById('searchInput');
          var val = el.value;
          if(val == ''){
              return;
          }else{
            el.value = "";
          }
        },
        clear(){
           this.HideO = false;
           this.dataHis = false;
           let param = {
               api:"_node_user/_search_clear.apno",
               data:{

               }
           }

           this.post(param, fn.empty);
        },
        searchInfo(){
            var el = document.getElementById('searchInput');
            var val = el.value;

            if(val == ""){
                val = el.attributes.getNamedItem("placeholder").value;
            }else{
              this.keyword = val;
              this.saveKey();
            }
          this.sd(val);

        },
         saveKey(){
            let params = {
                "api":"_node_user/_search_save.apno",
                 data:{
                   "keyWord":this.keyword,
//                   orderType:"",
//                   searchType:"",
//                   pageNumber:0
                 }
            }

            this.post(params, fn.empty);
         },
       post(params, fn){
        try {
          api.post(params).then((res) => {
            var data = res.data;
            fn(data);
          }).catch((error) => {
            console.log(error)
          })
        } catch (error) {
          console.log(error)
        }
      },
       searchO(event){
           var evt = event || window.event;
           var target = evt.toElement || evt.srcElement;
           var tag = target.tagName.toUpperCase(),parent,keyword;
           if(tag == "LI"){
               parent = target;
               keyword = target.children[0].innerHTML;
           }else{
               parent = target.parentElement;
             keyword = target.innerHTML;
           }
           var idx = parent.parentElement.dataset.index;
           //历史搜索数据 热门搜索数据
           this.sd(keyword);
      },
      sd(keyword){
        this.$router.push({name: "buy",query:{KEY:keyword}})
      }
    },
    activated(){
       var search = this.$route.query.search;
       var el = document.getElementById('searchInput');
       el.attributes.getNamedItem("placeholder").value = search;
       this.keyword = search;
      let params = {
        api: "/yg-systemConfig-service/preSearch/getPreSearchInfo.apec",
        data:{
//          "searchType":"YZSS"
        }
      }
      let paramsT = {
          api:"/_node_user/_search_his.apno"
      }
      this.post(params, fn.hot.bind(this));
      this.post(paramsT,fn.history.bind(this));
    },
    mounted(){

    },

  }
</script>
