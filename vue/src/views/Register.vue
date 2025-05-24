<template>
  <div class="bg">
    <div style="width: 350px;background-color: #fff; border-radius: 5px;box-shadow: 0 0 10px rgba(0,0,0,0.1); padding: 20px 20px">
      <el-form status-icon ref="formRef" :model="data.form" :rules="data.rules">
        <div style="margin: 10px 0; text-align: center; font-weight: bold; font-size: 24px">注册</div>
        <el-form-item prop="username">
          <el-input size="large" v-model="data.form.username" autocomplete="off" prefix-icon="User" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="large" show-password v-model="data.form.password" autocomplete="off" prefix-icon="Lock" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input size="large" show-password v-model="data.form.confirmPassword" autocomplete="off" prefix-icon="Lock" placeholder="请确认密码"/>
        </el-form-item>
        <div style="margin-bottom: 20px">
          <el-button type="primary" size="large" style="width: 100%; background-color: #248243; border-color: #248243" @click="register">注册</el-button>
        </div>
        <div style="text-align: right">
          已有账号？去<a style="color: #274afa" href="/login">登录</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import request from "@/utils/request.js";

const validatePass = (rule, value, callback) => {
  if(value !== data.form.password){
    callback(new Error('两次密码不一致'))
  }
  else{
    callback()
  }
}

const formRef = ref()
const data = reactive({
  form: {},
  rules: {
    username: [
      { required:true, message:'请输入账号', trigger: 'blur'},
      { min:3, message:'账号最少3位', trigger: 'blur'}
    ],
    password: [
      { required:true, message:'请输入密码', trigger: 'blur'}
    ],
    confirmPassword: [
      { required:true, message:'请确认密码', trigger: 'blur'},
      { validator:validatePass, trigger: 'change' }
    ]
  }
})

const register = () => {
  formRef.value.validate((valid) => {
    if(valid){
      request.post('/register', data.form).then(res => {
        if(res.code === '200'){
          ElMessage.success('注册成功')
          router.push('/login')
        }
        else{
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
.bg {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  background-image: url("@/assets/imgs/bg1.jpg");
  background-size: cover;
}
</style>