<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="load" style="width: 260px; margin-right: 5px" v-model="data.name" placeholder="请输入查询名称" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" @click="handleAdd">新 增</el-button>
      <el-button type="danger" @click="deleteBatch">批量删除</el-button>
      <el-button type="info" @click="exportData">批量导出</el-button>
      <el-upload
          style="display: inline-flex; margin-left: 10px"
          :show-file-list="false"
          :http-request="importData"
          :on-success="handleImportSuccess"
      >
        <el-button type="success">批量导入</el-button>
      </el-upload>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" @selection-change="handleSelectChange"
                :header-cell-style="{ color: '#333',backgroundColor: '#eaf4ff' }">
        <el-table-column type="selection" width="55" />
        <el-table-column label="头像" width="100">
          <template #default="scope">
            <el-image
                v-if="scope.row.avatar"
                :src="scope.row.avatar"
                :preview-src-list="[scope.row.avatar]"
                :preview-teleported="true"
                style="width: 40px; height: 40px; border-radius: 50%; display: block"
            />
          </template>
        </el-table-column>
        <el-table-column prop="username" label="账号" />
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="phone" label="电话" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column label="操作" width="100">
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
  </div>

  <div>
    <el-dialog v-model="data.formVisible" title="用户信息" width="30%" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 10px 0">
        <el-form-item prop="username" label="账号">
          <el-input v-model="data.form.username" autocomplete="off" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item prop="name" label="名称">
          <el-input v-model="data.form.name" autocomplete="off" placeholder="请输入名称"/>
        </el-form-item>
        <el-form-item prop="phone" label="电话">
          <el-input v-model="data.form.phone" autocomplete="off" placeholder="请输入电话"/>
        </el-form-item>
        <el-form-item prop="email" label="邮箱">
          <el-input v-model="data.form.email" autocomplete="off" placeholder="请输入邮箱"/>
        </el-form-item>
        <el-form-item prop="avatar" label="头像">
          <el-upload
              action="http://localhost:8080/files/upload"
              :headers="{ token:data.user.token }"
              :on-success="handleAvatarSuccess"
              list-type="picture"
          >
            <el-button type="primary">上传头像</el-button>
          </el-upload>
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
import { reactive, ref } from "vue";
import { Search } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem("user") || '{}'),
  username: null,
  name: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  formVisible: false,
  form: {},
  rules: {
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' }
    ],
    name: [
      { required: true, message: '请输入名称', trigger: 'blur' }
    ],
    phone: [
      { required: true, message: '请输入电话', trigger: 'blur' }
    ],
    email: [
      { required: true, message: '请输入邮箱', trigger: 'blur' }
    ]
  },
  rows: [],
  ids: []
});

const load = () => {
  request.get('user/selectPage',{
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name:data.name
    }
  }).then(res => {
    if(res.code === '200') {
      data.tableData = res.data.list
      data.total = res.data.total
    }
    else{
      ElMessage.error(res.msg)
    }
  })
}

const formRef = ref()

load()

const reset = () =>{
  data.name = null
  load()
}

const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}

const add = () => {
  formRef.value.validate((valid) => {
    if(valid){
      request.post('user/add',data.form).then(res => {
        if(res.code === '200') {
          data.formVisible = false
          ElMessage.success("添加成功")
          load()
        }
        else{
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const handleEdit = (row) => {
  // 不要用浅拷贝
  // data.form = row
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const update = () => {
  formRef.value.validate((valid) => {
    if(valid){
      request.put('user/update',data.form).then(res => {
        if(res.code === '200') {
          data.formVisible = false
          ElMessage.success("修改成功")
          load()
        }
        else{
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const save = () => {
  data.form.id ? update() : add()
}

const del = (id) => {
  ElMessageBox.confirm('确认删除吗？', "提示", {type: "warning"}).then(res => {
    request.delete('/user/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("删除成功")
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const handleSelectChange = (rows) => {
  data.rows = rows
  data.ids = data.rows.map(v => v.id)
}

const deleteBatch = () => {
  if (data.rows.length === 0){
    ElMessage.warning("请选择要删除的数据")
    return
  }
  ElMessageBox.confirm('确认删除吗？', "提示", {type: "warning"}).then(res => {
    request.delete('/user/deleteBatch', { data: data.rows }).then(res => {
      if (res.code === '200') {
        ElMessage.success("批量删除成功")
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const exportData = () => {
  let idsStr = data.ids.join(',')
  let url = `http://localhost:8080/user/export?`
      + `name=${data.name === null ? '' : data.name}`
      + `&ids=${idsStr}`
      + `&token=${data.user.token}`
  window.open(url)
}

const importData = async(params) => {
  const formData = new FormData();
  formData.append("file", params.file);
  try{
    const res = await request.post('/user/import',formData,{
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    params.onSuccess(res);
  }
  catch (err) {
    params.onError(err);
  }
}

const handleImportSuccess = (res) => {
  if(res.code === '200'){
    ElMessage.success("批量导入成功")
    load()
  }
  else{
    ElMessage.error(res.msg)
  }
}

const handleAvatarSuccess = (res) => {
  data.form.avatar = res.data
}

</script>