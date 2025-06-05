<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="load" style="width: 260px; margin-right: 5px" v-model="data.name" placeholder="请输入查询名称" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px"  v-if="data.user.role === 'ADMIN'">
      <el-button type="primary" @click="handleAdd">新 增</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" :header-cell-style="{ color: '#333',backgroundColor: '#eaf4ff' }">
        <el-table-column label="封面图" width="100">
          <template #default="scope">
            <el-image
                v-if="scope.row.img"
                :src="scope.row.img"
                :preview-src-list="[scope.row.img]"
                :preview-teleported="true"
                style="width: 50px; height: 50px; border-radius: 5px; display: block"
            />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="攻略标题" />
        <el-table-column prop="content" label="攻略内容">
          <template v-slot="scope">
            <el-button type="primary" @click="viewContent(scope.row.content)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="发布时间" />
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

    <el-dialog v-model="data.formVisible" title="攻略信息" width="80%" destroy-on-close>
      <el-form ref="formRef" :model="data.form" label-width="80px" style="padding: 20px 30px 10px 0">
        <el-form-item prop="img" label="攻略图">
          <el-upload
              action="http://localhost:8080/files/upload"
              :headers="{ token:data.user.token }"
              :on-success="handleFileSuccess"
              list-type="picture"
          >
            <el-button type="primary">上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="title" label="攻略标题">
          <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入攻略标题"/>
        </el-form-item>
        <el-form-item prop="content" label="攻略内容">
          <div style="border: 1px solid #ccc; width: 100%">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :mode="mode"
            />
            <Editor
                style="height: 400px; overflow-y: hidden;"
                v-model="data.form.content"
                :mode="mode"
                :defaultConfig="editorConfig"
                @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="data.viewVisible" :title="data.title" width="80%" destroy-on-close>
      <div v-html="data.content" style="padding: 0 20px"></div>
    </el-dialog>

  </div>
</template>

<script setup>
import {Search} from "@element-plus/icons-vue";
import { onBeforeUnmount, reactive, ref, shallowRef } from "vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";
import { Editor,Toolbar } from "@wangeditor/editor-for-vue";
import '@wangeditor/editor/dist/css/style.css'

const data = reactive({
  user:JSON.parse(localStorage.getItem('user') || '{}'),
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  title: null,
  form: {},
  formVisible: false,
  content: null,
  viewVisible: false,
})

const editorRef = shallowRef()
const mode = ref('default')
const editorConfig = { MENU_CONF: {} }
editorConfig.MENU_CONF['uploadImage'] = {
  headers: {
    token: data.user.token,
  },
  server: 'http://localhost:8080/file/wang/upload',
  filedName: 'file'
}

onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

const handleCreated = (editor) => {
  editor.value = editor
}

const load = () => {
  request.get('/introduction/selectPage',{
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data.records
      data.total = res.data.total
    }
    else {
      ElMessage.error(res.msg)
    }
  })
}

load()

const viewContent = (content) => {
  data.content = content
  data.viewVisible = true
}

const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const handleFileSuccess = (res) => {
  data.form.file = res.data
}

const add = () => {
  request.post('/introduction/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('保存成功')
      data.formVisible = false
      load()
    }
    else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put('/introduction/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('更新成功')
      data.formVisible = false
      load()
    }
    else {
      ElMessage.error(res.msg)
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm('确认删除吗？', "提示", {type: "warning"}).then(res => {
    request.delete('/introduction/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("删除成功")
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const save = () => {
  data.form.id ? update() : add()
}

const reset = () => {
  data.title = null
  load()
}
</script>
