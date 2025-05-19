package com.example.controller;

import com.example.common.Result;
import com.example.service.AdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {
    private final AdminService adminService;

    public WebController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/hello")
    public Result index(){
        return Result.success("hello world");
    }

    @GetMapping("admin")
    public Result admin(String name){
        String admin = adminService.admin(name);
        return Result.success(admin);
    }
}
