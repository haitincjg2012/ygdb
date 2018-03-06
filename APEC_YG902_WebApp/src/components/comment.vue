<template>
  <div class="c-comment" v-show="showHide">
      <div class="c-comment-textarea">
          <textarea id="test" class="c-comment-textarea-string"></textarea>
      </div>
      <div class="c-comment-submit">
          <div @click="commentSubmit" class="c-comment-submit-btn">
              提交
          </div>
      </div>
  </div>
</template>
<style>
@import "../assets/css/comment.css";
</style>
<script>
  export default{
      methods:{
        commentSubmit(){
            this.$emit('getChildRelay');
        }
      },
      props:['showHide']
  }
</script>
