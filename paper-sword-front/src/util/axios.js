import axios from "axios";

axios.defaults.baseURL = 'http://49.234.3.91:8090/'  //正式
// axios.defaults.baseURL = 'http://dwy855.natappfree.cc/' //测试

//post请求头
if(localStorage.getItem('token')){
    axios.defaults.headers.Authorization = localStorage.getItem('token')
}
axios.defaults.headers.post["Content-Type"] = "application/json;charset=UTF-8";

//设置超时
axios.defaults.timeout = 15000;

axios.interceptors.request.use(
    config => {
        // console.log(config.headers)
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

axios.interceptors.response.use(
    response => {
        // console.log(response)
        if (response.status === 200) {
            return Promise.resolve(response);
        } else {
            return Promise.reject(response);
        }
    },
    error => {

        // alert(JSON.stringify(error), '请求异常', {
        //     confirmButtonText: '确定',
        //     callback: (action) => {
        //         console.log(action)
        //     }
        // });
    }
);
export default {
    post(url, data) {
        return new Promise((resolve, reject) => {
            axios({
                method: 'post',
                url,
                data: data,
            })
                .then(res => {
                    resolve(res.data)
                })
                .catch(err => {
                    reject(err)
                });
        })
    },

    get(url, data) {
        return new Promise((resolve, reject) => {
            axios({
                method: 'get',
                url,
                params: data,
            })
                .then(res => {
                    resolve(res.data)
                })
                .catch(err => {
                    reject(err)
                })
        })
    }
};
