<template>
  <div class="card">
    <div style="font-size: 20px; margin-bottom: 20px">系统公告</div>
    <el-timeline style="max-width: 600px">
      <el-timeline-item :timestamp="item.time" color="#0bbd87" placement="top" v-for="item in data.noticeData">
        <el-card>
          <div style="font-size: 18px">{{ item.title }}</div>
          <p>{{ item.content }}</p>
        </el-card>
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user:JSON.parse(localStorage.getItem('user') || '{}'),
  noticeData: []
})

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    if(res.code === '200') {
      data.noticeData = res.data
      if (data.noticeData.length > 3){
        data.noticeData = data.noticeData.slice(0,3)
      }
    }
    else{
      data.noticeData = []
      ElMessage.error(res.msg)
    }
  })
}

loadNotice()
</script>