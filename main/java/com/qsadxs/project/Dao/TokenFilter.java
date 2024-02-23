package com.qsadxs.project.Dao;


import cn.hutool.json.JSONUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class TokenFilter extends OncePerRequestFilter {
    /**
     * 用户指定数据的核心接口.
     */
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtils jwtUtils;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        try {
            // 获取JWT存储的请求头
            String authHeader = request.getHeader(this.tokenHeader);
            // 判断
            if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
                // "Bearer "负载之后的部分，也就是我们的token
                String authToken = authHeader.substring(this.tokenHead.length());
                // 从token中获取登录用户名
                Claims claims = jwtUtils.getClaimsByToken(authToken);
            if (jwtUtils.getClaimsByToken(authToken) == null ||jwtUtils.isTokenExpired(jwtUtils.getClaimsByToken(authToken))) {
                // Token 已过期，返回提示信息
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                PrintWriter writer = response.getWriter();
                writer.write(JSONUtil.toJsonStr(ResultMap.fail("token无效")));
                writer.flush();
                return;
            }

                if (claims != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    String username = claims.getSubject();
                    // 用于通过用户名获取用户数据. 返回 UserDetails 对象, 表示用户的核心信息 (用户名, 用户密码信息).
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    // 验证token是否还有效
                    if (!jwtUtils.isTokenExpired(jwtUtils.getClaimsByToken(authToken))) {
                        // UsernamePasswordAuthenticationToken装载用户名和密码以及权限 获得Authentication
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                    }else {
                        throw new Exception();
                    }
                }
            }
        } catch (Exception e) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter writer = response.getWriter();
            writer.write(JSONUtil.toJsonStr(ResultMap.fail(e.getMessage())));
            writer.flush();
            return;
        }
        chain.doFilter(request, response);
    }
}

