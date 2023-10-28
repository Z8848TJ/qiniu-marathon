import { createApp } from 'vue'
// import './style.css'
// import axios from './util/axios';
import App from './App.vue'
import router from './router/index'

const app = createApp(App)


app.use(router).mount('#app')
// app.config.globalProperties.$axios = axios;
