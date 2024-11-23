package com.shopping_cart_project.shopping_cart_project.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/auth/login", "/auth/register").permitAll() // 無需驗證
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll())
                // 關閉CSRF防護，我們之後會使用JWT防禦CSRF攻擊。
                .csrf(csrf->csrf.disable())
                // 添加自定義的 JWT 過濾器
                .addFilterBefore(new JWTAuthenticationFilter(), BasicAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Arrays.asList(
                                "http://localhost:5173"
                        ));
                        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                        config.setAllowCredentials(true);
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }))
        ;
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // 設定密碼雜湊函式，可以將10修改更大數字，讓密碼經過更多次的運算，但是也需要耗費更多時間雜湊密碼
        return new BCryptPasswordEncoder(10);
    }
}
