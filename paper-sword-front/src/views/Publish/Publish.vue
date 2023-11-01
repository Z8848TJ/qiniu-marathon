<template>
    <div class="publish">
        <Header></Header>
        <div class="publishContainer">
            <div class="category">
                <span>发布视频</span>
                <span>发布图文</span>
            </div>
            <div class="uploadBox" @click="triggerFileInput">
                <input type="file" ref="fileInput" @change="uploadFile">
                <div class="upload">
                    <img src="../../../videolist/image/favicon.png" alt="">
                    <div class="uploadText">点击上传视频</div>
                </div>
            </div>
        </div>

    </div>
</template>

<script setup>
    import {ref} from 'vue'
    import {GetAction} from "../../util/api";

    const fileInput = ref(null)
    const triggerFileInput = ()=>{
        fileInput.value.click()
    }
    const uploadFile = (event)=> {
        const file = event.target.files[0]
        // console.log(file)
        GetAction('video/upload').then((res)=>{
            console.log(res)
        })
        // upload(file).then((res)=>{
        //     console.log(res)
        // })

    }
    const upload = (file)=> {   //file是选择的文件对象
        return new Promise((resolve, reject) => {
            GetAction().then((res) => {    //这是我封装的获取uptoken的方法，自行修改
                let uptoken = res.data;
                let key = new Date().getTime() + Math.random(1000) + file.name;   //这是上船后返回的文件名，这里为了避免重复，加上了时间戳和随机数
                var config = {
                    useCdnDomain: true,        //表示是否使用 cdn 加速域名，为布尔值，true 表示使用，默认为 false。
                    region: qiniu.region.z0,      //选择上传域名区域；当为 null 或 undefined 时，自动分析上传域名区域。  我这里是华东区
                    domain: "https://image.wedsap.com", //配置好的七牛云域名  如   https://cdn.qniyun.com/
                    chunkSize: 100,     //每个分片的大小，单位mb，默认值3
                    forceDirect: true    //直传还是断点续传方式，true为直传
                };
                var putExtra = {
                    fname: file.name,    //文件原始文件名
                    params: {},
                    mimeType: [] || null
                    // ...config,
                };
                var observable = qiniu.upload(file, key, uptoken, putExtra, config);
                observable.subscribe({
                    next: (result) => {
                        //主要用来展示进度
                    },
                    error: (err) => {
                        //上传错误后触发
                        console.log(err);
                        reject(err)
                    },
                    complete: (result) => {
                        //上传成功后触发。包含文件地址。
                        console.log(result);
                        resolve(result)
                    },
                });
            });
        })
    }


</script>

<style scoped>
    .publish{
        width: 100%;
        height: 100vh;
        background-color: #666666;
        display: flex;
        flex-direction: column;
    }
    .publishContainer{
        width: 80%;
        margin: 50px auto;
    }
    .category{
        display: flex;
    }
    .category span{
        margin-right: 30px;
        font-size: 20px;
    }
    .uploadBox{
        margin-top: 50px;
        width: 100%;
        height: 300px;
        border: 1px solid #999999;
        border-radius: 10px;
        cursor:pointer
    }
    input{
        display: none;
    }
    .upload{
        width: 100%;
        height: 300px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }
    .upload img{
        width: 40px;
        height: 40px;
    }
    .uploadText{
        margin-top: 30px;
        font-size: 18px;
    }

</style>
