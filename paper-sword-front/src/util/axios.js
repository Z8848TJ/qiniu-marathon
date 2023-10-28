import axios from "axios";

axios.defaults.baseURL = 'http://i7d4qr.natappfree.cc/'  //正式
// axios.defaults.baseURL = 'http://localhost:3000' //测试

//post请求头
axios.defaults.headers.post["Content-Type"] = "application/json;charset=UTF-8";

//允许跨域携带cookie信息
axios.defaults.withCredentials = false;
//设置超时
axios.defaults.timeout = 15000;

axios.interceptors.request.use(
    config => {
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

axios.interceptors.response.use(
    response => {
        if (response.status === 200) {
            return Promise.resolve(response);
        } else {
            return Promise.reject(response);
        }
    },
    error => {

        alert(JSON.stringify(error), '请求异常', {
            confirmButtonText: '确定',
            callback: (action) => {
                console.log(action)
            }
        });
    }
);
export default {
    /**
     * @param {String} url
     * @param {Object} data
     * @returns Promise
     */
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
