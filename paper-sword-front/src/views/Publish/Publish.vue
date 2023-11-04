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
            <div v-if="videoId !== ''">
                <div class="detail">
                    <div class="coverBox">
                        设置封面
                        <div class="cover">
                            <img :src="cover" alt="" v-if="cover !==''">
                            <img src="../../../videolist/image/favicon.png" alt="" v-else>

                        </div>
                    </div>
                    <div class="descriptionBox">
                        <div>添加描述</div>
                        <textarea class="description" v-model="description">

                    </textarea>
                    </div>
                </div>
                <div class="submit" @click="onsubmit">投稿</div>
            </div>
        </div>
    </div>
</template>

<script setup>
    import {ref,onMounted} from 'vue'
    import {GetAction, PostAction} from "../../util/api"
    import * as qiniu from 'qiniu-js'

    //上传
    const fileInput = ref(null)
    const triggerFileInput = ()=>{
        fileInput.value.click()
    }
    const uploadFile = (event)=> {
        const file = event.target.files[0]
        // console.log(file)
        upload(file).then((res)=>{
            console.log(res)
            cover.value = res.data.info.cover
            videoId.value = res.data.info.id
            videoUrl.value = res.data.info.videoUrl
        })

    }
    const upload = (file)=> {   //file是选择的文件对象
        return new Promise((resolve, reject) => {
            GetAction('upload/video').then((res) => {    //这是我封装的获取uptoken的方法，自行修改
                console.log(res)
                let uptoken = res.data.token;
                let key = res.data.key
                let config = {
                    useCdnDomain: true,        //表示是否使用 cdn 加速域名，为布尔值，true 表示使用，默认为 false。
                    region: qiniu.region.z1,      //选择上传域名区域；当为 null 或 undefined 时，自动分析上传域名区域。  我这里是华东区
                    domain: "http://uc.qiniuapi.com", //配置好的七牛云域名  如   https://cdn.qniyun.com/
                    chunkSize: 100,     //每个分片的大小，单位mb，默认值3
                    forceDirect: true,    //直传还是断点续传方式，true为直传
                };
                let putExtra = {
                    fname: file.name,    //文件原始文件名
                    params: {},
                    mimeType: [] || null,
                    // ...config,
                };
                // console.log("-------",file, key, uptoken)
                let observable = qiniu.upload(file, key, uptoken, putExtra, config);
                observable.subscribe({
                    next: (res) => {
                        //主要用来展示进度
                        console.log(res)
                    },
                    error: (err) => {
                        //上传错误后触发
                        console.log(err);
                        reject(err)
                    },
                    complete: (res) => {
                        resolve(res)
                    },
                });
            });
        })
    }

    //视频封面
    const cover = ref('')
    //添加视频描述
    const description = ref('')

    //投稿
    const videoId = ref('')
    const videoUrl = ref('')
    const onsubmit = function () {
        let params = {
            id: videoId.value,
            description: description.value,

        }
        PostAction('/video/videoInfo',params).then((res)=>{
            console.log(res)
            //投稿成功，继续添加逻辑

        })
    }

    onMounted(()=>{
        cover.value = ''
        description.value = ''
        videoId.value = ''
        videoUrl.value = ''
    })


</script>

<style scoped>
    .publish{
        /*width: 100%;*/
        min-height: 100vh;
        background-color: #666666;
        display: flex;
        flex-direction: column;
        overflow-x: hidden;
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
        margin-top: 30px;
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
    .detail{
        /*width: 100%;*/
        /*height: 300px;*/
        margin-top: 30px;
        padding: 5px;
        border: 1px solid #999999;
        border-radius: 10px;
    }
    /*.coverBox{*/
    /*    height: 200px;*/
    /*}*/
    .cover{
        width: 300px;
        height: 150px;
        margin-top: 10px;
        /*background-color: red;*/
        border: 1px dashed #999999;
        display: flex;
        justify-content: center;
        border-radius: 5px;
    }
    .cover img{
        max-width: 300px;
        max-height: 150px;
    }
    .descriptionBox{
        margin-top: 10px;
    }
    .description{
        width: 90%;
        height: 100px;
        margin-top: 10px;
        padding: 20px;
        background-color: #666666;
        border: 1px solid #000000;
        border-radius: 10px;
        resize: none;
    }
    .description:focus {
         outline: none;
     }
    .submit{
        margin-top: 20px;
        width: 160px;
        height: 40px;
        line-height: 40px;
        background-color: orangered;
        border: 1px solid #999999;
        border-radius: 5px;
        text-align: center;
        color: #ffffff;
        font-size: 22px;

        cursor:pointer;
    }

</style>
