package com.qsadxs.project.controller;

import com.qsadxs.project.Dao.JwtUtils;
import com.qsadxs.project.Dao.ResultMap;
import com.qsadxs.project.Mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.hutool.json.JSONUtil;

@RestController
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
        return ResultMap.success(
                "If you see this, that means the program worked\n"
                +"如果你看到这，代表程序运行成功"
        );
    }
    //测试，用来获取所有用户信息，这就不删了吧
    @RequestMapping("/alluser")
    public String alluser() {
        return jsonUtil.toJsonPrettyStr(userMapper.findAll());

    }
}
