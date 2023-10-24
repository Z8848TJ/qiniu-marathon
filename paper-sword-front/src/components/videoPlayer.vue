<template>
    <div class="container">
        <video src="../assets/1-1.mp4" controls="controls" autoplay="autoplay" loop="loop"></video>
    </div>
</template>

<script>
    export default {
        name: "videoPlayer"
    }
</script>

<style scoped>
.container{
    width: 500px;
    height: 800px;
}
video{
    width: 100%;
    height: 100%;
}
</style>
