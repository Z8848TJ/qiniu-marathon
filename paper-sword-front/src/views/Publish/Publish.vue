<template>
    <div class="publish">
        <Header></Header>
        <div class="publishContainer">
            <div class="category">
                <span>发布视频</span>
<!--                <span>发布图文</span>-->
            </div>
            <div class="uploadBox" @click="triggerFileInput">
                <input type="file" ref="fileInput" @change="uploadFile">
                <div class="upload">
                    <img src="/contribute.png" alt="">
                    <div class="uploadText">{{uploadText}}</div>
                    <div class="progressBox" v-if="uploadProgress !== 0">
                        <div class="progressBar" :style="`width: ${Math.floor(uploadProgress)}%`"></div>
                    </div>
                </div>
            </div>
            <div v-if="videoId !== ''">
<!--                <div>-->
                <div class="detail">
                    <div class="coverBox">
                        <div>
                            <el-tag class="mx-1" type="warning" effect="plain" round> 查看封面 </el-tag>
                            <div class="cover">
                                <img :src="cover" alt="" v-if="cover !==''">
                                <img src="/favicon.png" alt="" v-else>
                            </div>
                        </div>
                        <div class="tagBox">
                            <el-tag class="mx-1" type="warning" effect="plain" round > 视频分析 </el-tag>
                            <div>
                                <el-tag v-for="tag in videoTag" class="tag" round>#{{tag}}</el-tag>
                            </div>
<!--                            <el-tag>123</el-tag>-->
                        </div>
                    </div>
                    <div class="descriptionBox">
                        <el-tag class="mx-1" type="warning" effect="plain" round > 添加描述 </el-tag>
                        <textarea class="description" v-model="description">

                    </textarea>
                    </div>
                </div>
                <div class="submit" @click="onsubmit">投稿</div>
            </div>
        </div>
        <div class="alert">
            <el-alert title="投稿成功！" type="success" center show-icon v-if="submitSuccess"/>
        </div>
    </div>
</template>

<script setup>
    import {ref,onMounted,defineComponent,onBeforeUnmount} from 'vue'
    import {GetAction, PostAction} from "../../util/api"
    import * as qiniu from 'qiniu-js'
    import {ElAlert,ElTag} from 'element-plus'
    import 'element-plus/dist/index.css';
    import {useRouter} from 'vue-router'


    defineComponent(ElAlert)
    const router = useRouter()

    //上传
    const uploadText = ref('点击上传视频')
    const fileInput = ref(null)
    const triggerFileInput = ()=>{
        fileInput.value.click()
    }
    const uploadFile = (event)=> {
        uploadText.value='上传中'
        const file = event.target.files[0]
        // console.log(file)
        upload(file).then((res)=>{
            console.log(res)
            cover.value = res.data.info.cover
            videoTag.value = res.data.info.videoString.split(',')
            videoId.value = res.data.info.id
            uploadText.value='上传完成'
        })

    }
    const upload = (file)=> {
        return new Promise((resolve, reject) => {
            GetAction('upload/video').then((res) => {
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
                        if (res.total.percent) {
                            uploadProgress.value = Math.floor(res.total.percent);
                        }
                    },
                    error: (err) => {
                        //上传错误后触发
                        console.log(err);
                        reject(err)
                    },
                    complete: (res) => {
                        resolve(res)
                        uploadProgress.value = 100;
                    },
                });
            });
        })
    }
    // 上传进度百分比
    const uploadProgress = ref(0);

    //视频封面
    const cover = ref('')
    //视频标签
    const videoTag = ref('')
    //添加视频描述
    const description = ref('')

    //投稿
    const videoId = ref('')
    const submitSuccess = ref(false)
    const submitTimeId = ref(null)
    const onsubmit = function () {
        let params = {
            id: videoId.value,
            description: description.value,
        }
        PostAction('/video/videoInfo',params).then((res)=>{
            console.log(res)
            //投稿成功，继续添加逻辑
            submitSuccess.value = true
            submitTimeId.value = setTimeout(()=>{
                router.push('user/self')
            },1000)
        })
    }

    onMounted(()=>{
        cover.value = ''
        description.value = ''
        videoId.value = ''
    })
    onBeforeUnmount(()=>{
        clearTimeout(submitTimeId.value)
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
    .progressBox {
        margin-top: 40px;
        width: 50%;
        height: 5px;
        background-color: #e0e0e0;
    }
    .progressBar {
        height: 100%;
        background-color: orangered;
    }
    .detail{
        /*width: 100%;*/
        /*height: 300px;*/
        margin-top: 30px;
        padding: 5px;
        border: 1px solid #999999;
        border-radius: 10px;
    }
    .coverBox{
        display: flex;

    }
    .cover{
        width: 300px;
        height: 150px;
        margin-top: 10px;
        margin-right: 40px;
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
    .tagBox{
        display: flex;
        flex-direction: column;
        justify-content: space-between;
    }
    .tag{
        margin-right: 15px;
        margin-top: -150px;
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

    .alert{
        width: 200px;
        position: fixed;
        top: 10%;
        left: 50%;
        transform: translate(-100px,0);
    }

    .el-alert {
        margin: 20px 0 0;
    }
    .el-alert:first-child {
        margin: 0;
    }
</style>
