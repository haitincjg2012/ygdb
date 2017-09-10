export default{
  url: '',
  //baseURL: 'http://shoptest.ap-ec.cn',//测试环境
  //baseURL: 'https://shop.ap-ec.cn',//正式环境环境
  method: '',

  transformRequest: [
    function(data) {
      data = JSON.stringify(data);
      return data;
    }
  ],

  transformResponse: [
    function(data) {
      return data
    }
  ],

  headers: {
    'Content-Type':'application/json',
    'cc':'WEIXIN'
  },
  params: {

  },

  paramsSerializer: function(params) {
    return JSON.stringify(params)
  },


  data: {
  },


  timeout: 5000,


  withCredentials: false, // default


  responseType: 'json', // default



  maxContentLength: 2000,


  validateStatus: function(status) {
    return status >= 200 && status < 300; // default
    //return status;
  },


  maxRedirects: 5, // default
}
