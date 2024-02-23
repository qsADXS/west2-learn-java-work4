package com.qsadxs.project.Mapper;

import com.qsadxs.project.pojo.Article;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
@Resource
public interface ArticleMapper {
    //发布文章
    @Update("INSERT INTO `Article` (`userId`, `title`, `date`, `context`) VALUES (#{userId}, #{title}, #{date}, #{context});")
    void insertArticle(int userId,String title,String date,String context);
    //查询某个用户的最后一篇文章
    @Select("SELECT MAX(articleId) FROM Article WHERE userId = #{userId}; ")
    int findUserArticleIdByUserId(int userId);
    @Select("SELECT `userId`,`title`,`date`,`context` FROM Article WHERE articleId = #{articleId}; ")
    Article findArticleByArticleId(int article);
    @Select("SELECT `userId`,`title`,`date` FROM Article WHERE articleId = #{articleId}; ")
    Article findArticleWithoutContextByArticleId(int article);
    @Select("SELECT `userId`,`title`,`date`,`articleId` FROM Article WHERE userId = #{userId}; ")
    List<Article> findArticleWithoutContextByUsername(int userId);
    @Select("SELECT userId, title, date, articleId FROM Article ORDER BY date DESC LIMIT #{offset}, 5;")
    List<Article> findArticleOrderByDate(int offset);


}
