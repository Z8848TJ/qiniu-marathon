<template>
    <div class="container">
        <div class="header">
            <div class="group1">
                <div class="logo">搞子剑</div>
                <div class="nav">推荐</div>
                <div class="nav">关注</div>
                <div class="nav">朋友</div>
                <div class="nav">我的</div>
            </div>
            <div class="searchBox">
                <input class="search-input" type="text" placeholder="你想搜什么">
                <img class="search-icon" src="../../videolist/image/search.png" alt="Search">
                <div class="search-text">搜索</div>
            </div>
            <div class="group2">
                <div>
                    <img class="letter" src="../../videolist/image/letter.png" alt="">
                    <span>消息</span>
                </div>
                <div>
                    <img class="contribute" src="../../videolist/image/contribute.png" alt="">
                    <span>投稿</span>
                </div>
                <div class="avatar" @click="showLogin" v-if="!isLogin">登录</div>
                <div class="avatar" @click="toUser" v-else>
                    <img src="../../videolist/image/favicon.png" alt="">
                </div>
            </div>
        </div>

    </div>
</template>

<script setup>
    import {ref, defineEmits, onMounted} from 'vue'
    import {useRouter} from 'vue-router'

    const router = useRouter()

    const emits = defineEmits(['showLogin'])

    const showLogin = ()=>{
        emits('showLogin')
    }
    const toUser = ()=>{
        console.log('跳转用户页面')
        router.push('/user')

    }
    //登录态
    const isLogin = ref(false)
    onMounted(()=>{
        const userInfo = localStorage.getItem('token')
        if(userInfo){
            isLogin.value = true
        }
    })


</script>

<style scoped>
.container{
    height: 50px;
    width: 100%;
    /*min-width: 1020px;*/
}
.header{
    display: flex;
    justify-content: space-between;
}
.group1,.group2,.searchBox{
    display: flex;
    margin: 10px 10px;
}
.group1{
    margin-left: 30px;
}
.logo{
    width: 60px;
    font-size: 20px;
    margin-right: 10px;
}
.nav{
    min-width: 36px;
    margin: 0 10px;
    font-size: 18px;
    line-height: 30px;
}
.searchBox {
    min-width: 260px;
    display: flex;
    align-items: center;
    border: 3px solid #ccc;
    border-radius: 10px;
    height: 30px;
    width: 350px;
    overflow: hidden;
}

.search-input {
    border: none;
    outline: none;
    flex: 1;
    padding: 5px;
    color: #000000 ;
    background-color: #666666;
}
input::placeholder {
    color: #000000;
}

.search-icon {
    width: 20px;
    height: 20px;
    margin: 0 5px;
}

.search-text {
    min-width: 32px;
    color: #ffffff;
    margin: 0 5px;
    font-size: 16px;
    line-height: 30px;
}
.group2 div{
    width: 40px;
    height: 40px;
    text-align: center;
    margin-right: 10px;
}
.letter,.contribute{
    width: 20px;
    height: 20px;
}
.group2 span{
    display: block;
    font-size: 10px;
}

.avatar{
    height: 40px;
    width: 40px;
    line-height: 40px;
    border-radius: 50%;
    /*background-color: red;*/
    margin: 0 10px;
}
.avatar img{
    width: 40px;
    height: 40px;
    border-radius: 50%;
}
</style>
