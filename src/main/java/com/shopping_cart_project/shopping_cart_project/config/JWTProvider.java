package com.shopping_cart_project.shopping_cart_project.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JWTProvider {
    // 用JWTConstant的SECRET產生驗證用的金鑰，JWT預設採用HMAC-SHA256，所以選擇產生HMAC-SHA256格式的金鑰。
    SecretKey key = Keys.hmacShaKeyFor(JWTConstant.SECRET != null ? JWTConstant.SECRET.getBytes() : null);

    public String generateToken(String email){
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 12 * 60 * 60 * 1000))
                .claim("email", email)
                .signWith(key)
                .compact();
    }

    // jwt取得email的程式碼
    public String getEmailFromJWT(String jwt) {
        jwt = jwt.substring("Bearer ".length() - 1);
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        return String.valueOf(claims.get("email"));
    }
}
