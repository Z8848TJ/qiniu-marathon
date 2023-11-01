<template>
    <div class="container">
        <div class="header">
            <div class="group1">
                <div class="logo" @click="navigate('/')">搞子剑</div>
                <div class="nav" @click="navigate('/')">推荐</div>
                <div class="nav" @click="navigate('/')">关注</div>
                <div class="nav" @click="navigate('/')">朋友</div>
                <div class="nav" @click="navigate('/')">我的</div>
                <div class="category" @mouseenter="showCateInfo" @mouseleave="hideCateInfo">
                    <div class="nav" @click="navigate('/')">分类</div>
                    <div class="nav-dropdown" v-if="showNav">
                        <!-- 分类导航内容 -->
                        <!-- 这里可以放你的分类导航菜单 -->
                        <ul>
                            <li>热门</li>
                            <li>体育</li>
                            <li>游戏</li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="searchBox">
                <input class="search-input" type="text" placeholder="你想搜什么">
                <img class="search-icon" src="../../videolist/image/search.png" alt="Search">
                <div class="search-text">搜索</div>
            </div>
            <div class="group2">
                <div class="nav">
                    <img class="letter" src="../../videolist/image/letter.png" alt="">
                    <span>消息</span>
                </div>
                <div class="nav" @click="navigate('/publish')">
                    <img class="contribute" src="../../videolist/image/contribute.png" alt="">
                    <span>投稿</span>
                </div>
                <div class="avatar" @click="showLogin" v-if="!store.state.isLogin">登录</div>
                <div class="avatar" v-else @mouseenter="showUserInfo" @mouseleave="hideUserInfo">
                    <img src="../../videolist/image/favicon.png" @click="toUser">
                    <div class="userInfo" v-if="showProfile">
                        <div class="user-name">Your Name</div>
                        <div class="userActions">
                            <button @click="editProfile">编辑资料</button>
                            <button @click="logout">退出登录</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</template>

<script setup>
    import {ref, onMounted,onBeforeUnmount} from 'vue'
    import {useRouter} from 'vue-router'
    import {useStore} from 'vuex'

    const router = useRouter()
    const store = useStore()

    const showLogin = ()=>{
        store.commit('showLoginRegister',true)
    }
    const toUser = ()=>{
        console.log('跳转我的页面')
        router.push('/user/self')
    }
    const navigate = (route)=>{
        router.push(route)
    }

    //分类
    const showNav = ref(false)

    const showCateInfo = ()=>{
        showNav.value = true
    }
    const hideCateInfo = ()=>{
        showNav.value = false
    }

    //用户
    //登录态
    const isLogin = ref(false)
    const showProfileId = ref(null)
    const showProfile = ref(false)
    const showUserInfo = ()=>{
        clearTimeout(showProfileId.value)
        showProfile.value = true
    }
    const hideUserInfo = () => {
        showProfileId.value = setTimeout(()=>{
            showProfile.value = false
        },500)
    }
    const logout = ()=>{
        showProfileId.value = setTimeout(()=>{
            localStorage.removeItem('token')
            showProfile.value = false
            store.commit('logout')
            store.state.isLogin = false
        },500)

    }



    onMounted(()=>{
        const userInfo = localStorage.getItem('token')
        if(userInfo){
            store.commit('isLogin')
        }
        console.log(store.state.isLogin)
    })
    onBeforeUnmount(()=>{
        clearTimeout(showProfileId.value)
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
.group1 div{
     /*margin-left: 30px;*/
     cursor: pointer;
}
.logo{
    width: 60px;
    font-size: 20px;
    margin-right: 10px;
}
.group1 .nav{
    min-width: 36px;
    margin: 0 10px;
    font-size: 18px;
    line-height: 30px;
}
.category{
    position: relative;
    z-index: 2;
}
.nav-dropdown{
    /*display: none;*/
    width: 240px;
    height: 150px;
    color: #999999;
    background-color: rgba(0,0,0,0.6);
    border-radius: 8px;
    position: absolute;
    top: 40px;
    left: -100px;
}
.nav-dropdown ul{
    list-style: none;
    padding: 0;
    display: flex;
}
.nav-dropdown li{
    width: 60px;
    height: 20px;
    margin: 10px;
    font-size: 16px;
    text-align: center;
    /*background-color: blue;*/
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
    cursor: pointer;
}

.search-text {
    min-width: 32px;
    color: #ffffff;
    margin: 0 5px;
    font-size: 16px;
    line-height: 30px;
    cursor: pointer;
}
.group2 .nav{
    width: 40px;
    height: 40px;
    text-align: center;
    margin-right: 10px;
    cursor: pointer;
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
    position: relative;
}
.avatar img{
    width: 40px;
    height: 40px;
    border-radius: 50%;
    transform: translate(0,-5px);
}
.userInfo {
    width: 300px;
    height: 150px;
    position: absolute;
    top: 40px;
    right: 0;
    background-color: red;
    border: 1px solid #999999;
    border-radius: 10px;
    padding: 10px;
    z-index: 2;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.user-name {
    font-weight: bold;
}

.userActions{
    display: flex;
    justify-content: flex-end;
}

.userActions button {
    margin-right: 10px;
    padding: 5px 10px;
    background-color: #007bff;
    color: #000000;
    border: none;
    cursor: pointer;
}
</style>
