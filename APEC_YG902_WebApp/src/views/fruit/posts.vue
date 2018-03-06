<template>
  <div>
    <p class="c-post" v-for="item in postsC">
      <span class="c-p-name">{{item.name}}:{{item.toUserName?"@"+item.toUserName:""}}&nbsp;</span><span class="c-p-content">{{item.content}}</span>
    </p>
    <div class="cPostMore" v-if="postNumber" @click="morePosts">展开更多评论...</div>
  </div>
</template>
<style>
@import "../../assets/css/posts.css";
</style>
<script>
  export default{
      data(){
          return{
          }
      },
      props:["postsC","postNumber",'commentId','commentName'],
      methods:{
        morePosts(e){
            var name = localStorage.name;
            if(name){
              var id = this.commentId;
              var name = this.commentName || name;
              var coordinate = {
                y:e.pageY,
              }

              this.$emit("postDetail", coordinate);
              this.$router.push({name:"morePost", query:{commentId:id,commentName:name}});
            }

        }
      },
    mounted(){
    }
  }
</script>
