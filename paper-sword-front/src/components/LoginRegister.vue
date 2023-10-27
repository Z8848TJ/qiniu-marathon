<template>
    <div class="container">
        <div class="loginBox">
            <div class="logo">
<!--                <img src="./assets/logo.png" alt="TikTok Logo" />-->
            </div>
            <div class="form">
                <div class="closeBox" @click="closeLoginRegister">
                    <img src="../../videolist/image/close.png" alt="">
                </div>
                <h2>{{ isRegister ? '注册' : '登录' }}</h2>
                <div class="inputBox">
                    <input v-model="email" type="email" @focus="clearError('email')" @blur="validateEmail" placeholder="请输入邮箱" />
                    <div class="error" v-if="emailError">{{ emailError }}</div>
                </div>
                <input v-model="password" type="password" placeholder="请输入密码" />
                <button @click="handleLogin">{{ isRegister ? '注册' : '登录' }}</button>
            </div>
            <div class="switch">
                <span>{{ isRegister ? '已有账号？' : '没有账号？' }}</span>
                <a @click="toggleForm">{{ isRegister ? '登录' : '注册' }}</a>
            </div>
        </div>
    </div>
</template>

<script setup>
    import { ref, defineEmits } from 'vue'
    import {GetAction} from '../util/api'


    const email = ref('')
    const emailError = ref('')
    const password = ref('')
    const isRegister = ref(false)

    const handleLogin = () => {
        const credentials = {
            email: email.value,
            password: password.value,
        }
        if (isRegister.value) {
            console.log('注册:', credentials)
        } else {
            console.log('登录:', credentials)
            GetAction('http://afcdth.natappfree.cc/system/hello').then((res)=>{
                console.log(res)

            })

        }
    }
    //切换
    const toggleForm = () => {
        isRegister.value = !isRegister.value
        email.value = ''
        password.value = ''
    }

    //邮箱验证
    const validateEmail = () => {
        // 简单的电子邮件验证示例
        if (!email.value) {
            console.log('空')
            emailError.value = '输入邮箱不能为空！'
        } else if (!isValidEmail(email.value)) {
            emailError.value = '无效的邮箱地址'
        }
    }
    const isValidEmail = (email) => {
        // 电子邮件验证正则表达式
        const emailRegex = /^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$/;
        return emailRegex.test(email);
    };
    const clearError = (field) => {
        if (field === 'email') {
            emailError.value = ''
        }
    }

    //关闭
    const emits = defineEmits(['close'])
    const closeLoginRegister = ()=>{
        emits('close')
    }

</script>

<style scoped>
    .container {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        position: relative;
        background-color: #fff;
    }

    .loginBox {
        width: 500px;
        height: 400px;
        text-align: center;
        background: linear-gradient(to bottom, #ec9191, #333);
        border-radius: 8px;
        padding: 30px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
        position: relative;
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

    .inputBox{
        height: 50px;
        margin: 15px;
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
        color: #333;
        text-decoration: underline;
        cursor: pointer;
    }

    a:hover {
        color: #555;
    }
</style>
