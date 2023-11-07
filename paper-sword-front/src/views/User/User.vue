<template>
    <div class="user">
        <Header></Header>
        <div class="userContainer">
            <div class="userInfoBox">
                <div class="avatarBox">
                    <img src="/favicon.png" alt="User Avatar" class="avatar">
                    <div class="userInfo">
                        <h2>{{ userName }}</h2>
                        <div class="stats">
                            <span>关注：{{ followCount }}</span>
                            <span>|</span>
                            <span>粉丝：{{ followCount }}</span>
                            <span>|</span>
                            <span>获赞数：{{ likesCount }}</span>
                        </div>
                        <p>邮箱：{{ account }}</p>
                    </div>
                </div>
                <div class="settingBox">
                    <button class="setting" @click="logout">退出登录</button>
                </div>
<!--                <div class="settingBox" v-if="isCurrentUser">-->
<!--                    <button class="setting" @click="setting">资料设置</button>-->
<!--                </div>-->
<!--                <div class="controlsBox" v-else>-->
<!--                    <button class="control" @click="followUser">{{ isFollowing ? '已关注' : '关注' }}</button>-->
<!--                    <button class="control">私信</button>-->
<!--                    <button class="control">分享</button>-->
<!--                </div>-->
            </div>
            <div class="userContent">
                <div class="category">
                    <button v-for="category in categories" :key="category" @click="changeContent(category)">{{ category }}</button>
                </div>
                <hr>
                <div class="content">
                    <router-view></router-view>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>

    import {ref,onMounted,onBeforeUnmount} from 'vue'
    import {useRouter} from 'vue-router'
    import {useStore} from 'vuex'
    import {GetAction} from "../../util/api";

    const router = useRouter()
    const store = useStore()

    const userName = ref('luo')
    const followCount = ref(0)
    const followedCount = ref(0)
    const likesCount = ref(0)
    const account = ref('')
    const categories = ref([ '喜欢', '收藏','作品'])
    //切换分类
    const changeContent = (category)=>{
        if(category==='喜欢'){
            router.push('/user/self')
        }else if(category==='收藏'){
            router.push('/user/self/collect')
        }else if(category==='作品'){
            router.push('/user/self/works')
        }
    }

    //退出登录

    const logoutTime = ref(null)
    const logout = ()=>{
        logoutTime.value = setTimeout(()=>{
            localStorage.removeItem('token')
            store.commit('changeLog',false)
            store.state.isLogin = false
            router.push('/')
        },500)
    }

    // const isCurrentUser = ref(false) // 根据用户身份状态判断是否是当前用户
    // const isFollowing = ref(false) // 根据用户关注状态判断是否已关注
    // const followUser = () => {
    //     isFollowing.value = !isFollowing.value
    // }

    onMounted(()=>{
        GetAction('/user/mainInfo').then((res)=>{
            // console.log(res)
            followCount.value = res.data.followCount
            followedCount.value = res.data.followedCount
            likesCount.value = res.data.likeCount
            userName.value = res.data.user.username
            account.value = res.data.user.email
        })

    })

    onBeforeUnmount(()=>{
        clearTimeout(logoutTime.value)
    })


</script>

<style scoped>
    .user {
        width: 100%;
        background-color: #666666;
    }
    .userContainer{
        margin: 0 auto;
        width: 90%;
        height: calc(100vh - 100px);
    }

    .userInfoBox {
        display: flex;
        justify-content: space-between;
        margin-top: 50px;
    }

    .avatarBox {
        display: flex;
    }

    .avatar {
        width: 150px;
        height: 150px;
        border-radius: 50%;
        margin-right: 50px;
    }

    .userInfo {
        margin-top: 25px;
        display: flex;
        flex-direction: column;
    }

    .userInfo h2 {
        /*font-size: 24px;*/
        margin: 0;
    }

    .stats {
        margin-top: 10px;
    }

    .stats span {
        margin-right: 20px;
    }

    .settingBox {
        flex: 1;
        display: flex;
        justify-content: flex-end;
        align-items: center;
    }
    .setting{
        width: 100px;
        height: 40px;
        font-size: 20px;
        text-align: center;
        line-height: 40px;
        background-color: #999999;
        border: 1px solid #999999;
        border-radius: 5px;
        cursor: pointer;
    }
    .controlsBox{
        display: flex;
        justify-content: flex-end;
        align-items: flex-end;
    }
    .control{
        width: 100px;
        height: 40px;
        margin: 0 10px;
        font-size: 20px;
        text-align: center;
        line-height: 40px;
        background-color: #999999;
        border: 1px solid #999999;
        border-radius: 5px;
        cursor: pointer;
    }


    .userContent {
        margin-top: 60px;
    }

    .category {
        display: flex;
    }

    hr{
        border: 1px solid #999999;
    }

    .category button {
        background-color: #333;
        color: #fff;
        border: none;
        padding: 10px 20px;
        cursor: pointer;
        margin-right: 10px;
    }

    .category button:last-child {
        margin-right: 0;
    }

    .content {
        margin-top: 10px;
    }



</style>
