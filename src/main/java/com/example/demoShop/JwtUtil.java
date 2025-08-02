package com.example.demoShop;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

//    private final String SECRET_KEY = "your-secret-key";
//    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간
	
	@Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;
    
    @Value("${jwt.reset-password-expiration}")
    private long RESET_PASSWORD_EXPIRATION_TIME;

    //JWT 생성
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
    
    //비밀번호 재설정용 JWT 생성
    public String generatePasswordResetToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + RESET_PASSWORD_EXPIRATION_TIME))
                .claim("type", "password_reset") // 토큰 용도 명시
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
    
    //비밀번호 재설정 토큰인지 확인
    public boolean isPasswordResetToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return "password_reset".equals(claims.get("type"));
        } catch (Exception e) {
            return false;
        }
    }

    //JWT에서 이메일 추출
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    //JWT 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
