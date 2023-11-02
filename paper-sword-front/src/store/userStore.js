import { createStore } from 'vuex'

const store = createStore({
    state: {
        showLoginRegister : false,
        isLogin: false,
        userEmail: '',

    },
    mutations: {
        showLoginRegister(state,value){
          state.showLoginRegister = value
        },
        login(state, userData) {
            state.isLogin = true
            state.userEmail = userData.userEmail
        },
        isLogin(state) {
            state.isLogin = true
        },
        logout(state){
            state.isLogin = false
            state.userEmail = ''
        }
    },

});

export default store;
