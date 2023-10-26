<template>
    <div class="container">
        <div class="header">
            <div class="group1">
                <div>搞子剑</div>
                <div>推荐</div>
                <div>关注</div>
                <div>朋友</div>
                <div>我的</div>
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
                <div class="avatar">头像</div>
            </div>
        </div>
    </div>
</template>

<script setup>



</script>

<style scoped>
.container{
    height: 50px;
    width: 100%;
    min-width: 1020px;
}
.header{
    display: flex;
    justify-content: space-between;
}
.group1,.group2,.searchBox{
    display: flex;
    margin: 10px 20px;
}
.group1 div{
    margin: 0 15px;
    font-size: 18px;
    line-height: 30px;
}
.searchBox {
    position: relative;
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
    color: #555555 ;
    background-color: #999999;
}

.search-icon {
    width: 20px;
    height: 20px;
    margin: 0 5px;
}

.search-text {
    color: #ffffff;
    margin: 0 5px;
    font-size: 16px;
    line-height: 30px;
}
.group2 div{
    width: 40px;
    height: 40px;
    text-align: center;
    margin: 0 10px;
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
    background-color: red;
}
</style>
