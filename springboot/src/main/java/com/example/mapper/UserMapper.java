package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
    List<User> selectAll(User user);

    void insert(User user);

    @Select("select * from `user` where username = #{username}")
    User selectByUsername(String username);

    void updateById(User user);

    void deleteById(Integer id);

    @Select("select * from `user` where id = #{id}")
    User selectById(String userId);

    void updatePasswordById(User user);
}
