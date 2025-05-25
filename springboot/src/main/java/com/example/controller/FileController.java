package com.example.controller;

import cn.hutool.core.io.FileUtil;
import com.example.common.Result;
import com.example.exception.CustomerException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
}
