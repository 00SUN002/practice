<template>
  <div class="bg">
    <div style="width: 350px;background-color: #fff; border-radius: 5px;box-shadow: 0 0 10px rgba(0,0,0,0.1); padding: 20px 20px">
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <div style="margin: 10px 0; text-align: center; font-weight: bold; font-size: 24px">欢迎登录</div>
        <el-form-item prop="username">
          <el-input size="large" v-model="data.form.username" autocomplete="off" prefix-icon="User" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="large" show-password v-model="data.form.password" autocomplete="off" prefix-icon="Lock" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item prop="role">
          <el-select size="large" style="width: 100%" v-model="data.form.role" placeholder="请选择角色" aria-label="选择">
            <el-option label="管理员" value="ADMIN"></el-option>
            <el-option label="用户" value="USER"></el-option>
          </el-select>
        </el-form-item>
        <div style="margin-bottom: 20px">
          <el-button type="primary" size="large" style="width: 100%" @click="login">登录</el-button>
        </div>
        <div style="text-align: right">
          还没有账号？请<a style="color: #248243" href="/register">注册</a>
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

const formRef = ref()
const data = reactive({
  form: { role: 'ADMIN' },
  rules: {
    username: [
      { required:true, message:'请输入账号', trigger: 'blur'},
      { min:3, message:'账号最少3位', trigger: 'blur'}
    ],
    password: [
      { required:true, message:'请输入密码', trigger: 'blur'}
    ],
  }
})

const login = () => {
  formRef.value.validate((valid) => {
    if(valid){
      request.post('/login', data.form).then(res => {
        if(res.code === '200'){
          localStorage.setItem('user',JSON.stringify(res.data || {}))
          ElMessage.success('登录成功')
          router.push('/')
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
  background-image: url("@/assets/imgs/bg2.jpg");
  background-size: cover;
}
</style>