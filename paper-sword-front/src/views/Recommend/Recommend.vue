<template>
    <div class="recommend">
        <Header @changeRoute="changeRoute"></Header>
        <div class="loading" v-if="loading">
            <div class="loading-spinner"></div>
            <p style="color: white">加载中...</p>
        </div>
        <Swiper
                @swiper="onSwiper" @slideChange="onSlideChange" :class="{'swiper':true,'swiper-no-swiping':!canToggleVideo}"
                :modules="modules"
                :direction="'vertical'"
                :mousewheel="canToggleVideo"
                keyboard
                v-else
        >
            <swiper-slide
                    v-for="(video,index) of store.state.videos"
                    :key="video.id"
            >
                <!--                @mousedown="onMouseDown"-->
                <VideoPlayer
                        v-if="shouldRenderVideo(index)"
                        :source="video"
                        @canToggleVideo="changeToggleVideo"
                        :isPlaying="currentIndex === index"
                        @fullscreen="toggleFullscreen"
                        :videoHeight="videoHeight"
                >
                </VideoPlayer>
            </swiper-slide>
        </Swiper>
    </div>
    <LoginRegister v-if="store.state.showLoginRegister"></LoginRegister>
</template>

<script setup>
    import {ref,onMounted,watch,computed} from 'vue'
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

    //拖动音量条(和滚动评论)不会改变视频
    const canToggleVideo = ref(true)
    const changeToggleVideo = (value)=>{
        canToggleVideo.value = value
    }
    watch(canToggleVideo,(newValue)=>{
        if(mySwiper){
            if(newValue){
                mySwiper.mousewheel.enable()
            }else{
                mySwiper.mousewheel.disable()
            }
        }
    },{
        immediate:true
    })

    //同时只渲染三个视频
    const shouldRenderVideo = (index) => {
        // 根据 currentIndex 和视频的索引判断是否需要渲染视频组件
        return (
            Math.abs(index - currentIndex.value) <= 1 ||
            index === currentIndex.value
        )
    }


    // loading
    const loading = ref(true)
    const initData = ()=>{
        loading.value = true
        console.log('初始化数据')
        if(router.currentRoute.value.name === 'recommend'){
            console.log('推荐')
            GetAction('/video/recommend').then((res) => {
                console.log('推荐的视频',res)
                store.commit('initVideos',res.data.info)
                loading.value = false
            })
        }else if(router.currentRoute.value.name === 'hot'){
            console.log('热门')
            GetAction('/video/hot', {begin: 0}).then((res)=>{
                console.log('热门',res)
                store.commit('initVideos',res.data.info)
                loading.value = false
            })
        }else {
            console.log('分类')
            // console.log(router.currentRoute)
            const params = {
                type:router.currentRoute.value.params.key
            }
            GetAction('/video/type',params).then((res)=>{
                console.log(res)
                store.commit('initVideos',res.data.info)
                loading.value = false
            })
        }
    }
    const changeRoute = (e)=>{
        initData()
    }

    //全屏
    const isFull = ref(false)
    const videoHeight = computed(()=>{
        if(isFull.value){
            return '100vh'
        }else{
            return 'calc(100vh - 50px)'
        }
    })
    const toggleFullscreen = function () {
        const element = document.querySelector('.swiper')
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
            element.style.userSelect = 'none'
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
