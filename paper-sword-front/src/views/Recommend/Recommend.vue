<template>
    <div class="recommend">
        <Header @changeRoute="changeRoute"></Header>
        <div class="loading" v-if="loading">
            <div class="loading-spinner"></div>
            <p style="color: white">加载中...</p>
        </div>
        <Swiper
                @swiper="onSwiper" @slideChange="onSlideChange" :class="{'swiper-no-swiping':isDragVolume}"
                :modules="modules"
                :direction="'vertical'"
                mousewheel
                keyboard
                v-else
        >
            <swiper-slide
                    v-for="(video,index) of videos"
                    :key="video.id"
            >
<!--                @mousedown="onMouseDown"-->
                    <VideoPlayer
                            v-if="shouldRenderVideo(index)"
                            :source="video"
                            @dragVolume="changeVolume"
                            :isPlaying="currentIndex === index"
                    >
                    </VideoPlayer>
            </swiper-slide>
        </Swiper>
    </div>
    <LoginRegister v-if="store.state.showLoginRegister"></LoginRegister>
</template>

<script setup>
    import {ref,onMounted} from 'vue'
    import { Swiper, SwiperSlide } from 'swiper/vue'
    import 'swiper/css'
    import {Autoplay,Mousewheel,Keyboard} from 'swiper/modules'
    import {useRouter} from 'vue-router'
    import {useStore} from 'vuex'
    import {GetAction} from '../../util/api'
    import VideoPlayer from '../../components/VideoPlayer.vue'
    import LoginRegister from '../../components/LoginRegister.vue'

    const router = useRouter()
    const store = useStore()

    //视频
    let mySwiper = null
    const modules = [Autoplay,Mousewheel,Keyboard]

    const onSwiper = (swiper)=>{
        mySwiper = swiper
    }

    const videos = ref([])

    //切换视频
    const currentIndex = ref(0)
    const refVideo = ref(null)
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


    // loading
    const loading = ref(true);
    const initData = ()=>{
        loading.value = true
        console.log('初始化数据')
        if(router.currentRoute.value.name === 'following'){
            console.log('关注')

        }else {
            console.log('推荐')
            GetAction('video/recommend').then((res) => {
                console.log(res)
                videos.value = res.data.info
                loading.value = false
            })
        }
    }
    const changeRoute = (e)=>{
        initData()
    }

    onMounted(() => {
        initData()
    })




</script>

<style scoped>
.recommend{
    /*width: 100%;*/
    /*!*height: 100vh;*!*/
    /*background-color: #666666;*/
    /*position: relative;*/
    width: 100%;
    height: 100vh;
    background-image: url('/background.jpg');
    /*filter: blur(10px);*/
    background-size: cover;
    display: flex;
    flex-direction: column;
    overflow: hidden;
}
.swiper{
    margin: 0;
}
.loading {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
}

.loading-spinner {
    border: 4px solid rgba(255, 255, 255, 0.3);
    border-top: 4px solid #3498db;
    border-radius: 50%;
    width: 40px;
    height: 40px;
    animation: spin 2s linear infinite;
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}

</style>
