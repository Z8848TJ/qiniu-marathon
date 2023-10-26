<template>
    <div class="container">
        <div id="playBox" :class="{'playBox':true,'full':isFull}">
            <div id="videoBox" class="videoBox" @click="play">
                <video id="video"
                       :src="srcVideo"
                       ref="refVideo"
                       @loadedmetadata="getVideoDuration"
                       @timeupdate="updateProgress"
                       @ended="videoEnded"
                ></video>
                <div class="vPlay">
                    <img src="../../videolist/image/bigPlay.png" alt="" v-if="!isPlay">
                </div>
            </div>
            <div class="controlBox">
                <div class="progress" @click="selectTime">
                    <div class="now" :style="{ width: progressWidth, transition: progressTransition }"></div>
                </div>
                <div class="control">
                    <div class="group1">
                        <div class="playButton" @click="play">
                            <img src="../../videolist/image/pause.png" alt="" title="暂停" v-if="isPlay">
                            <img src="../../videolist/image/play.png" alt="" title="播放" v-else>
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
                                <img src="../../videolist/image/noVolume.png" alt="">
                            </div>
                            <div class="volumeBar" v-if="isVolumeBarVisible">
                                <div class="volumeText">{{nowVolume}}</div>
                                <div
                                        class="barBox"
                                        @click="setVolume"
                                        @mousedown="startDrag"
                                        @mousemove="dragVolume"
                                        @mouseup="endDrag"
                                        @mouseleave="endDrag"
                                        ref="refVolumeBar"
                                    >
                                    <div class="slider">
                                        <div class="nowVolume" :style="{ height: volumePercent }"></div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="fullscreen" title="全屏" ref="fullscreen" @click="toggleFullscreen">
                            <img src="../../videolist/image/fullscreen.png" alt="">
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</template>

<script setup>
import {ref,computed,onMounted,onBeforeUnmount} from 'vue'

    // let video = document.querySelector('#video')
    const refVideo = ref(null)
    const srcVideo = ref('../../videolist/1-3.mp4')
    const getVideoDuration = () => {
        const video = refVideo.value
        if (video) {
            console.log('视频时长:', video.duration)
            totalTime.value = makeTime(video.duration)
        }
    }

    //播放暂停
    const isPlay = ref(false)
    const play = function () {
        if(video.duration <= video.currentTime){
            progressTransition.value = 'none'
            progressWidth.value = 0
        }
        if(isPlay.value){
            console.log('暂停')
            video.pause()
        }else{
            console.log('播放')
            video.play()
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
    // 更新时间轴进度
    const progressWidth = ref(0)
    const progressTransition = ref('width 0.3s linear')
    const updateProgress = ()=>{
        // console.log(isPlay.value)
        currentTime.value = makeTime(video.currentTime)
        progressWidth.value = `${(video.currentTime / video.duration) * 100}%`;
        //最后一秒加快速度
        if (video.duration - video.currentTime <= 1) {
            progressTransition.value = 'width 0.2s linear'
        } else {
            progressTransition.value = 'width 0.3s linear'
        }
    }
    const videoEnded = ()=>{
        isPlay.value=false
    }
    // 点击选择时间
    const selectTime = (event)=>{
        const progressBar = event.currentTarget;
        const clickX = event.clientX - progressBar.getBoundingClientRect().left;
        const percent = clickX / progressBar.clientWidth;
        video.currentTime = percent * video.duration;
        isPlay.value = true
        video.play()
        updateProgress()
    }

    //全屏
    const isFull = ref(false)
    const toggleFullscreen = function () {
        const element = document.querySelector('#playBox')
        if(isFull.value){
            console.log('退出全屏')
            if (document.exitFullscreen) {
                document.exitFullscreen()
            } else if (document.webkitCancelFullScreen) {
                document.webkitCancelFullScreen()
            } else if (document.mozCancelFullScreen) {
                document.mozCancelFullScreen()
            } else if (document.msExitFullscreen) {
                document.msExitFullscreen()
            }
            element.style.userSelect = 'none';
        }else{
            console.log('全屏')
            if (element.requestFullscreen) {
                element.requestFullscreen()
            } else if (element.webkitRequestFullScreen) {
                element.webkitRequestFullScreen()
            } else if (element.mozRequestFullScreen) {
                element.mozRequestFullScreen()
            } else if (element.msRequestFullscreen) {
                // IE11
                element.msRequestFullscreen()
            }
        }
        isFull.value = !isFull.value

    }

    //倍速
    const isSpeedListVisible = ref(false)
    const speedTimeId = ref(null)
    const currentSpeed = ref(1.0)
    const speedList = ['2', '1.5', '1.25', '1', '0.75', '0.5'];
    //倍速显隐
    const showSpeedList = () => {
        isSpeedListVisible.value = true;
        clearTimeout(speedTimeId.value)
    }
    const hideSpeedList = () => {
        speedTimeId.value = setTimeout(()=>{
            isSpeedListVisible.value = false;
        },500)

    }
    //改变视频播放速度
    const changeSpeed = (speed)=>{
        currentSpeed.value = speed
        isSpeedListVisible.value = false;
        video.playbackRate = speed
        clearTimeout(speedTimeId.value)
    }

    //音量调节
    const volumeStates = ref({
        mute: {
            icon: '../../videoList/image/noVolume.png',
            value: 0, // 表示静音状态
        },
        low: {
            icon: '../../videoList/image/lowVolume.png',
            value: 0.3, // 表示低音量状态
        },
        high: {
            icon: '../../videoList/image/highVolume.png',
            value: 1, // 表示高音量状态
        },
    })
    const volume = ref(1.0)
    const isMuted = ref(false)
    const volumeTimeId = ref(null)
    const isVolumeBarVisible = ref(false)
    const refVolumeBar = ref(null)
    // 调整前的音量，用于静音恢复
    const prevVolume = ref(null)
    //拖动
    const dragState = ref({
        dragging: false,
        startY: 0,
        startVolume: 0,
    })

    const nowVolume = computed(()=> (volume.value * 100).toFixed(0))
    const volumePercent = computed(() => `${volume.value * 100}%`)
    //静音
    const toggleMute = () => {
        console.log('toggleMute')
        isMuted.value = !isMuted.value;
        if (isMuted.value) {
            prevVolume.value = volume.value;
            volume.value = 0;
        } else {
            volume.value = prevVolume.value || 1.0;
        }
    }
    //最后设置音量
    const setVolume = (event) => {
        if (dragState.value.dragging) return;
        const volumeBar = event.currentTarget;
        const clickY = event.clientY - volumeBar.getBoundingClientRect().top;
        const percent = clickY / volumeBar.clientHeight;
        // 限制音量值在 0 到 1 之间
        volume.value = Math.max(0, Math.min(1, 1.0 - percent));
        prevVolume.value = isMuted.value ? 1.0 : volume.value;
    }
    //拖动
    const startDrag = (event) => {
        if (isMuted.value) toggleMute()
        const volumeBar = event.currentTarget;
        dragState.value.dragging = true;
        dragState.value.startY = event.clientY;
        dragState.value.startVolume = volume.value;
        volumeBar.addEventListener('mousemove', dragVolume)
        volumeBar.addEventListener('mouseup', endDrag)
    }
    const dragVolume = (event) => {
        if (dragState.value.dragging) {
            const volumeBar = event.currentTarget;
            const deltaY = event.clientY - dragState.value.startY;
            const totalHeight = volumeBar.clientHeight;
            const changePercent = deltaY / totalHeight;
            const newVolume = dragState.value.startVolume - changePercent;
            volume.value = Math.max(0, Math.min(1, newVolume))
        }
    }
    const endDrag = () => {
        if (dragState.value.dragging) {
            dragState.value.dragging = false;
            const volumeBar = refVolumeBar.value;
            volumeBar.removeEventListener('mousemove', dragVolume)
            volumeBar.removeEventListener('mouseup', endDrag)
        }
    }
    //音量条显隐
    const showVolumeBar = () => {
        isVolumeBarVisible.value = true;
        clearTimeout(volumeTimeId.value)
    }

    const hideVolumeBar = () => {
        volumeTimeId.value = setTimeout(()=>{
            isVolumeBarVisible.value = false;
        },500)
    }




    onBeforeUnmount(()=>{
            clearTimeout(speedTimeId.value)
    })
</script>

<style scoped>
.container{
    position: relative;
}
.fullscreen {
    user-select: none;
}
.container::before{
    content: "";
    background-image: url("../../videolist/image/bg2.png");
    background-size: cover;
    filter: blur(10px);
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
}
.videoBox{
    position: relative;
    max-width: 100%;
    height: calc(100vh - 50px);
    margin: 0 auto;
}
#video{
    height: calc(100% - 50px);
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
.controlBox{
    width: 100%;
    position: fixed;
    bottom: 20px;
}
.progress{
    min-width: 1020px;
    height: 3px;
    background-color: #999999;
    border-radius: 3px;
    position: relative;
}
.progress .now{
    width: 0;
    height: 3px;
    background-color: #dc143c;
    border-radius: 3px;
    transition: width 500ms linear;
    position: absolute;
    top: 0;
    left: 0;
}
.progress:hover,.progress:hover .now{
    height: 10px;
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
    width: 80px;
    color: #ffffff;
    text-align: center;
    padding: 0 5px;
    cursor: default;
    position: relative;
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
    z-index: 50;
}
.speedList div:hover,.active{
    background-color: #dc143c;
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
    padding: 0 5px;
    cursor: default;
    position: relative;
}
.volume .volumeBox{
    width: 20px;
    height: 20px;
}
.volumeBar{
    width: 40px;
    height: 130px;
    border: solid 1px #999999;
    border-radius: 4px;
    position: absolute;
    bottom: 25px;
    left: 0;

}
.volumeText{
    font-size: 12px;
    text-align: center;
}
.barBox{
    height: 100px;
    width: 100%;
    position: absolute;
    bottom: 5px;
    left: 0;
    z-index: 50;
}
.slider{
    height: 100px;
    width: 6px;
    margin: 0 auto;
    border: solid 1px #999999;
    border-radius: 5px;
    position: absolute;
    bottom: 5px;
    left: 50%;
    transform: translate(-3px,0);
    z-index: 50;
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


</style>
