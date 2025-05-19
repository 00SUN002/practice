package com.example.service;


import cn.hutool.core.util.StrUtil;
import com.example.entity.Admin;
import com.example.exception.CustomerException;
import com.example.mapper.AdminMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Resource
    AdminMapper adminMapper;

    public String admin(String name){
        if("admin".equals(name)){
            return "admin";
        }
        else{
            throw new CustomerException("用户名错误");
        }
    }

    public List<Admin> selectAll(){
        return adminMapper.selectAll(null);
    }

    public PageInfo<Admin> selectPage(Integer pageNum, Integer pageSize,String name) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectAll(name);
        return PageInfo.of(list);
    }

    public void add(Admin admin) {
        Admin dbAdmin = adminMapper.selectByUsername(admin.getUsername());
        if(dbAdmin != null){
            throw new CustomerException("账号已存在");
        }
        if(StrUtil.isBlank(admin.getPassword())){
            admin.setPassword("123456");
        }
        adminMapper.insert(admin);
    }

    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }

    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    public void deleteBatch(List<Admin> list) {
        for (Admin admin : list) {
            deleteById(admin.getId());
        }
    }
}
