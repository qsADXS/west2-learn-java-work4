package com.qsadxs.project.controller;

import com.qsadxs.project.Dao.RedisServer;
import com.qsadxs.project.pojo.ResultMap;
import com.qsadxs.project.Dao.ArticleMapper;
import com.qsadxs.project.Dao.UserMapper;
import com.qsadxs.project.pojo.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisServer redisServer;

    @GetMapping("/latest-articles")
    //按照发布时间降序
    public ResultMap latestArticles(@RequestParam int offset){
        List<Article> articles = articleMapper.findArticleOrderByDate(offset);
        for(Article article: articles){
            article.setUsername(userMapper.findUsernameByUserid(article.getUserId()));
        }
        return ResultMap.success(articles);
    }
    @GetMapping("/articles-ranking")
    public ResultMap articlesRanking(@RequestParam int numArticles){
        List<Article> articles = new ArrayList<>();
        Set<Object> objects =  redisServer.zrevrange("juejin-article-clickCount",numArticles);
        for (Object object : objects) {
            int articleId = Integer.parseInt((String)object);
            Article article = articleMapper.findArticleWithoutContextByArticleId(articleId);
            article.setLike(redisServer.zscore("juejin-article-like",articleId));
            article.setClickCount(redisServer.zscore("juejin-article-clickCount",articleId));
            article.setUsername(userMapper.findUsernameByUserid(article.getUserId()));
            articles.add(article);
        }
        return ResultMap.success(articles);
    }
}
