
var deepCopy = function(obj,copy){
    var copy = copy || {};
    console.log(obj)
    for(var i in obj){
    	console.log(i)
	    if(typeof obj[i] === 'object'){
   	   	    //要考虑深复制问题了
            if(obj[i] && obj[i].constructor === Array){
            	copy[i] =[];
            }else{
            	copy[i] = {};
            }
            deepCopy(obj[i],copy[i]);
	  	}else{
  	   	   	copy[i] = obj[i];
	  	}
  	}
  	return copy;
}
export default deepCopy;