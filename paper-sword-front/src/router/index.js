import { createRouter, createWebHistory } from 'vue-router'

const Recommend = ()=> import('../views/Recommend/Recommend.vue')
const User = ()=> import('../views/User/User.vue')
const Favour = ()=> import('../views/User/components/favour.vue')
const Collect = ()=> import('../views/User/components/Collect.vue')
const Works = ()=> import('../views/User/components/Works.vue')
const Publish = ()=> import('../views/Publish/Publish.vue')

const routes = [
    {
        path: '/',
        name: 'recommend',
        component: Recommend
    },
    {
        path: '/following',
        name: 'following',
        component: Recommend
    },
    {
        path: '/friends',
        name: 'friends',
        component: Recommend
    },
    // {    path: '/',    name: 'user',    component: User,  },
    {
        path: '/user/self',
        name: 'user',
        component: User,
        children:[
            {
                path: '',
                name: 'favour',
                component: Favour
            },
            {
                path: 'collect',
                name: 'collect',
                component: Collect
            },
            {
                path: 'works',
                name: 'works',
                component: Works
            }
        ]
    },
    //其他用户
    {
        path: '/user/:userId',
        name: 'user-other',
        component: User,
        children: [
            {
                path: '',
                name: 'favour',
                component: Favour,
            },
            {
                path: 'collect',
                name: 'collect',
                component: Collect,
            },
            {
                path: 'works',
                name: 'works',
                component: Works,
            },
        ],
    },
    {
        path: '/publish',
        name: 'publish',
        component: Publish
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes:routes
})

export default router
