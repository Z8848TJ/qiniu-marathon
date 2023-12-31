import { createApp } from 'vue'

import App from './App.vue'
import router from './router/index'
import store from './store/userStore'


const app = createApp(App)
//组件
import Header from "./components/Header.vue"

app.component('Header',Header)

app.use(router).use(store).mount('#app')
// app.config.globalProperties.$axios = axios
