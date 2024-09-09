package org.example.goldroomauth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    // 비밀번호 암호화 기능
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // 임시설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest().permitAll()  // 모든 요청 허용
                )
                .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화
                .formLogin(formLogin -> formLogin.disable())  // 폼 로그인 비활성화
                .httpBasic(httpBasic -> httpBasic.disable());  // HTTP Basic 인증 비활성화

        return http.build();
    }
}
