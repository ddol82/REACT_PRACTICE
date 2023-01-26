package com.ddol.jwtd9.configuration;

import com.ddol.jwtd9.jwt.JwtAccessDeniedHandler;
import com.ddol.jwtd9.jwt.JwtAuthenticationEntryPoint;
import com.ddol.jwtd9.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SpringSecurityConfig(TokenProvider tokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers("/myimgs/**");
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        List<String> adminPermitList = new ArrayList<>();
        List<String> memberPermitList = new ArrayList<>();

        //관리자가 접근할 수 있는 URL
        adminPermitList.add("/admin/**");

        //회원이 접근할 수 있는 URL
        memberPermitList.add("/member/test/**");

        http.csrf().disable()//CSRF 설정 disable
                /* URL 접근 제한 */
                .authorizeHttpRequests()
                //관리자가 접근할 수 있는 URL
                .antMatchers(adminPermitList.toArray(new String[adminPermitList.size()])).hasRole("ADMIN")
                //관리자, 회원만 접근할 수 있는 URL
                .antMatchers(memberPermitList.toArray(new String[memberPermitList.size()])).hasAnyRole("ADMIN", "MEMBER")
                //그 외엔 모두가 접근 가능
                .anyRequest().permitAll()

                /* 로그인 */
                //Spring security에서 제공하는 formLogin을 사용하지 않음
                .and()
                .formLogin().disable()
                //매 요청마다 id, pwd를 보내는 httpBasic을 사용하지 않음
                .httpBasic().disable()

                /* API 서버에선 세션을 사용하지 않으므로 세션 설정을 Stateless로 설정 */
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)//인증 실패
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                /* CORS Setting */
                .and()
                .cors()
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        // local Recat에서 오는 요청의 CORS 허용에 대한 설정
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        //해당 IP만 응답
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        //해당 Method만 응답
        config.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Content-Type",
                                        "Access-Control-Allow-headers", "Authorization", "X-Request-With"));
        //해당 Header의 응답만 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**기본적으로 프로토콜, 호스트, 포트 를 통틀어서 Origin(출처) 라고 한다.

     즉 서로 같은 출처란 이 셋이 동일한 출처를 말하고, 여기서 하나라도 다르다면 Cross Origin, 즉 교차출처가 되는 것이다.

     http://localhost:8080 : Spring Boot
     http://localhost:3000 : React
     보안상의 이유로, 브라우저는 스크립트에서 시작한 Cross Origin HTTP Request를 제한한다. 즉, SOP(Same Origin Policy)를 따른다.

     React와 Spring Boot의 port 가 서로 다르기 때문에 cors 정책 위반 에러가 나왔던 것이다.*/
}
