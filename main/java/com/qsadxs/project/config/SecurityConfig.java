package com.qsadxs.project.config;

import com.qsadxs.project.Dao.TokenFilter;
import com.qsadxs.project.Dao.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private TokenFilter tokenFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/user/login/").permitAll()
                        .requestMatchers("/user/register/").permitAll()
                        .requestMatchers("/alluser").permitAll()
                        .requestMatchers("/welcome").permitAll()
                        .requestMatchers("/article/get/*").permitAll()
                        .requestMatchers("/article/user/*").permitAll()
                        .requestMatchers("/home/*").permitAll()
                        .requestMatchers("/getAvatar/*").permitAll()

                        .anyRequest().authenticated()

                ).addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf((csrf)->csrf.disable())
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))


                ;

        return httpSecurity.build();
    }
}
