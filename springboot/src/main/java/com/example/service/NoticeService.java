package com.example.service;


import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Notice;
import com.example.exception.CustomerException;
import com.example.mapper.NoticeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    @Resource
    NoticeMapper noticeMapper;

    public List<Notice> selectAll(Notice notice){
        return noticeMapper.selectAll(notice);
    }

    public PageInfo<Notice> selectPage(Integer pageNum, Integer pageSize,Notice notice) {
        PageHelper.startPage(pageNum, pageSize);
        List<Notice> list = noticeMapper.selectAll(notice);
        return PageInfo.of(list);
    }

    public void add(Notice notice) {
        Account currentUser = TokenUtils.getCurrentUser();
        if ("ADMIN".equals(currentUser.getRole())) {
            notice.setTime(DateUtil.now());
            noticeMapper.insert(notice);
        }
        else{
            throw new CustomerException("您的权限不足");
        }
    }

    public void update(Notice notice) {
        Account currentUser = TokenUtils.getCurrentUser();
        if ("ADMIN".equals(currentUser.getRole())) {
            noticeMapper.updateById(notice);
        }
        else{
            throw new CustomerException("您的权限不足");
        }
    }

    public void deleteById(Integer id) {
        Account currentUser = TokenUtils.getCurrentUser();
        if ("ADMIN".equals(currentUser.getRole())) {
            noticeMapper.deleteById(id);
        }
        else{
            throw new CustomerException("您的权限不足");
        }
    }
}
