<template>
    <div class="recommend">
        <Header @showLogin="showLogin"></Header>
        <VideoPlayer></VideoPlayer>
        <LoginRegister class="loginRegisterBox" @close="closeLogin" @login="login" v-if="isShow"></LoginRegister>
    </div>
</template>

<script setup>
    import {ref} from 'vue'

    import Header from "../../components/Header.vue";
    import VideoPlayer from '../../components/VideoPlayer.vue'
    import LoginRegister from '../../components/LoginRegister.vue'
    import User from '../User/User.vue'

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



</script>

<style scoped>
.recommend{
    width: 100%;
    /*height: 100vh;*/
    background-color: #666666;
    position: relative;
}
.loginRegisterBox{
    position: absolute;
    top: 0;
    left: 0;
    background-color: rgba(0, 0, 0, 0.8);
}
</style>
