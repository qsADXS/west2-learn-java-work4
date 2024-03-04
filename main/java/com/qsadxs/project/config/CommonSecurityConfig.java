package com.qsadxs.project.config;

import com.qsadxs.project.Dao.JwtUtils;
import com.qsadxs.project.component.TokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class CommonSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }
    @Bean
    public TokenFilter tokenFilter() {
        return new TokenFilter();
    }
}
