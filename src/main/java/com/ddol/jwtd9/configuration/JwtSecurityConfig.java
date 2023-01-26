package com.ddol.jwtd9.configuration;

import com.ddol.jwtd9.jwt.JwtFilter;
import com.ddol.jwtd9.jwt.TokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;

    public JwtSecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // TokenProvider를 주입받아 Security 로직 필터에 JwtFilter 등록
    // UsernamePasswordAuthentocationFilter는 접근 권한이 없을 때 기본적으로 로그인 폼으로 보내는데
    // 이 필터 앞에 JwtFilter를 넣어서 인증 권한이 없는 오류 처리를 가능하게 함
    @Override
    public void configure(HttpSecurity http) {
        JwtFilter customFilter = new JwtFilter(tokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
