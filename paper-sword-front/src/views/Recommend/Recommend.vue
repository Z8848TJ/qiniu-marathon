<template>
    <div class="recommend">
        <Header></Header>
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
        <LoginRegister class="loginRegisterBox" v-if="store.state.showLoginRegister"></LoginRegister>
    </div>
</template>

<script setup>
    import {ref,onMounted} from 'vue'
    import { Swiper, SwiperSlide } from 'swiper/vue'
    import 'swiper/css'
    import {Autoplay,Mousewheel,Keyboard} from 'swiper/modules'
    import {useStore} from 'vuex'

    const store = useStore()

    import VideoPlayer from '../../components/VideoPlayer.vue'
    import LoginRegister from '../../components/LoginRegister.vue'


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
.swiper{
    margin: 0;
}
.loginRegisterBox{
    position: absolute;
    top: 0;
    left: 0;
    background-color: rgba(0, 0, 0, 0.8);
    z-index: 2;
}
</style>
