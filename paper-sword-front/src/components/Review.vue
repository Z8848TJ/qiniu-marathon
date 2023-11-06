<template>
    <div class="comment">
        <div class="avatar">
            <img :src="source.headerUrl" alt="">
        </div>
        <div class="comment-content">
            <div class="user-info">
                <span class="user-name">{{source.username}}</span>
                <span class="comment-time">{{source.createTime}}</span>
            </div>
            <div class="comment-text">
                {{source.content}}
            </div>
            <div class="comment-actions">
<!--                <button @click="toggleLike" :class="{ liked: comment.liked }">-->
                <button @click="toggleLike" >
                    <i class="fas fa-heart"></i>
                    点赞
                </button>
                <button @click="reply">
                    <i class="fas fa-reply"></i>回复
                </button>
            </div>
        </div>
    </div>
</template>

<script setup>
    import {ref,onMounted,defineProps} from 'vue'
    import {useStore} from 'vuex'
    import {GetAction} from "../util/api";


    const store = useStore()
    const props = defineProps(['source'])

    const toggleLike = ()=> {
        // 在此方法中处理点赞的逻辑，可以使用 comment.id 来标识评论
        // 可以切换 comment.liked 状态，同时更新点赞数 comment.likes
    }

    const reply = ()=> {
        // 在此方法中处理回复评论的逻辑，可以打开回复框等
    }


    onMounted(()=>{
        console.log(props.source)

    })

</script>

<style scoped>
    .comment {
        display: flex;
        justify-content: center;
        min-height: 100px;
        width: 100%;
        color: #ffffff;
        /*background-color: red;*/
        margin: 10px 0;

    }

    .avatar {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        overflow: hidden;
        margin-right: 10px;
    }

    .avatar img {
        width: 50px;
        height: 50px;
    }

    .user-info {
        display: flex;
        align-items: center;
        margin-bottom: 5px;
    }

    .user-name {
        font-weight: bold;
        margin-right: 25px;
    }

    .comment-time {
        font-size: 12px;
    }

    .comment-text {
        width: 300px;
        margin-bottom: 10px;
    }

    .comment-actions {
        display: flex;
        align-items: center;
    }

    button {
        background: none;
        border: none;
        margin-right: 10px;
        cursor: pointer;
        display: flex;
        align-items: center;
        color: #ffffff;
    }

    button i {
        margin-right: 5px;
    }

    button.liked {
        color: #ed4956; /* 点赞后的颜色 */
    }

</style>
