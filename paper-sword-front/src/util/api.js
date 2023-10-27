import axios from './axios'

export const GetAction = (url, data) => {
    return new Promise((resolve, reject) => {
        axios.get(url, { params: data })
            .then(res => {
                resolve(res.data)
            })
            .catch(err => {
                reject(err)
            })
    })
}
export const PostAction = (url, data) => {
    return new Promise((resolve, reject) => {
        axios.post(url, data)
            .then(res => {
                resolve(res.data);
            })
            .catch(err => {
                reject(err);
            });
    });
};


