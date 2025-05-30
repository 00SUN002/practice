package com.example.mapper;

import com.example.entity.Notice;

import java.util.List;

public interface NoticeMapper {
    List<Notice> selectAll(Notice notice);

    void insert(Notice notice);

    void updateById(Notice notice);

    void deleteById(Integer id);
}
