package com.qsadxs.project.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.qsadxs.project.pojo.ResultMap;
import com.qsadxs.project.util.SnowflakeIdGenerator;
import com.qsadxs.project.Dao.ArticleMapper;
import com.qsadxs.project.Dao.CommentMapper;
import com.qsadxs.project.Dao.UserMapper;
import com.qsadxs.project.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/article")
public class CommentController {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CommentMapper commentMapper;


    @PostMapping("/{articleId}/comment")
    //对文章评论
    public ResultMap commentToArticle(@PathVariable int articleId,
                            @RequestBody String json){
        if(articleMapper.findArticleByArticleId(articleId) == null){
            return ResultMap.fail("文章id错误");
        }
        String context = JSONUtil.parseObj(json).getStr("context");
        UserDetails userDetails = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            userDetails = (UserDetails) authentication.getPrincipal();
        } else {
            return ResultMap.fail("error");
        }
        User user = userMapper.findByUsername(userDetails.getUsername());
        commentMapper.commentToArticle(new SnowflakeIdGenerator().nextId(),
                 DateUtil.today(), user.getUserId(),
                articleId, context
                );

        log.info("评论创建成功");
        return ResultMap.success(null);
    }

    @PostMapping("/{articleId}/{commentId}/comment")
    //对文章评论
    public ResultMap commentToComment(@PathVariable String articleId,
                                      @PathVariable String commentId,
                                      @RequestBody String json){
        if(articleMapper.findArticleByArticleId(Integer.parseInt(articleId)) == null){
            log.error("文章id错误");
            return ResultMap.fail("文章id错误");
        }
        if (commentMapper.findCommentById(commentId) == null){
            log.error("评论id错误");
            return ResultMap.fail("文章id错误");
        }
        String context = JSONUtil.parseObj(json).getStr("context");
        UserDetails userDetails = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            userDetails = (UserDetails) authentication.getPrincipal();
        } else {
            return ResultMap.fail("error");
        }
        User user = userMapper.findByUsername(userDetails.getUsername());
        commentMapper.commentToComment(new SnowflakeIdGenerator().nextId(),
                DateUtil.today(), user.getUserId(),
                articleId,commentId, context
        );

        log.info("评论创建成功");
        return ResultMap.success(null);
    }
}
