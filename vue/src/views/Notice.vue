<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="load" style="width: 260px; margin-right: 5px" v-model="data.title" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px"  v-if="data.user.role === 'ADMIN'">
      <el-button type="primary" @click="handleAdd">新 增</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" :header-cell-style="{ color: '#333',backgroundColor: '#eaf4ff' }">
        <el-table-column prop="title" label="公告标题" />
        <el-table-column prop="content" label="公告内容" />
        <el-table-column prop="time" label="发布时间" />
        <el-table-column label="操作" width="100"  v-if="data.user.role === 'ADMIN'">
          <template #default="scope">
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[5,10,20]"
          :total="data.total"
          @current-change="load"
          @size-change="load"
      />
    </div>

    <el-dialog v-model="data.formVisible" title="公告信息" width="30%" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 10px 0">
        <el-form-item prop="title" label="公告标题">
          <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入公告标题"/>
        </el-form-item>
        <el-form-item prop="content" label="公告内容">
          <el-input type="textarea" :rows="3" v-model="data.form.content" autocomplete="off" placeholder="请输入公告内容"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import {Search} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import { reactive,ref } from "vue";
import {ElMessage, ElMessageBox} from "element-plus";

const formRef = ref()

const data = reactive({
  user:JSON.parse(localStorage.getItem("user") || '{}'),
  title: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  form: {},
  formVisible: false,
  rules: {
    title: [
      {required: true, message: '请输入公告标题', trigger: 'blur'},
    ],
    content: [
      {required: true, message: '请输入公告内容', trigger: 'blur'},
    ],
  }
})

const load = () => {
  request.get('/notice/selectPage',{
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
    }
  }).then(res => {
    if (res.code === '200'){
      data.tableData = res.data?.list
      data.total = res.data?.total
    }
    else{
      ElMessage.error(res.msg)
    }
  })
}

load()

const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const add = () => {
  request.post('/notice/add',data.form).then(res => {
    if (res.code === '200'){
      ElMessage.success('新增成功')
      data.formVisible = false
      load()
    }
    else{
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.post('/notice/update',data.form).then(res => {
    if (res.code === '200'){
      ElMessage.success('更新成功')
      data.formVisible = false
      load()
    }
    else{
      ElMessage.error(res.msg)
    }
  })
}

const save = () => {
  formRef.value.validate((valid) => {
    if (valid){
      data.form.id ? update() : add()
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm('确认删除吗？', "提示", {type: "warning"}).then(res => {
    request.delete('/notice/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("删除成功")
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const reset = () => {
  data.title = null;
  load()
}
</script>

<style scoped>

</style>