<template>
  <div>
    <div style="height: 60px; display: flex;">
      <div style="width: 240px; display: flex; align-items: center; padding-left: 20px; background-color: #3a456b">
        <img style="width: 40px; height: 40px; border-radius: 50%" src="@/assets/imgs/logo.png" alt="">
        <span style="font-size: 20px; font-weight: bold; color: #ffffff; margin-left: 5px">标题</span>
      </div>
      <div style="flex: 1; display: flex; align-items: center; padding-left: 20px; border-bottom: 1px solid #ddd">
        <span style="margin-left: 5px; cursor: pointer" @click="router.push('/manager/home')">首页</span> / <span style="margin-left: 5px">{{ router.currentRoute.value.meta.name }}</span>
      </div>
      <div style="width: fit-content; padding-right: 20px; display: flex; align-items: center;  border-bottom: 1px solid #ddd">
        <el-dropdown>
          <div style="display: flex; align-items: center">
            <img v-if="data.user?.avatar" style="width: 40px; height: 40px; border-radius: 50%" :src="data.user?.avatar" />
            <img v-else style="width: 40px; height: 40px; border-radius: 50%" src="@/assets/imgs/default_avatar.png" alt="">
            <span style="margin-left: 5px">{{data.user?.name}}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/manager/person')">个人中心</el-dropdown-item>
              <el-dropdown-item @click="router.push('/manager/updatePassword')">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <div style="display: flex">

      <div style="width: 240px;">
        <el-menu router :default-openeds="['1']" :default-active="router.currentRoute.value.path" style="min-height: calc(100vh - 60px)">
          <el-menu-item index="/manager/home">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-sub-menu index="1">
            <template #title>
              <el-icon><Monitor /></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/manager/notice"  v-if="data.user.role === 'ADMIN'">系统公告</el-menu-item>
            <el-menu-item index="/manager/notice"  v-else>公告信息</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2" v-if="data.user.role === 'ADMIN'">
            <template #title>
              <el-icon><User /></el-icon>
              <span>用户管理</span>
              </template>
            <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
            <el-menu-item index="/manager/user">用户信息</el-menu-item>
            </el-sub-menu>
        </el-menu>
      </div>

      <div style="flex:1; width: 0; padding: 10px; background-color: #f2f4ff">
        <RouterView @update="update"/>
      </div>

    </div>

  </div>
</template>

<script setup>
import router from "@/router/index.js";
import { reactive } from "vue";

const data = reactive({
  user:JSON.parse(localStorage.getItem('user') || '{}')
})

const logout = () => {
  localStorage.removeItem('user');
  location.href = "/login";
}

const update = () => {
  data.user = JSON.parse(localStorage.getItem('user') || '{}')
}

</script>

<style>
.el-menu {
  background-color: #3a456b;
  border: none;
}
.el-sub-menu__title {
  background-color: #3a456b;
  color: #ddd;
}
.el-menu-item {
  height: 50px;
  color: #ddd;
}
.el-menu .is-active {
  background-color: #537bee;
  color: #fff;
}
.el-sub-menu__title:hover{
  background-color: #3a456b;
}
.el-menu-item:not(.is-active):hover {
  background-color: #537bee;
  color: #333;
}
.el-dropdown{
  cursor: pointer;
}
.el-tooltip_trigger {
  outline: none;
}
.el-menu--inline .el-menu-item{
  padding-left: 48px !important;
}
</style>