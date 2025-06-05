package com.example.mapper;

import com.example.entity.Introduction;

import java.util.List;

public interface IntroductionMapper {
    List<Introduction> selectAll(Introduction introduction);

    void insert(Introduction introduction);

    void updateById(Introduction introduction);

    void deleteById(Integer id);
}
