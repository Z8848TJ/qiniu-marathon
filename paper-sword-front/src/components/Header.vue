<template>
    <div class="container">
        <div class="header">
            <div class="group1">
                <div class="logo" @click="navigate('/')">搞子剑</div>
                <div class="nav" :class="{ 'highlight': router.currentRoute.value.path === '/' }" @click="navigate('/')">推荐</div>
                <div class="nav" :class="{ 'highlight': router.currentRoute.value.path === '/hot' }" @click="navigate('/hot')">热门</div>
<!--                <div class="nav" @click="navigate('/friends')">朋友</div>-->
                <div class="nav" :class="{ 'highlight': router.currentRoute.value.path === '/user/self' }" @click="navigate('/user/self')">我的</div>
                <div class="category" @mouseenter="showCateInfo" @mouseleave="hideCateInfo">
                    <div class="nav">{{currentCate ===''?'分类':currentCate}}</div>
                    <div class="navDropdown" v-if="showNav">
                        <ul>
                            <li v-for="(cate,index) in cateList" @click="nav(cate)">{{cate.value}}</li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="searchBox">
                <input class="search-input" type="text" placeholder="你想搜什么">
                <img class="search-icon" src="/search.png" alt="Search">
                <div class="search-text">搜索</div>
            </div>
            <div class="group2">
                <div class="nav">
                    <img class="letter" src="/letter.png" alt="">
                    <span>消息</span>
                </div>
                <div class="nav" @click="navigate('/publish')">
                    <img class="contribute" src="/contribute.png" alt="">
                    <span>投稿</span>
                </div>
                <div class="avatar" @click="showLogin" v-if="!store.state.isLogin">登录</div>
                <div class="avatar" v-else @mouseenter="showUserInfo" @mouseleave="hideUserInfo">
                    <img :src="store.state.avatar" @click="toUser">
<!--                    <div class="userInfo" v-if="showProfile">-->
<!--                        <div class="user-name">Your Name</div>-->
<!--                        <div class="userActions">-->
<!--                            <button @click="editProfile">编辑资料</button>-->
<!--                            <button @click="logout">退出登录</button>-->
<!--                        </div>-->
<!--                    </div>-->
                </div>
            </div>
        </div>

    </div>
</template>

<script setup>
    import {ref,onMounted,onBeforeUnmount} from 'vue'
    import {useRouter} from 'vue-router'
    import {useStore} from 'vuex'
    import {GetAction} from "../util/api"

    const router = useRouter()
    const store = useStore()

    const showLogin = ()=>{
        store.commit('showLoginRegister',true)
    }
    const toUser = ()=>{
        // console.log('跳转我的页面')
        router.push('/user/self')
    }
    const navigate = (route)=>{
        if(route === '/user/self'){
            if(store.state.isLogin){
                router.push(route)
            }
        }else if(route === '/publish'){
            if(store.state.isLogin){
                router.push(route)
            }
        }else {
            router.push(route)
        }

    }
    const nav = (cate)=>{
        router.push({
            name:'category',
            params:{
                key:cate.key
            }
        })
        currentCate.value = cate.value
    }

    //分类
    const showNav = ref(false)

    const showCateInfo = ()=>{
        showNav.value = true
    }
    const hideCateInfo = ()=>{
        showNav.value = false
    }
    const cateList = ref([])
    const currentCate = ref('')

    //用户
    //登录态
    const isLogin = ref(false)
    // const showProfileId = ref(null)
    // const showProfile = ref(false)
    // const showUserInfo = ()=>{
    //     clearTimeout(showProfileId.value)
    //     showProfile.value = true
    // }
    // const hideUserInfo = () => {
    //     showProfileId.value = setTimeout(()=>{
    //         showProfile.value = false
    //     },500)
    // }
    // const logout = ()=>{
    //     showProfileId.value = setTimeout(()=>{
    //         localStorage.removeItem('token')
    //         showProfile.value = false
    //         store.commit('logout')
    //         store.state.isLogin = false
    //     },500)
    //
    // }

    //路由变化
    const emits = defineEmits(['changeRoute'])
    const currentRoute = ref('')
    router.afterEach((to)=>{
        // console.log(to)
        currentRoute.value = to.path
        emits('changeRoute',to.path)
    })

    //头像
    onMounted(()=>{
        // console.log(router.currentRoute.value.path)
        const userInfo = localStorage.getItem('token')
        if(userInfo){
            store.commit('isLogin')
            GetAction('/user/header').then((res)=>{
                // console.log(res)
                store.commit('setAvatar',res.data)
            })
        }
        GetAction('/video/videoLab').then((res)=>{
            // console.log(res)
            cateList.value = res.data.info
        })
    })
    onBeforeUnmount(()=>{
        // clearTimeout(showProfileId.value)
    })

</script>

<style scoped>
.container{
    height: 50px;
    width: 100%;
    /*padding: 10px 0;*/
}
.header{
    display: flex;
    justify-content: space-between;
}

.group1{
    height: 50px;
    display: flex;
    /*margin: 10px 10px;*/
}
.searchBox,.group2{
    display: flex;
    margin: 10px;
}
.group1{
    margin-left: 30px;
}
.group1 div{
     /*margin-left: 30px;*/
     cursor: pointer;
}
.logo{
    height: 50px;
    width: 70px;
    line-height: 50px;
    font-size: 22px;
    color: #ffffff;
    margin-right: 10px;
}
.group1 .nav{
    height: 50px;
    width: 70px;
    border-radius: 5px;
    min-width: 36px;
    /*margin: 0 10px ;*/
    font-size: 18px;
    line-height: 50px;
    color: #ffffff;
    text-align: center;
}
.highlight {
    background-color: rgba(100,100,100,0.6); /* 高亮时的样式 */
}
.category{
    position: relative;
    z-index: 2;
}
.navDropdown{
    /*display: none;*/
    width: 240px;
    min-height: 150px;
    color: #ffffff;
    background-color: rgba(0,0,0,0.3);
    border-radius: 8px;
    position: absolute;
    top: 50px;
    left: -100px;
}
.navDropdown ul{
    list-style: none;
    padding: 0;
    display: flex;
    flex-wrap: wrap;
}
.navDropdown li{
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
    color: #ffffff ;
    background-image: url('/background.jpg');
}
input::placeholder {
    color: #999999;
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
    color: #ffffff;
}

.avatar{
    height: 40px;
    width: 40px;
    line-height: 40px;
    border-radius: 50%;
    color: #ffffff;
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
    background-color: rgb(45, 44, 44);
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
