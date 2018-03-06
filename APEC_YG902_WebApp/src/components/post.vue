<script>
  import API from "../api/api"
  var api = new API();

  function post(params, fn, Bubble) {
    try {
      api.post(params).then((res) => {
        var data = res.data;
        fn(data);
      }).catch((error) => {
          if(Bubble){
            Bubble(error);
          }
        console.log(error)
      })
    } catch (error) {
      console.log(error)
      if(Bubble){
        Bubble(error);
      }
    }
  }

  export default{post}
</script>
