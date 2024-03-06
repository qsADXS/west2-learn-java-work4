package com.qsadxs.project.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.qsadxs.project.Dao.RedisServer;
import com.qsadxs.project.pojo.ResultMap;
import com.qsadxs.project.Dao.ArticleMapper;
import com.qsadxs.project.Dao.CommentMapper;
import com.qsadxs.project.Dao.UserMapper;
import com.qsadxs.project.pojo.Article;
import com.qsadxs.project.pojo.Comment;
import com.qsadxs.project.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    RedisServer redisServer;
    @Autowired
    CommentMapper commentMapper;
    @Value("${qsadxs.defaultAvatar}")
    String defaultAvatar;

    @PostMapping("/write-article")
    public ResultMap writeArticle(@RequestBody String json){
        UserDetails userDetails = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            userDetails = (UserDetails) authentication.getPrincipal();
        } else {
            log.info("/write-article运行时出错");
            return ResultMap.fail("error");
        }
        Article article = JSONUtil.toBean(json, Article.class);
        User user = userMapper.findByUsername(userDetails.getUsername());
        articleMapper.insertArticle(user.getUserId(), article.getTitle(), DateUtil.today(), article.getContext());
        int articleId = articleMapper.findUserArticleIdByUserId(user.getUserId());
        redisServer.zadd("clickCount",articleId);
        redisServer.zadd("like",articleId);
        return ResultMap.success(null);
    }
    @PostMapping("/{articleId}/like")
    public ResultMap like(@PathVariable int articleId){
        UserDetails userDetails = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            userDetails = (UserDetails) authentication.getPrincipal();
        } else {
            log.info("/{articleId}/like运行时出错");
            return ResultMap.fail("error");
        }
        int result = redisServer.sadd(userMapper.findIdByUsername(userDetails.getUsername()),articleId).intValue();
        //成功
        if(result == 1){
            redisServer.zincrby("juejin-article-like",articleId,1);
            log.info("成功点赞文章");
            return ResultMap.success(null);
        }else{
            log.info("无法点赞文章");
            return ResultMap.fail("无法点赞文章");
        }
    }
    @GetMapping("/get/{articleId}")
    public ResultMap getArticle(@PathVariable int articleId){
        Article article = articleMapper.findArticleByArticleId(articleId);
        if(article == null){
            log.info("找不到文章:articleId="+articleId);
            return ResultMap.fail("找不到文章");
        }
        article.setArticleId(articleId);
        List<Comment>comments=commentMapper.findCommentByArticleId(articleId);
        //这里应该有获取用户名更快的方法，不太懂
        for(int i = 0; i < comments.toArray().length;i++){
            //获得该评论的用户名
            comments.get(i).setUsername(userMapper.findUsernameByUserid(comments.get(i).getUserId()));
            //获得用户头像
            String base64Image = userMapper.findAvatarByUserid(comments.get(i).getUserId());
            if(base64Image == null){
                base64Image = defaultAvatar;
            }
            comments.get(i).setAvatar(base64Image);
            //获得该评论的子评论
            comments.get(i).setSubComments(commentMapper.findSubCommentByArticleId(comments.get(i).getId()));
            //获得子评论的用户名和头像
            for(int j = 0; j <  comments.get(i).getSubComments().toArray().length; j++){
                comments.get(i).getSubComments().get(j).setUsername(userMapper.findUsernameByUserid(comments.get(i).getSubComments().get(j).getUserId()));
                base64Image = userMapper.findAvatarByUserid(comments.get(i).getSubComments().get(j).getUserId());
                if(base64Image == null){
                    base64Image = defaultAvatar;
                }
                comments.get(i).getSubComments().get(j).setAvatar(base64Image);
            }
        }
        article.setComments(comments);
        article.setUsername(userMapper.findByUserid(article.getUserId()).getUsername());
        article.setLike(redisServer.zscore("juejin-article-like", articleId));
        article.setClickCount(redisServer.zscore("juejin-article-clickCount", articleId));
        redisServer.zincrby("juejin-article-clickCount", articleId,1);
        return ResultMap.success(article);
    }
    @GetMapping("/user/{userId}")
    public ResultMap getUserArticle(@PathVariable int userId){
        User user = userMapper.findByUserid(userId);
        if(user == null){
            log.info("找不到用户:userId"+userId);
            return ResultMap.fail("找不到用户");
        }
        String username = user.getUsername();
        log.info("username="+username);
        List<Article> articles = articleMapper.findArticleWithoutContextByUsername(userId);
        log.info("文章数为："+articles.size());
        for(Article article : articles){
            article.setLike(redisServer.zscore("juejin-article-like",article.getArticleId()));
            article.setClickCount(redisServer.zscore("juejin-article-clickCount",article.getArticleId()));
            article.setUsername(username);
        }
        log.info("完成");
        return ResultMap.success(articles);
    }

}
