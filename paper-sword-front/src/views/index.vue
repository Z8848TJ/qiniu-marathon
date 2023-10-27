<template>
    <div class="container">
        <Header></Header>
        <VideoPlayer></VideoPlayer>
    </div>
    <div></div>
</template>

<script setup>
    import Header from "../components/Header.vue";
    import VideoPlayer from '../components/VideoPlayer.vue'



</script>

<style scoped>
.container{
    width: 100%;
    /*height: 100vh;*/
    background-color: #999999;
}
</style>
