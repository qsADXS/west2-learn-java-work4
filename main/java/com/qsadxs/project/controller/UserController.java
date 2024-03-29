package com.qsadxs.project.controller;

import cn.hutool.json.JSONUtil;
import com.qsadxs.project.util.JwtUtils;
import com.qsadxs.project.Dao.RedisServer;
import com.qsadxs.project.pojo.ResultMap;
import com.qsadxs.project.Dao.ArticleMapper;
import com.qsadxs.project.Dao.UserMapper;
import com.qsadxs.project.pojo.Article;
import com.qsadxs.project.pojo.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Resource
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    UserMapper userMapper;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    RedisServer redisServer;
    @Autowired
    ArticleMapper articleMapper;
    @Value("${qsadxs.defaultAvatar}")
    String defaultAvatar;
    //登录
    @PostMapping("/login/")
    public ResultMap loginUser(@RequestParam String username,@RequestParam String password) {
        if(username ==null||password==null){
            log.info("用户名或密码为空");
            return ResultMap.fail("用户名或密码为空");
        }
        if(Objects.equals(password, userMapper.findPasswordByUsername(username))){
            Map<String, Object> map = new HashMap<>();
            String token = jwtUtils.generateToken(username);
            int userId = userMapper.findIdByUsername(username);
            map.put("token",token);
            map.put("tokenHead",tokenHead);
            map.put("userId",userId);
            return ResultMap.success(map);
        }else{
            return ResultMap.fail("用户名或密码错误");
        }
    }
    @PostMapping("/register/")
    public ResultMap register(@RequestParam String username,@RequestParam String password){
        if(username == null||password== null|| username.equals("")||password.equals("")){
            log.info("用户名或密码为空");
            return ResultMap.fail("用户名或密码为空");
        }
        if(userMapper.findByUsername(username) != null){
            return ResultMap.fail("用户名已存在");
        }else{
            userMapper.insertUser(new User(username,password));
            return ResultMap.success(null);
        }
    }
    @PostMapping("/change-username")
    public ResultMap changeUsername(@RequestParam String newUsername){
        if(newUsername == null){
            log.info("新用户名为空");
            return ResultMap.fail("新用户名为空");
        }
        UserDetails userDetails = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            userDetails = (UserDetails) authentication.getPrincipal();
        } else {
            return ResultMap.fail("error");
        }
        User user = userMapper.findByUsername(userDetails.getUsername());
        if(Objects.equals(newUsername,user.getUsername())){
            return ResultMap.fail("新用户名不能与旧用户名相同");
        } else if (userMapper.findByUsername(newUsername) != null) {
            return ResultMap.fail("用户名已存在");
        }
        userMapper.updateUsername(newUsername,user.getUserId());
        return ResultMap.success(null);
    }
    @PostMapping("/change-password")
    public ResultMap changePassword(@RequestParam String newPassword){
        if(newPassword ==null){
            log.info("新密码为空");
            return ResultMap.fail("新密码为空");
        }
        UserDetails userDetails = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            userDetails = (UserDetails) authentication.getPrincipal();
        } else {
            return ResultMap.fail("error");
        }
        User user = userMapper.findByUsername(userDetails.getUsername());
        if(Objects.equals(newPassword,user.getPassword())){
            return ResultMap.fail("新密码不能与旧密码相同");
        }
        userMapper.updatePassword(newPassword,user.getUserId());
        return ResultMap.success(null);
    }
    @GetMapping("/like")
    public ResultMap getLike(){
        UserDetails userDetails = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            userDetails = (UserDetails) authentication.getPrincipal();
        } else {
            return ResultMap.fail("error");
        }
        Set<Object>likes =  redisServer.smember(userMapper.findIdByUsername(userDetails.getUsername()));
        List<Article> articles = new ArrayList<>();
        for (Object like : likes) {
            int articleId = Integer.parseInt((String)like);
            Article article = articleMapper.findArticleWithoutContextByArticleId(articleId);
            article.setLike(redisServer.zscore("juejin-article-like",articleId));
            article.setClickCount(redisServer.zscore("juejin-article-clickCount",articleId));
            article.setUsername(userMapper.findUsernameByUserid(article.getUserId()));
            articles.add(article);
        }
        return ResultMap.success(articles);
    }
    @GetMapping("/getUser/{userId}")
    public ResultMap getUser(@PathVariable int userId){
        String username = userMapper.findUsernameByUserid(userId);
        if(username == null){
            return ResultMap.fail("错误用户id");
        }
        String avatar = userMapper.findAvatarByUserid(userId);
        if(avatar == null){
            avatar = defaultAvatar;
        }
        Map<String,Object>map = new HashMap<>();
        map.put("username",username);
        map.put("avatar",avatar);
        return ResultMap.success(map);
    }
}
