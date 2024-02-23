package com.qsadxs.project.Mapper;


import com.qsadxs.project.pojo.Comment;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
@Resource
public interface CommentMapper{
    @Update("INSERT INTO `Comment` (`id`, `isSubComment`,`date`, `userId`, `articleId`, `context`) " +
            "VALUES (#{id}, 0 ,#{date}, #{userId}, #{articleId}, #{context}); ")
    void commentToArticle(Long id, String date, int userId, int articleId, String context);
    @Update("INSERT INTO `Comment` (`id`, `isSubComment`,`date`, `userId`, `articleId`,`fatherCommentId`, `context`) " +
            "VALUES (#{id}, 1,#{date}, #{userId}, #{articleId},#{fatherCommentId}, #{context}); ")
    void commentToComment(Long id,String date,int userId,String articleId,String fatherCommentId,String context);
    @Select("SELECT * FROM `Comment` WHERE id = #{id};")
    Comment findCommentById(String id);
    @Select("SELECT `id`,`date`,`userId`,`articleId`,`fatherCommentId`,`context` FROM `Comment` WHERE `articleId` = #{articleId} AND `isSubComment` = 0;")
    List<Comment> findCommentByArticleId(int articleId);
    @Select("SELECT `id`,`date`,`userId`,`articleId`,`fatherCommentId`,`context` FROM `Comment` WHERE `id` = #{commentId} AND `isSubComment` = 0;")
    List<Comment> findSubCommentByArticleId(String commentId);
}
