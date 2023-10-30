<template>
    <div class="user">
        <Header></Header>
        <div class="userContainer">
            <div class="userInfoBox">
                <div class="avatarBox">
                    <img src="../../../videolist/image/favicon.png" alt="User Avatar" class="avatar">
                    <div class="userInfo">
                        <h2>{{ userName }}</h2>
                        <div class="stats">
                            <span>关注：{{ followingCount }}</span>
                            <span>|</span>
                            <span>粉丝：{{ followersCount }}</span>
                            <span>|</span>
                            <span>点赞数：{{ likesCount }}</span>
                        </div>
                        <p>邮箱：{{ account }}</p>
                    </div>
                </div>
                <div class="controlsBox">
                    <button class="setting">资料设置</button>
                </div>
            </div>
            <div class="userContent">
                <div class="category">
                    <button v-for="category in categories" :key="category">{{ category }}</button>
                </div>
                <hr>
                <div class="content">
                    <div v-if="selectedCategory === '喜欢'">显示喜欢图片</div>
                    <div v-else-if="selectedCategory === '收藏'">显示收藏图片</div>
                    <div v-else-if="selectedCategory === '作品'">显示作品图片</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>


    import {ref} from 'vue'

    const userName = ref('luo')
    const followersCount = ref(1000)
    const followingCount = ref(500)
    const likesCount = ref(5000)
    const account = ref('2825787448@qq.com')
    const categories = ref([ '喜欢', '收藏','作品'])
    const selectedCategory = ref('喜欢')// 默认选中的分类



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

    .controlsBox {
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

    .search-input {
        width: 200px;
        height: 30px;
        padding: 5px;
        border: 1px solid #ccc;
        border-radius: 5px;
        margin-right: 10px;
    }

    .search-button {
        background-color: #333;
        color: #fff;
        border: none;
        padding: 5px 10px;
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

    /* 图片样式可以根据需求自定义 */

</style>
