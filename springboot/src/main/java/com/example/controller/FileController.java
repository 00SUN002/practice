package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import com.example.common.Result;
import com.example.exception.CustomerException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileController {

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
}
