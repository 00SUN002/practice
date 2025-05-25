package com.example.service;


import cn.hutool.core.util.StrUtil;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Resource
    UserMapper userMapper;

    public void register(User user) {
        this.add(user);
    }

    public List<User> selectAll(User user){
        return userMapper.selectAll(user);
    }

    public PageInfo<User> selectPage(Integer pageNum, Integer pageSize,User user) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectAll(user);
        return PageInfo.of(list);
    }

    public void add(User user) {
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if(dbUser != null){
            throw new CustomerException("账号已存在");
        }
        if(StrUtil.isBlank(user.getPassword())){
            user.setPassword("123456");
        }
        if(StrUtil.isBlank(user.getName())){
            user.setName(user.getUsername());
        }
        user.setRole("USER");
        userMapper.insert(user);
    }

    public void update(User user) {
        userMapper.updateById(user);
    }

    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    public void deleteBatch(List<User> list) {
        for (User user : list) {
            deleteById(user.getId());
        }
    }

    public User login(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if(dbUser == null){
            throw new CustomerException("账号不存在");
        }
        if(!dbUser.getPassword().equals(account.getPassword())){
            throw new CustomerException("账号或密码错误");
        }
        String token = TokenUtils.createToken(dbUser.getId() + "-" + "USER",dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    public User selectById(String userId) {
        return userMapper.selectById(userId);
    }
}
