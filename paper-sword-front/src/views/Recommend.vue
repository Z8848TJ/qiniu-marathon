<template>
    <div class="container">
        <Header @login="login"></Header>
        <VideoPlayer></VideoPlayer>
        <LoginRegister class="loginRegisterBox" @close="close" v-if="isLogin"></LoginRegister>
    </div>
</template>

<script setup>
    import {ref,defineProps } from 'vue'

    import Header from "../components/Header.vue";
    import VideoPlayer from '../components/VideoPlayer.vue'
    import LoginRegister from '../components/LoginRegister.vue'

    const props = defineProps(['parentMethod'])
    //登录注册
    const isLogin = ref(false)
    //打开关闭
    const login = ()=>{
        isLogin.value = true
    }
    const close = ()=>{
        isLogin.value = false
    }




</script>

<style scoped>
.container{
    width: 100%;
    /*height: 100vh;*/
    background-color: #999999;
    position: relative;
}
.loginRegisterBox{
    position: absolute;
    top: 0;
    left: 0;
    background-color: rgba(0, 0, 0, 0.8);
}
</style>
