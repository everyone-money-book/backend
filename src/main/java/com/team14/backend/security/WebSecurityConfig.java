package com.team14.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder encoderPassword() {
        return new BCryptPasswordEncoder();
    }
    //로그인 성공 핸들러 빈 등록
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new LoginSuccessHandlerImpl();
    }

    //로그인 실패 핸들러 빈 등록
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new LoginFailHandlerImpl();
    }

    //로그아웃 성공 핸들러 빈 등록
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(){
        return new LogoutSuccessHandlerImpl();
    }

    @Override
    //h2-console 사용에 대한 허용 (CSRF, FrameOption 무시)
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/h2-console/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf()
                .disable();

        http.authorizeRequests()
                .antMatchers("/","/api/users/**", "/images/**", "/css/**", "/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**")
                .permitAll() //index.html 허용, userController 허용
                .anyRequest().authenticated();

        http.formLogin() //로그인 관련 설정
//                .loginPage("/user/login/necessary") //로그인 view 페이지 따로 설정 GET /user/login
                .loginProcessingUrl("/user/login") //로그인처리 Post/user/login
                .permitAll()
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler()) //로그인 실패 핸들러
                .and()
                .logout()
                .logoutUrl("/user/logout")
                .logoutSuccessHandler(logoutSuccessHandler())
                .permitAll();
    }
}