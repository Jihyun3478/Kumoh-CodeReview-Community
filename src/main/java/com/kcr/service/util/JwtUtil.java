package com.kcr.service.util;

import com.kcr.domain.entity.Member;
import com.kcr.domain.type.RoleType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

//@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {
//    /* 1. JWT 데이터 준비하기 */
//    public static final String AUTHORIZATION_HEADER = "Authorization";      // Header KEY 값
//    public static final String AUTHORIZATION_KEY = "auth";      // 사용자 권한 값의 KEY
//    public static final String BEARER_PREFIX = "Bearer ";       // Token 식별자
//    private static final long TOKEN_TIME = 60 * 60 * 1000L;        // 토큰 만료시간 : 60분
//
//    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey (application.properties 에 추가해둔 값)
//    private String secretKey;       // 그 값을 가져와서 secretKey 변수에 넣는다
//    private static Key key;        // Secret key 를 담을 변수
//    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;     // 사용할 알고리즘 선택
//
//    @PostConstruct      // 한 번만 받으면 값을 사용할 때마다, 매번 요청을 새로 호출하는 것을 방지
//    public void init() {
//        byte[] bytes = Base64.getDecoder().decode(secretKey);
//        key = Keys.hmacShaKeyFor(bytes);
//    }
//
//    /* 2. JWT 토큰 생성 */
//    // 인증된 토큰을 기반으로 JWT 토큰을 발급
//    public static String createToken(String username, RoleType role) {
//        Date date = new Date();
//
//        // 암호화
//        return BEARER_PREFIX +
//                Jwts.builder()
//                        .setSubject(username)               // 사용자 식별자값(ID). 여기에선 username 을 넣음
//                        .claim(AUTHORIZATION_KEY, role)     // 사용자 권한 (key, value)
//                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))   // 만료 시간 : 현재시간 date.getTime() + 위에서 지정한 토큰 만료시간(60분)
//                        .setIssuedAt(date)                  // 발급일
//                        .signWith(key, signatureAlgorithm)  // 암호화 알고리즘 (Secret key, 사용할 알고리즘 종류)
//                        .compact();
//    }
//
//    // 3. header 에서 JWT 가져오기
//    public String getJwtFromHeader(HttpServletRequest request) {
//        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//
//    /* 4. JWT 토큰 검증 */
//    // 토큰의 만료, 위/변조 를 검증
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//            return true;
//        } catch (SecurityException | MalformedJwtException e) {
//            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
//        } catch (ExpiredJwtException e) {
//            log.error("Expired JWT token, 만료된 JWT token 입니다.");
//        } catch (UnsupportedJwtException e) {
//            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
//        } catch (IllegalArgumentException e) {
//            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
//        }
//        return false;
//    }
//
//    /* 5. JWT 토큰에서 사용자 정보 가져오기 */
//    public Claims getUserInfoFromToken(String token) {
//        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
//    }

    public final static long TOKEN_VALIDATION_SECOND = 1000L * 10;
    public final static long REFRESH_TOKEN_VALIDATION_SECOND = 1000L * 60 * 24 * 2;

    final static public String ACCESS_TOKEN_NAME = "accessToken";
    final static public String REFRESH_TOKEN_NAME = "refreshToken";

    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token) throws ExpiredJwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getLoginId(String token) {
        return extractAllClaims(token).get("loginId", String.class);
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    public String generateToken(Member member) {
        return doGenerateToken(member.getLoginId(), TOKEN_VALIDATION_SECOND);
    }

    public String generateRefreshToken(Member member) {
        return doGenerateToken(member.getLoginId(), REFRESH_TOKEN_VALIDATION_SECOND);
    }

    public String doGenerateToken(String loginId, long expireTime) {

        Claims claims = Jwts.claims();
        claims.put("loginId", loginId);

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();

        return jwt;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String loginId = getLoginId(token);

        return (loginId.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}