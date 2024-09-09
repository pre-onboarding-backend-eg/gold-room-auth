package org.example.goldroomauth.config;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.goldroomauth.user.domain.UserDetail;
import org.example.goldroomauth.user.service.UserDetailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret_key}")
    private String secretKey;

    @Value("${jwt.access_token_expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh_token_expiration}")
    private long refreshTokenExpiration;

    private final String BEARER_PREFIX = "Bearer ";
    private final UserDetailService userDetailService;

    public String generateToken(String username, String type) {
        Date now = new Date();
        long time = type.equals("access") ? accessTokenExpiration : refreshTokenExpiration;

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(Jwts.claims().
                setSubject(username).
                setAudience(type).
                setIssuedAt(now).
                setExpiration(new Date(now.getTime()+time))
                )
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    // 토큰 유효성 검증
    public boolean validToken(String token){
        try{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }
    /*
    * 토큰으로 인증 정보 담은 Authentication 반환
    * principal : 인증된 사용자 정보
    * credentials : 사용자의 인증 자격 증명 (인증 완료된 상태이므로 빈 문자열 사용)
    * authorities : 사용자의 권한목록
    * */
    public Authentication getAuthentication(String token) {
        UserDetail userDetail = (UserDetail) userDetailService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetail, "", userDetail.getAuthorities());

    }
    // 토큰에서 username 얻기
    public String getUsername(String token) {
        try { // JWT를 파싱해서 JWT 서명 검증 후 클레임을 반환하여 payload에서 subject 클레임 추출
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }
    // 토큰 Header에서 꺼내오기
    public String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(BEARER_PREFIX))
            return header.substring(BEARER_PREFIX.length());
        return null;
    }
}
