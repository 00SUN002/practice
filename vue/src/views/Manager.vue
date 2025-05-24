<template>
  <div>
    <div style="height: 60px; display: flex;">
      <div style="width: 240px; display: flex; align-items: center; padding-left: 20px; background-color: #3a456b">
        <img style="width: 40px; height: 40px; border-radius: 50%" src="@/assets/imgs/logo.png" alt="">
        <span style="font-size: 20px; font-weight: bold; color: #ffffff; margin-left: 5px">标题</span>
      </div>
      <div style="flex: 1; border-bottom: 1px solid #ddd"></div>
      <div style="width: fit-content; padding-right: 20px; display: flex; align-items: center;  border-bottom: 1px solid #ddd">
        <el-dropdown>
          <div style="display: flex; align-items: center">
            <img style="width: 40px; height: 40px; border-radius: 50%" src="@/assets/imgs/avatar.jpg" alt="">
            <span style="margin-left: 5px">管理员</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>个人信息</el-dropdown-item>
              <el-dropdown-item>修改密码</el-dropdown-item>
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
              <el-icon><location /></el-icon>
              <span>用户管理</span>
              </template>
            <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
            <el-menu-item index="/manager/user">用户信息</el-menu-item>
            </el-sub-menu>
        </el-menu>
      </div>

      <div style="flex:1; width: 0; padding: 10px; background-color: #f2f4ff">
        <RouterView />
      </div>

    </div>

  </div>
</template>

<script setup>
import router from "@/router/index.js";

const logout = () => {
  localStorage.removeItem('user');
  location.href = "/login";
}

let userStr = localStorage.getItem("user");
if(userStr){
  let user = JSON.parse(userStr);
  if(!user.id){
    location.href = "/login";
  }
}
else{
  location.href = "/login";
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