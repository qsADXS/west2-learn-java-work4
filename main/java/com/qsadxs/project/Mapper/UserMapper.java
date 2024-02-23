package com.qsadxs.project.Mapper;

import com.qsadxs.project.pojo.User;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    @Select("select * from User")
    List<User> findAll();
    @Select("select username from User WHERE userId = #{userId}")
    String findUsernameByUserid(int userid);
    @Select("select * from User where username = #{username}")
    User findByUsername(String username);
    @Select("select * from User where userId = #{userId}")
    User findByUserid(int userId);
    @Select("select userId from User where username = #{username}")
    int findIdByUsername(String username);
    @Select("select password from User where username = #{username}")
    String findPasswordByUsername(String username);
    @Select("select `avatar` from User where userId = #{userId}")
    String findAvatarByUserid(int userId);
    @Update("INSERT INTO `User` (`username`, `password`) VALUES (#{username},#{password}); ")
    @Transactional
    void insertUser(User user);
    @Update("UPDATE `User` SET `password` = #{newPassword} WHERE `userId` = #{userId}; ")
    void updatePassword(String newPassword,int userId);
    @Update("UPDATE `User` SET `username` = #{newUsername} WHERE `userId` = #{userId}; ")
    void updateUsername(String newUsername,int userId);
    @Update("UPDATE `User` SET `avatar` = #{avatar} WHERE `userId` = #{userId}; ")
    void updateAvatar(String avatar,int userId);
}
