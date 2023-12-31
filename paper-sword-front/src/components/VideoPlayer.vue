<template>
    <div class="container">
        <div id="playBox" :class="{'playBox':true}" tabindex="0" @keydown="adjustProgress">
            <div id="videoBox" class="videoBox" :style="{height: videoHeight}" @click="play">
                <video id="video"
                       :src="source.videoUrl"
                       ref="video"
                       @loadedmetadata="getVideoDuration"
                       @timeupdate="updateProgress"
                       @ended="videoEnded"

                ></video>
                <div class="vPlay">
                    <img src="/bigPlay.png" alt="" v-if="!isPlay">
                </div>
            </div>
            <div class="detailBox">
                <div class="author">@ {{source.username}}</div>
                <div class="description">
                    {{source.description}}
                </div>
            </div>
            <div class="interactionBox">
                <div class="like">
                    <img src="/like.png" alt="" v-if="!isLiked" @click="changeLiked(1)">
                    <img src="/liked.png" alt="" v-else @click="changeLiked(0)">
                    <div class="interactionText">{{likeNumber}}</div>
                </div>
                <div class="review" @click="showReview">
                    <img src="/review.png" alt="">
                    <div class="interactionText">{{reviewNumber}}</div>
                </div>
                <div class="collect">
                    <img src="/star.png" alt="" v-if="!isStar" @click="changeCollect(1)">
                    <img src="/stared.png" alt="" v-else @click="changeCollect(0)">
                    <div class="interactionText">{{collectNumber}}</div>
                </div>
                <div class="more">
                    <img src="/more.png" alt="">
                </div>
            </div>
            <div class="controlBox">
                <div class="progress" @click="selectTime">
                    <div class="now" :style="{ width: progressWidth, transition: progressTransition }"></div>
                </div>
                <button ref="button"></button>
                <div class="control">
                    <div class="group1">
                        <div class="playButton" @click="play">
                            <img src="/pause.png" alt="" title="暂停" v-if="isPlay">
                            <img src="/play.png" alt="" title="播放" v-else>
                        </div>
                        <div class="time">
                            <span class="currentTime">{{currentTime}}</span> / <span class="totalTime">{{totalTime}}</span>
                        </div>
                    </div>
                    <div class="group2">
                        <div class="speed" @mouseenter="showSpeedList" @mouseleave="hideSpeedList">
                            <div class="curSpeed">{{currentSpeed}}x</div>
                            <div class="speedList" v-if="isSpeedListVisible">
                                <div
                                        v-for="speed in speedList"
                                        :key="speed"
                                        @click="changeSpeed(parseFloat(speed))"
                                        :class="{active: currentSpeed === parseFloat(speed) }"
                                >
                                    {{ speed }}x
                                </div>
                            </div>
                        </div>
                        <div class="volume" @mouseenter="showVolumeBar" @mouseleave="hideVolumeBar">
                            <div class="volumeBox" @click="toggleMute">
                                <img :src="volumeIcon" alt="">
                            </div>
                            <div class="volumeBar" v-if="isVolumeBarVisible">
                                <div class="volumeText">{{nowVolume}}</div>
                                <div
                                        class="barBox"
                                        @click="setVolume"
                                        @mousedown="startDrag"
                                        @mousemove="changeToggleVideo"
                                        @mouseleave="endDrag"
                                        @mouseup="endDrag"
                                        ref="refVolumeBar"
                                    >
                                    <div class="slider">
                                        <div class="nowVolume" :style="{ height: volumePercent }"></div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="fullscreen" title="全屏" ref="fullscreen" @click="toggleFullscreen">
                            <img src="/fullscreen.png" alt="">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="reviewBox" @mouseenter="disabledRoll" @mouseleave="enabledRoll" v-if="isShowReview">
            <div class="title">评论</div>
            <div class="infiniteList" style="overflow: auto" @scroll="handleScroll">
                <Review v-for="(item,index) in comment" :key="index" :source="comment[index]"></Review>
            </div>
            <div class="submit">
                <input v-model="newComment" type="text" placeholder="输入你的评论" />
                <button @click="submitComment">提交评论</button>
            </div>
            <div class="blurBg" :style="{ 'background-image': `url(${source.cover})` }"></div>
        </div>
        <div class="blurBg" :style="{ 'background-image': `url(${source.cover})` }"></div>
    </div>
</template>

<script setup>
    import {ref,computed,onMounted,onBeforeUnmount,watch } from 'vue'
    import {GetAction, PostAction} from "../util/api";
    import {useStore} from 'vuex'
    import LoginRegister from '../components/LoginRegister.vue'
    import Review from "../components/Review.vue";




    const props = defineProps(['source','isPlaying','videoHeight'])
    const emits = defineEmits(['canToggleVideo','fullscreen'])
    const store = useStore()

    const video = ref(null)
    //播放暂停
    const isPlay = ref(false)
    const play = () =>{
        //播放完重新播放进度条瞬间归零，没有过渡动画
        if(video.value.duration <= video.value.currentTime){
            progressTransition.value = 'none'
            progressWidth.value = 0
        }
        if(isPlay.value){
            // console.log('暂停')
            video.value.pause()
        }else{
            // console.log('播放')
            video.value.play()
        }
        isPlay.value = !isPlay.value
    }

    //时间
    const currentTime = ref("00:00")
    const totalTime = ref("00:00")
    const haveHour = ref(false)
    //时间格式
    const makeTime = function (time) {
        time = Math.floor(time)
        let h = Math.floor(time/3600)
        let m = Math.floor((time-h*3600)/60)
        let s = time-h*3600-m*60
        m = m < 10 ? "0" + m : m
        s = s < 10 ? "0" + s : s
        if(haveHour.value){
            h = h < 10 ? "0" + h : h
            return h + ":" + m + ":" + s
        }
        return m + ":" + s
    }
    //视频时长
    const getVideoDuration = () => {
        if (video.value) {
            totalTime.value = makeTime(video.value.duration)
        }
    }
    // 更新时间轴进度
    const progressWidth = ref(0)
    const progressTransition = ref('width 0.3s linear')
    const updateProgress = ()=>{
        // console.log(isPlay.value)
        if(video.value){
            currentTime.value = makeTime(video.value.currentTime)
            progressWidth.value = `${(video.value.currentTime / video.value.duration) * 100}%`
            //最后一秒加快速度
            if (video.value.duration - video.value.currentTime <= 1) {
                progressTransition.value = 'width 0.2s linear'
            } else {
                progressTransition.value = 'width 0.3s linear'
            }
        }

    }
    const videoEnded = ()=>{
        isPlay.value=false
    }
    // 点击选择时间
    const selectTime = (event)=>{
        const progressBar = event.currentTarget
        const clickX = event.clientX - progressBar.getBoundingClientRect().left
        const percent = clickX / progressBar.clientWidth
        video.value.currentTime = percent * video.value.duration
        isPlay.value = true
        video.value.play()
        updateProgress()
    }
    //键盘切换时间
    const adjustProgress = (event)=>{
       if(props.isPlaying){
           if (event.key === 'ArrowLeft' || event.key === 'ArrowRight') {
               if (event.key === 'ArrowLeft') {
                   video.value.currentTime -= 5
                   progressWidth.value = `${(video.value.currentTime / video.value.duration) * 100}%`
               } else if (event.key === 'ArrowRight') {
                   video.value.currentTime += 5
                   progressWidth.value = `${(video.value.currentTime / video.value.duration) * 100}%`
               }
               // 防止事件冒泡
               event.preventDefault()
           }
       }
    }

    //全屏
    const isFull = ref(false)
    const videoBox = document.querySelector('.videoBox')
    // const toggleFullscreen = function () {
    //     const element = document.querySelector('#playBox')
    //     if(isFull.value){
    //         console.log('退出全屏')
    //         if (document.exitFullscreen) {
    //             document.exitFullscreen()
    //         } else if (document.webkitCancelFullScreen) {
    //             document.webkitCancelFullScreen()
    //         } else if (document.mozCancelFullScreen) {
    //             document.mozCancelFullScreen()
    //         } else if (document.msExitFullscreen) {
    //             document.msExitFullscreen()
    //         }
    //         element.style.userSelect = 'none'
    //     }else{
    //         console.log('全屏')
    //         if (element.requestFullscreen) {
    //             element.requestFullscreen()
    //         } else if (element.webkitRequestFullScreen) {
    //             element.webkitRequestFullScreen()
    //         } else if (element.mozRequestFullScreen) {
    //             element.mozRequestFullScreen()
    //         } else if (element.msRequestFullscreen) {
    //             // IE11
    //             element.msRequestFullscreen()
    //         }
    //     }
    //     isFull.value = !isFull.value
    //
    // }

    const toggleFullscreen = ()=>{
        emits('fullscreen',!isFull.value)
        isFull.value = !isFull.value
    }

    //倍速
    const isSpeedListVisible = ref(false)
    const speedTimeId = ref(null)
    const currentSpeed = ref(1.0)
    const speedList = ['2', '1.5', '1.25', '1', '0.75', '0.5']
    //倍速显隐
    const showSpeedList = () => {
        isSpeedListVisible.value = true
        clearTimeout(speedTimeId.value)
    }
    const hideSpeedList = () => {
        speedTimeId.value = setTimeout(()=>{
            isSpeedListVisible.value = false
        },500)
    }
    //改变视频播放速度
    const changeSpeed = (speed)=>{
        currentSpeed.value = speed
        isSpeedListVisible.value = false
        video.value.playbackRate = speed
        clearTimeout(speedTimeId.value)
    }

    //音量调节
    const volume = ref(1.0)
    const isMuted = ref(false)
    const volumeTimeId = ref(null)
    const isVolumeBarVisible = ref(false)
    const refVolumeBar = ref(null)
    // 调整前的音量，用于静音恢复
    const prevVolume = ref(null)
    const nowVolume = computed(()=> (volume.value * 100).toFixed(0))
    const volumePercent = computed(() => `${volume.value * 100}%`)
    //拖动状态
    const dragState = ref({
        dragging: false,
        startY: 0,
        startVolume: 0,
    })
    //图标
    const volumeIcon = computed(()=>{
        if (isMuted.value||volume.value === 0){
            return '/noVolume.png'
        }else if(volume.value <0.5){
            return '/lowVolume.png'
        }else {
            return '/highVolume.png'
        }
    })
    //静音
    const toggleMute = () => {
        isMuted.value = !isMuted.value
        if (isMuted.value) {
            prevVolume.value = volume.value
            volume.value = 0
            video.value.muted = true
        } else {
            volume.value = prevVolume.value || 1.0
            video.value.muted = false
        }
    }
    //设置音量
    const setVolume = (event) => {
        // if (dragState.value.dragging) return
        const volumeBar = event.currentTarget
        const clickY = event.clientY - volumeBar.getBoundingClientRect().top
        const percent = clickY / volumeBar.clientHeight
        // 限制音量值在 0 到 1 之间
        volume.value = Math.max(0, Math.min(1, 1.0 - percent))
        video.value.volume = volume.value
        prevVolume.value = isMuted.value ? 1.0 : volume.value
    }
    //拖动
    const startDrag = (event) => {
        // console.log('startDrag')
        if (isMuted.value) toggleMute()
        const volumeBar = event.currentTarget
        dragState.value.dragging = true
        dragState.value.startY = event.clientY
        //获取当前音量
        const clickY = event.clientY - volumeBar.getBoundingClientRect().top
        const percent = clickY / volumeBar.clientHeight
        // 限制音量值在 0 到 1 之间
        volume.value = Math.max(0, Math.min(1, 1.0 - percent))
        video.value.volume = volume.value
        prevVolume.value = isMuted.value ? 1.0 : volume.value
        dragState.value.startVolume = volume.value

        volumeBar.addEventListener('mousemove', changeToggleVideo)
        volumeBar.addEventListener('mouseup', endDrag)
    }
    const changeToggleVideo = (event) => {
        if (dragState.value.dragging) {
            const volumeBar = event.currentTarget
            const deltaY = event.clientY - dragState.value.startY
            const totalHeight = volumeBar.clientHeight
            const changePercent = deltaY / totalHeight
            const newVolume = dragState.value.startVolume - changePercent
            volume.value = Math.max(0, Math.min(1, newVolume))
            video.value.volume = volume.value
        }
    }
    const endDrag = () => {
        if (dragState.value.dragging) {
            dragState.value.dragging = false
            const volumeBar = refVolumeBar.value
            volumeBar.removeEventListener('mousemove', changeToggleVideo)
            volumeBar.removeEventListener('mouseup', endDrag)
        }
    }
    //音量条显隐
    const showVolumeBar = () => {
        isVolumeBarVisible.value = true
        emits('canToggleVideo',false)
        clearTimeout(volumeTimeId.value)
    }
    const hideVolumeBar = () => {
        volumeTimeId.value = setTimeout(()=>{
            isVolumeBarVisible.value = false
        },500)
        emits('canToggleVideo',true)
    }

    //点赞等信息
    const isLiked = ref(false)
    const likeNumber = ref(0)
    const changeLiked = (type)=>{
        if(!store.state.isLogin){
            store.commit('showLoginRegister',true)
        }else{
            if(type ===1){
                isLiked.value = true
                likeNumber.value++
            }else{
                isLiked.value = false
                likeNumber.value--
            }
            let params = {
                videoId : props.source.id,
                userId : props.source.userId,
                type : type
            }
            GetAction('/interact/like',params).then((res)=>{
                // console.log(res)
            })
        }
    }
    //评论
    const reviewNumber = ref(0)
    const comment = ref([])
    const isShowReview = ref(false)
    const showReview = ()=>{
        if(!store.state.isLogin){
            store.commit('showLoginRegister',true)
        }else{
            isShowReview.value = !isShowReview.value
        }
    }
    const disabledRoll = () => {
        emits('canToggleVideo',false)
    }
    const enabledRoll = () => {
        emits('canToggleVideo',true)
    }
    const handleScroll = ()=>{
        const target = event.target;
        if (target.scrollHeight - target.scrollTop === target.clientHeight) {
            load(); // 当到达底部时加载更多评论
        }
    }
    //添加评论


    //转发
    const isStar = ref(false)
    const collectNumber = ref(0)
    const changeCollect = (type)=>{
        if(!store.state.isLogin){
            store.commit('showLoginRegister',true)
        }else {
            if (type === 1) {
                isStar.value = true
                collectNumber.value++
            } else {
                isStar.value = false
                collectNumber.value--
            }
            let params = {
                videoId: props.source.id,
                userId: props.source.userId,
                type: type
            }
            GetAction('/interact/collect', params).then((res) => {
                // console.log('count', res)

            })
        }
    }


    //初始化信息
    const initData = ()=>{
        let params = {
            videoId : props.source.id,
            begin : 0,
            userId:store.state.userId
        }
        GetAction('/video/count',params).then((res)=>{
            // console.log('数量信息',res)
            likeNumber.value = res.data.counts[0]
            reviewNumber.value = res.data.counts[2]
            collectNumber.value = res.data.counts[1]
            isLiked.value = res.data.likeAndCollect[0]
            isStar.value = res.data.likeAndCollect[1]
        })
        //获取评论
        GetAction('/video/comment',params).then((res)=>{
            // console.log('评论',res)
            comment.value = res.data.info
        })
    }

    //添加评论
    const newComment = ref('')
    const submitComment  = ()=>{
        if (newComment.value.trim() === '') {
            // 防止提交空评论
            return
        }
        // 构建评论对象，可以包括评论内容、评论者信息、评论时间等
        const comment = {
            videoId : props.source.id,
            parentId : 0,
            content: newComment.value,
            recoverUserId : props.source.userId
        }
        PostAction('/comment/add',comment).then((res)=>{
            // console.log(res)
            initData()
        })
        // 清空
        newComment.value = ''
    }
    const count = ref(20)
    const load = () => {
        count.value += 2
    }


    onMounted(()=>{
        initData()
        watch(props,(newValue)=>{
            if(newValue.isPlaying){
                isPlay.value = true
                video.value.play()
                document.addEventListener('keydown', adjustProgress)
            }else{
                video.value.pause()
                isPlay.value = false
                document.removeEventListener('keydown', adjustProgress)
            }
        },{
            immediate: true,
            deep: true
        })

    })
    onBeforeUnmount(()=>{
        video.value = null
        clearTimeout(speedTimeId.value)
        clearTimeout(volumeTimeId.value)
        document.removeEventListener('keydown', adjustProgress)
    })
</script>

<style scoped>
.container{
    display: flex;
    position: relative;
    min-width: 900px;
}
.fullscreen {
    user-select: none;
}
.blurBg{
    content: "";
    /*background-image: url("../../videolist/image/bg2.png");*/
    background-size: cover;
    filter: blur(100px);
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: -1;
}
.playBox{
    min-width: 450px;
}
.videoBox{
    position: relative;
    max-width: 100%;
    height: calc(100vh - 50px);
    margin: 0 auto;
}
#video{
    height: calc(100vh - 50px);
    width: 100%;
}
.vPlay{
    width: 100px;
    height: 100px;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50px,-50px)
}
.detailBox{
    height: 100px;
    width: 30%;
    position: absolute;
    bottom: 50px;
    left: 20px;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    color: #ffffff;
}
.author{
    font-size: 22px;
}
.description{
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.interactionBox{
    height: 400px;
    width: 70px;
    position: absolute;
    bottom: 50px;
    right: 20px;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
}
.like,.review,.collect,.more{
    width: 100%;
    height: 50px;
    margin: 20px auto;
    text-align: center;
    color: #ffffff;
}
.like img{
    width: 35px;
    height: 35px;
}
.review,.collect,.more img{
    width: 30px;
    height: 30px;
}
.interactionText{

}

.controlBox{
    position: absolute;
    width: 100%;
    bottom: 20px;
}
.controlBox Button{
    display: none;
}
.progress{
    /*min-width: 1020px;*/
    height: 3px;
    background-color: #999999;
    border-radius: 3px;
    position: relative;
}
.progress .now{
    width: 0;
    height: 3px;
    background-color: red;
    border-radius: 3px;
    transition: width 500ms linear;
    position: absolute;
    top: 0;
    left: 0;
}
.progress:hover,.progress:hover .now{
    height: 10px;
    cursor: pointer;
}
.control{
    width: 100%;
    height: 20px;
    display: flex;
    justify-content: space-between;
}
.group1,.group2{
    display: flex;
    padding: 10px;
}
.time{
    height: 20px;
    margin: 0 15px;
    color: #ffffff;
    line-height: 20px;
}
.speed{
    width: 60px;
    color: #ffffff;
    text-align: center;
    /*margin: 0 10px;*/
    cursor: default;
    position: relative;
}
.curSpeed{
    line-height: 20px;
}
.speedList{
    /*display: none;*/
    width: 100%;
    border: solid 1px #999999;
    border-radius: 5px;
    line-height: 35px;
    position: absolute;
    bottom: 25px;
    left: 0;
    /*z-index: 50;*/
}
.speedList div:hover,.active{
    color: #dc143c;
}
.playButton,.fullscreen{
    width: 20px;
    height: 20px;
    margin: 0 15px;
}
img{
    height: 100%;
    width: 100%;
}
.volume{
    width: 50px;
    color: #ffffff;
    text-align: center;
    margin: 0 10px;
    cursor: default;
    position: relative;
}
.volume .volumeBox{
    width: 20px;
    height: 20px;
    margin: 0 auto;
}
.volumeBar{
    width: 50px;
    height: 140px;
    border: solid 1px #999999;
    border-radius: 4px;
    position: absolute;
    bottom: 25px;
    left: 0;

}
.volumeText{
    margin-top: 5px;
    font-size: 12px;
    text-align: center;
}
.barBox{
    height: 100px;
    width: 100%;
    position: absolute;
    bottom: 10px;
    left: 0;
    /*z-index: 50;*/
}
.slider{
    height: 100px;
    width: 6px;
    margin: 0 auto;
    border: solid 1px #999999;
    border-radius: 5px;
    /*z-index: 50;*/
}
.nowVolume{
    height: 10px;
    width: 6px;
    background-color: #189BF2;
    border-radius: 5px;
    line-height: 35px;
    position: absolute;
    bottom: 0;
}

.playBox{
    overflow: hidden;
    flex: 3;
    position: relative;
}
.reviewBox{
    /*flex: 1;*/
    width: 400px;
    height: calc(100vh - 50px);
    overflow: auto;
    padding: 0;
    position: relative;
}
.reviewBox .title{
    height: 30px;
    line-height: 30px;
    text-align: center;
    font-size: 20px;
    color: #ffffff;
    background-color: #000000;
}
.infiniteList {
    height: calc(100vh - 150px);
    padding: 0;
    margin: 0;
    list-style: none;
    /*background-color: red;*/
}

.submit {
    display: flex;
    align-items: center;
    background: #fff; /* 背景颜色 */
    border-radius: 50px; /* 圆角 */
    padding: 5px 10px; /* 内边距 */
    margin: 10px 0;
    box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1); /* 阴影效果 */
}
/* 输入框样式 */
input[type="text"] {
    flex: 1; /* 输入框占据剩余空间 */
    border: none;
    outline: none;
    padding: 10px;
    font-size: 14px;
}

/* 提交按钮样式 */
button {
    background: #fe345e; /* 按钮背景颜色 */
    border: none;
    outline: none;
    color: #fff; /* 按钮文字颜色 */
    padding: 10px 20px; /* 按钮内边距 */
    border-radius: 20px; /* 圆角 */
    margin-left: 10px;
    cursor: pointer;
    font-size: 14px;
    transition: background 0.2s;
}

/* 提交按钮悬停样式 */
button:hover {
    background: #ff003a; /* 鼠标悬停时的背景颜色 */
}



/* 滚动条 */
/* 自定义垂直滚动条 */
::-webkit-scrollbar {
    width: 10px; /* 滚动条宽度 */
}
::-webkit-scrollbar-thumb {
    background-color: #ccc; /* 滚动条滑块颜色 */
    border-radius: 5px; /* 滚动条滑块圆角 */
}

::-webkit-scrollbar-track {
    background-color: #f1f1f1; /* 滚动条轨道颜色 */
}

/* 自定义水平滚动条 */
::-webkit-scrollbar-horizontal {
    height: 10px; /* 水平滚动条高度 */
}

/* 自定义水平滚动条滑块 */
::-webkit-scrollbar-thumb:horizontal {
    background-color: #ccc; /* 水平滚动条滑块颜色 */
}

/* 自定义水平滚动条轨道 */
::-webkit-scrollbar-track:horizontal {
    background-color: #f1f1f1; /* 水平滚动条轨道颜色 */
}

</style>
