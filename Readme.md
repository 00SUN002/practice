### 端口设置

#### 前端访问地址设置

vue\utils\request.js
```js
const request = axios.create({
    baseURL: "http://localhost:8080",
    timeout: 30000
})
```

vue/src/views/Introduction.vue
```vue
editorConfig.MENU_CONF['uploadImage'] = {
  headers: {
    token: data.user.token,
  },
  server: 'http://localhost:8080/file/wang/upload',
  filedName: 'file'
}
```

vue/src/views/User.vue
```vue
  <el-upload
      action="http://localhost:8080/files/upload"
      :headers="{ token:data.user.token }"
      :on-success="handleAvatarSuccess"
      list-type="picture"
  >
    <el-button type="primary">上传头像</el-button>
  </el-upload>
```

vue/src/views/Admin.vue
```vue
  <el-upload
      action="http://localhost:8080/files/upload"
      :headers="{ token:data.user.token }"
      :on-success="handleAvatarSuccess"
      list-type="picture"
  >
    <el-button type="primary">上传头像</el-button>
  </el-upload>
```

#### 前端导出文件访问地址设置

vue/src/views/Admin.vue
```vue
const exportData = () => {
  let idsStr = data.ids.join(',')
  let url = `http://localhost:8080/admin/export?`
      + `name=${data.name === null ? '' : data.name}`
      + `&ids=${idsStr}`
      + `&token=${data.user.token}`
  window.open(url)
}
```

vue/src/views/User.vue
```vue
const exportData = () => {
    let idsStr = data.ids.join(',')
    let url = `http://localhost:8080/user/export?`
        + `name=${data.name === null ? '' : data.name}`
        + `&ids=${idsStr}`
        + `&token=${data.user.token}`
    window.open(url)
}
```
#### 后端端口号设置
springboot\src\main\resources\application.yml
```yml
server:
  port: 8080
```

#### 后端文件上传下载端口号及路径设置
springboot/src/main/java/com/example/controller/FileController.java
```java
@PostMapping("/upload")
public Result upload(@RequestParam("file")MultipartFile file) throws IOException {
    String filePath = System.getProperty("user.dir") + "/files/";
    if (!FileUtil.isDirectory(filePath)) {
        FileUtil.mkdir(filePath);
    }
    byte[] bytes =  file.getBytes();
    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
    FileUtil.writeBytes(bytes, filePath + fileName);
    String url = "http://localhost:8080/files/download/" + fileName;
    return Result.success(url);
}

@GetMapping("/download/{fileName}")
public void download(@PathVariable String fileName, HttpServletResponse response) throws Exception {
    String filePath = System.getProperty("user.dir") + "/files/";
    String realPath = filePath + fileName;
    boolean exist = FileUtil.exist(realPath);
    if(!exist){
        throw new CustomerException("文件不存在");
    }
    byte[] bytes = FileUtil.readBytes(realPath);
    ServletOutputStream os = response.getOutputStream();
    os.write(bytes);
    os.flush();
    os.close();
}
@PostMapping("/wang/upload")
public Map<String, Object> wangEditorUpload(MultipartFile file){
    String flag = System.currentTimeMillis() + "";
    String fileName = file.getOriginalFilename();
    try{
        String filePath = System.getProperty("user.dir") + "/files/";
        FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);
        System.out.println(fileName + "上传成功");
        Thread.sleep(1L);
    } catch (Exception e) {
        System.out.println(fileName + "上传失败");
    }
    String http = "http://localhost:8080/files/download/";
    Map<String, Object> resMap = new HashMap<>();
    resMap.put("errno", 0);
    resMap.put("data", CollUtil.newArrayList(Dict.create().set("url", http + flag + "-" + fileName)));
    return resMap;
}
```

### 数据库设置

#### 数据库设置

springboot\src\main\resources\application.yml
```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: '123456'
    url: jdbc:mysql://localhost:3306/yourdatabasename?useUnicode=true&allowMultiQueries=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC
```