import { createApp } from 'vue'
// import './style.css'
// import axios from './util/axios'
import App from './App.vue'
import router from './router/index'

const app = createApp(App)
//组件
import Header from "./components/Header.vue"

app.component('Header',Header)


app.use(router).mount('#app')
// app.config.globalProperties.$axios = axios
