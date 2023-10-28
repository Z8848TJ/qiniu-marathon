import { createRouter, createWebHistory } from 'vue-router'

const Recommend = ()=> import('../views/Recommend/Recommend.vue')
const User = ()=> import('../views/User/User.vue')

const routes = [
    {    path: '/',    name: 'recommend',    component: Recommend  },
    // {    path: '/',    name: 'user',    component: User,  },
    {    path: '/user',    name: 'user',    component: User,  },
]

const router = createRouter({
    history: createWebHistory(),
    routes:routes
})

export default router
