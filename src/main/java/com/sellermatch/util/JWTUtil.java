package com.sellermatch.util;

import com.sellermatch.config.security.SecurityMemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JWTUtil {
    private String secretKey = "sellermatchTest";

    // 토큰 유효시간 30분
    private long tokenValidTime = 30 * 60 * 1000L;

    private final SecurityMemberService securityMemberService;

    // 객체 초기화, secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public Map<String, Object> createToken(String memId) {
        Claims claims = Jwts.claims().setSubject(memId);
        // claim : JWT payload 에 저장되는 정보단위
        Date now = new Date();
        Date expires = new Date(now.getTime() + tokenValidTime);
        String token = Jwts.builder()
                        .setClaims(claims) // 정보 저장
                        .setIssuedAt(now) // 토큰 발행 시간 정보
                        .setExpiration(expires) // set Expire Time
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        // 사용할 암호화 알고리즘과
                        // signature에 들어갈 secret값 세팅
                        .compact();
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("expires", expires);
        return result;
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = securityMemberService.loadUserByUsername(this.getUserMemId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserMemId(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 token 값을 가져옴. "X-AUTH-TOKEN" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
