package com.qsadxs.project.controller;

import com.qsadxs.project.util.JwtUtils;
import com.qsadxs.project.pojo.ResultMap;
import com.qsadxs.project.Dao.UserMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.hutool.json.JSONUtil;

@RestController
@Slf4j
public class welcome {

    @Resource
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    UserMapper userMapper;
    @Autowired
    JwtUtils jwtUtils;
    JSONUtil jsonUtil;
    @RequestMapping("/welcome")
    public ResultMap hello(){
        log.info("控制器执行");
        return ResultMap.success(
                "如果你看到这，代表程序运行成功"
        );
    }
}
