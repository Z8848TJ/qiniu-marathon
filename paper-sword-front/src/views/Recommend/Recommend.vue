<template>
    <div class="recommend">
        <Header @showLogin="showLogin"></Header>
        <Swiper
                @swiper="onSwiper" @slideChange="onSlideChange" :class="{'swiper-no-swiping':isDragVolume}"
                :modules="modules"
                :direction="'vertical'"
                mousewheel
                keyboard
        >
            <swiper-slide
                    v-for="(video,index) of videos"
                    :key="video.id"
            >
<!--                @mousedown="onMouseDown"-->
                    <VideoPlayer
                            v-if="shouldRenderVideo(index)"
                            :source="video.source"
                            @dragVolume="changeVolume"
                            :isPlaying="currentIndex === index"
                    >
                    </VideoPlayer>
            </swiper-slide>
        </Swiper>

<!--            <VideoPlayer-->
<!--                    :source="videos[1].source"-->
<!--                    @dragVolume="changeVolume"-->
<!--                ></VideoPlayer>-->
        <LoginRegister class="loginRegisterBox" @close="closeLogin" @login="login" v-if="isShow"></LoginRegister>
    </div>
</template>

<script setup>
    import {ref,onMounted} from 'vue'
    import { Swiper, SwiperSlide } from 'swiper/vue'
    import 'swiper/css'
    import {Autoplay,Mousewheel,Keyboard} from 'swiper/modules'

    import VideoPlayer from '../../components/VideoPlayer.vue'
    import LoginRegister from '../../components/LoginRegister.vue'


    //登录注册
    const isShow = ref(false)
    const showLogin = ()=>{
        isShow.value = true
    }
    const closeLogin = ()=>{
        isShow.value = false
    }
    const login = ()=>{
        isShow.value = false
        isLogin.value = true
    }

    //视频
    let mySwiper = null
    const modules = [Autoplay,Mousewheel,Keyboard]

    const onSwiper = (swiper)=>{
        mySwiper = swiper
    }

    const videos = ref([
        { id: 1, source: './videolist/1-1.mp4' },
        { id: 2, source: './videolist/1-2.mp4' },
        { id: 3, source: './videolist/1-3.mp4' },
        { id: 4, source: 'http://s38keg0f3.hb-bkt.clouddn.com/test.mp4?e=1698568323&token=51Qm5EEd37-kTBM5STG7Wec0jE8f1t6d-te6G20o:2pYiAbg5Aa8_1_FbabkrJQgOevc=' },
        { id: 5, source: 'http://s38keg0f3.hb-bkt.clouddn.com/%E6%96%97%E7%A0%B4-1.mp4?e=1698568348&token=51Qm5EEd37-kTBM5STG7Wec0jE8f1t6d-te6G20o:y5RuRNK0D-21AW8UY3b8J-wtaY8=' },
        { id: 6, source: 'http://s38keg0f3.hb-bkt.clouddn.com/%E6%96%97%E7%A0%B4-2.mp4?e=1698568356&token=51Qm5EEd37-kTBM5STG7Wec0jE8f1t6d-te6G20o:g6sSlwGO65YZ9v7wcQ7eSDY4vKQ=' },

    ])

    //切换视频
    const currentIndex = ref(0)

    const onSlideChange = (e)=>{
        currentIndex.value = e.activeIndex
    }

    //拖动音量条不会改变视频
    const isDragVolume = ref(false)
    const changeVolume = (value)=>{
        isDragVolume.value = value
    }

    //同时只渲染三个视频
    const shouldRenderVideo = (index) => {
        // 根据 currentIndex 和视频的索引判断是否需要渲染视频组件
        return (
            Math.abs(index - currentIndex.value) <= 1 ||
            index === currentIndex.value
        )
    }

    // onMounted(() => {
    //
    // })


</script>

<style scoped>
.recommend{
    /*width: 100%;*/
    /*!*height: 100vh;*!*/
    background-color: #666666;
    /*position: relative;*/
    width: 100%;
    height: 100vh;
    display: flex;
    flex-direction: column;
}
.header {
    height: 50px;
    background-color: #333;
    color: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
}
.videoContainer {
    /*height: 100vh;*/
    height: calc(100vh - 50px);
    /*overflow-y: auto;*/
    display: flex;
    flex-direction: column;
    position: relative;
}

.video {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    display: none;
}

.video.active {
    display: block;
}




.loginRegisterBox{
    position: absolute;
    top: 0;
    left: 0;
    background-color: rgba(0, 0, 0, 0.8);
}
</style>
