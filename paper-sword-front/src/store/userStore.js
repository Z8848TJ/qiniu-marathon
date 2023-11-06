import { createStore } from 'vuex'

const store = createStore({
    state: {
        videos:[],                  //视频
        showLoginRegister : false,  //是否展示登录注册组件
        isLogin: false,             //是否登录
        userId:0,                  //用户id
        avatar:'',                  //用户头像
        commit:[]                   //评论
    },
    mutations: {
        initVideos(state,value){
            state.videos = value
        },
        addVideos(state,value){
            state.videos.push(...value)
        },
        showLoginRegister(state,value){
          state.showLoginRegister = value
        },
        isLogin(state) {
            state.isLogin = true
        },
        changeLog(state,value){
            state.isLogin = value
        },
        setAvatar(state,value){
            state.avatar = value.header
            state.userId = value.id
        }
    },

});

export default store;
