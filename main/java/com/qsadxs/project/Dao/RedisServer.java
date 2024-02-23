package com.qsadxs.project.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class RedisServer {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    //创建文章的同时给redis中创建相应的数据库，key: juejin-article-clickCount,juejin-article-like
    public void zadd(String key,int articleId){redisTemplate.opsForZSet().add(key,String.valueOf(articleId),0);}
    //查询某个文章相应的值
    public int zscore(String key,int articleId){return Objects.requireNonNull(redisTemplate.opsForZSet().score(key, String.valueOf(articleId))).intValue();}
    //给体某个文章的key加上increment
    public void zincrby(String key,int articleId,int increment){redisTemplate.opsForZSet().incrementScore(key,String.valueOf(articleId),increment);}
    //返回key的前numArticles个
    public Set<Object> zrevrange(String key,int numArticles){return redisTemplate.opsForZSet().reverseRange(key,0,numArticles - 1);}
    //某个用户(userId)点赞某篇文章,返回1表示成功，返回0表示失败
    public Long sadd(int userId,int articleId){return redisTemplate.opsForSet().add("juejin-userlike-"+String.valueOf(userId),String.valueOf(articleId));}
    //获取用户点赞的文章
    public Set<Object> smember(int userId){return redisTemplate.opsForSet().members("juejin-userlike-"+String.valueOf(userId));}



}
