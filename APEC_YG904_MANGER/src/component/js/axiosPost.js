import axios from 'axios';
import { Message  } from 'element-ui';

const axiosPost = function (url, param, callback) {
    // let data = JSON.stringify(param);
    axios.post(url, param).then((res) => {
        if(res.data.succeed) {
            callback(res);
        } else {
            Message  ({
                title: '注意',
                message: res.data.errorMsg,
                type: 'error',
            });
            if (res.data.errorCode == '20006') {
                setTimeout('window.location.href = "#/login"', 1000);
            }
        }
    }).catch((err) => {
	    // Message  ({
		 //    title: '注意',
		 //    message: err,
		 //    type: 'error',
	    // });
	    console.log(err);
    })
};

export default axiosPost;

