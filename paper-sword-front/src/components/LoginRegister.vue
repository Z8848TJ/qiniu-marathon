<template>
    <div class="container">
        <div class="loginBox">
            <div class="logo">
                <img src="/logo.png" alt="TikTok Logo" />
            </div>
            <div class="form">
                <div class="closeBox" @click="closeLoginRegister">
                    <img src="/close.png" alt="">
                </div>
                <div class="block" v-if="!isRegister"></div>
                <h2>{{ isRegister ? '注册' : '登录' }}</h2>
                <input class="username" v-model="username" type="text" v-if="isRegister" placeholder="请输入用户名">
                <div class="inputBox">
                    <input v-model="email" type="text" @focus="clearError('email')" @blur="validate('email')" placeholder="请输入邮箱" />
                    <div class="sendCode" v-if="isRegister && !countTime" @click="sendCode">发送验证码</div>
                    <div class="sendCode" v-if="countTime" @click="sendCode" disabled>{{countDown}} 秒后重试</div>
                    <div class="error" v-if="emailError">{{ emailError }}</div>
                </div>
                <input class="code" v-model="code" type="text" v-if="isRegister" placeholder="请输入验证码">
                <div class="inputBox">
                    <input v-model="password" type="password" @focus="clearError('password')" @blur="validate('password')" placeholder="请输入密码" />
                    <div class="error" v-if="passwordError">{{ passwordError }}</div>
                </div>
                <button @click="handleLogin">{{ isRegister ? '注册' : '登录' }}</button>
            </div>
            <div class="switch">
                <span>{{ isRegister ? '已有账号？' : '没有账号？' }}</span>
                <a @click="toggleForm">{{ isRegister ? '登录' : '立即注册' }}</a>
            </div>
        </div>
        <div class="alert">
            <el-alert title="登录成功！" type="success" center show-icon v-if="success"/>
        </div>
    </div>
</template>

<script setup>
    import {ref, onBeforeUnmount, defineComponent} from 'vue'
    import {GetAction, PostAction} from '../util/api'
    import {useStore} from 'vuex'
    import {ElAlert,ElTag} from 'element-plus'
    import 'element-plus/dist/index.css';


    const store = useStore()
    const username = ref('')
    const email = ref('')
    const emailError = ref('')
    const code = ref('')
    const password = ref('')
    const passwordError = ref('')
    const isRegister = ref(false)
    defineComponent(ElAlert)

    const success = ref(false)
    const successTimeId = ref(null)
    const handleLogin = () => {
        //注册信息
        const registerParams = {
            username: username.value,
            email: email.value,
            code: code.value,
            password: password.value,
        }
        //登录信息
        const loginParams ={
            email: email.value,
            password: password.value,
        }
        if (isRegister.value) {
            console.log('注册:', registerParams)
            PostAction('auth/register',registerParams).then((res)=>{
                console.log(res)
                if(res.code===200){
                    isRegister.value = false
                }
            })
        } else {
            console.log('登录:', loginParams)
            PostAction('auth/login',loginParams).then((res)=>{
                console.log(res)
                localStorage.setItem('token',res.data.token)
                store.commit('setAvatar',res.data.header)
                store.commit('changeLog',true)
                success.value = true
                successTimeId.value = setTimeout(()=>{
                    store.commit('showLoginRegister',false)
                },500)
            })

        }
    }
    //切换
    const toggleForm = () => {
        isRegister.value = !isRegister.value
        email.value = ''
        password.value = ''
    }

    //邮箱和密码的验证
    const validate = (arg) => {
        if(arg === 'email'){
            if (!email.value) {
                emailError.value = '输入邮箱不能为空！'
            } else if (!isValidEmail(email.value)) {
                emailError.value = '无效的邮箱地址'
            }
        }else if(arg === 'password'){
            if (!password.value) {
                passwordError.value = '密码不能为空！'
            } else if (!isValidPassword(email.value)) {
                passwordError.value = '密码应当不少于8位，且为数字和字母的组合'
            }
        }
    }
    //正则判断
    const isValidEmail = (email) => {
        const emailRegex = /^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$/
        return emailRegex.test(email)
    }
    const isValidPassword = (password)=>{
        const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d).{8,}$/;
        return passwordRegex.test(password)
    }
    const clearError = (field) => {
        if (field === 'email') {
            emailError.value = ''
        }else if(field === 'password'){
            passwordError.value = ''
        }
    }
    //发送验证码
    //倒计时
    const countDown = ref(60)
    const countTime = ref(null)

    const sendCode = ()=>{
        console.log(email.value)
        if(email.value !== ''){
            const params = {
                email:email.value,
                type:0
            }
            GetAction('auth/sendEmail', params).then((res)=>{
                console.log(res)
                if(res.code === 200){
                    clearInterval(countTime.value)
                    countTime.value = null
                }
            })
            countTime.value = setInterval(()=>{
                if(countDown.value >0){
                    countDown.value --
                }else{
                    clearInterval(countTime.value)
                    countTime.value = null
                }
            },1000)
        }else{
            emailError.value = '请输入邮箱！'
        }
    }


    //关闭
    const closeLoginRegister = ()=>{
        store.commit('showLoginRegister',false)
    }

    onBeforeUnmount(()=>{
        clearInterval(countTime.value)
        clearTimeout(successTimeId.value)
    })

</script>

<style scoped>
    .container {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        width: 100%;
        position: fixed;
        top: 0;
        left: 0;
        z-index: 50;
        background-color: rgba(55,55,55,0.8);
    }

    .loginBox {
        width: 500px;
        height: 500px;
        text-align: center;
        background: linear-gradient(to bottom, #ec9191, #333);
        border-radius: 8px;
        padding: 30px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
        position: relative;
        display: flex;
        flex-direction: column;
        justify-content: center;
    }

    .closeBox {
        position: absolute;
        top: 10px;
        right: 10px;
        cursor: pointer;
        color: #fff;
    }

    .closeBox img{
        width: 20px;
        height: 20px;
    }

    .logo img {
        width: 100px;
        height: 100px;
        margin: 0 auto;
    }

    .form h2 {
        margin: 20px 0;
        font-size: 24px;
        color: #333;
    }

    .block{
        height: 80px;
    }
    .username,.code{
        margin-bottom: 10px;
    }
    .inputBox{
        height: 50px;
        margin: 15px;
        position: relative;
    }
    .sendCode{
        position: absolute;
        top: 5px;
        right: 80px;
        height: 20px;
        padding: 5px;
        line-height: 20px;
        border-radius: 2px;
        background-color: gray;
        cursor: pointer;
    }
    input {
        width: 300px;
        padding: 12px;
        border: 1px solid #ddd;
        border-radius: 4px;
    }
    .error {
        width: 300px;
        height: 10px;
        color: red;
        margin: 3px auto;
        font-size: 12px;
        text-align: left;
    }
    button {
        width: 330px;
        padding: 12px;
        margin-top: 20px;
        background: #333;
        color: #fff;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    .switch {
        margin: 20px 0;
    }

    a {
        color: #ffffff;
        text-decoration: underline;
        cursor: pointer;
    }

    a:hover {
        color: blue;
    }
    .alert{
        width: 200px;
        position: fixed;
        top: 10%;
        left: 50%;
        transform: translate(-100px,0);
    }

    .el-alert {
        margin: 20px 0 0;
    }
    .el-alert:first-child {
        margin: 0;
    }
</style>
