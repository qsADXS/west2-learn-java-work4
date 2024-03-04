package com.qsadxs.project.component;

import cn.hutool.json.JSONUtil;
import com.qsadxs.project.Dao.RedisServer;
import com.qsadxs.project.Dao.ResultMap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;


@Component
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    RedisServer redisServer;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info("preHandle执行");

        String ipAddress = request.getRemoteAddr();
        String key = "juejin-ip-rate-limit:" + ipAddress;
        log.info("ip:"+ipAddress);
        // 检查是否存在该IP对应的访问记录
        if (redisServer.hasKey(key)) {
            // 如果存在，检查访问次数
            Long count = redisServer.ipIncrease(key);
            log.info("访问次数："+count);
            if (count != null && count > 5) {
                log.info("访问次数过多");
                // 如果访问次数超过限制，返回错误信息
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                PrintWriter writer = response.getWriter();
                writer.write(JSONUtil.toJsonStr(ResultMap.fail("访问次数过多,，限制访问，当前访问次数："+count+",当前限制访问剩余时间"+redisServer.getExpire(key)+"s")));
                writer.flush();
                return false;
            }
        } else {
            // 如果不存在，创建该IP的访问记录并设置过期时间
            log.info("不存在，创建ip访问记录");
            redisServer.setIp(key);
        }
        log.info("preHandle完成");
        return true;
    }



}


