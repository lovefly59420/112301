package com.shopping_cart_project.shopping_cart_project.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // 跳過 /auth/login 的 Token 驗證
        if (path.contains("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = request.getHeader("Authorization");
        if(jwt != null){
            jwt = jwt.substring("Bearer ".length()-1);
            try{
                SecretKey key = Keys.hmacShaKeyFor(JWTConstant.SECRET != null? JWTConstant.SECRET.getBytes() : null);
                Claims claims = Jwts.parserBuilder()//建立JWT解讀器
                        .setSigningKey(key)//設定檢驗的密鑰
                        .build()//產生解讀器
                        .parseClaimsJws(jwt)//解讀JWT
                        .getBody();//取得payload內容
                String email = String.valueOf(claims.get("email"));
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, null);
                // 設定通過Spring Security的認證
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (Exception e){
                throw new BadCredentialsException("Invalid JWT or expired");
            }
            // 把request傳給下個filter使用
            filterChain.doFilter(request, response);
        }
    }
}
