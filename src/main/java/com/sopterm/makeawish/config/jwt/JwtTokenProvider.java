package com.sopterm.makeawish.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final int ACCESS_TOKEN_EXPIRATION_TIME = 7200000; // 2시간

    private static final int REFRESH_TOKEN_EXPIRATION_TIME = 1209600000; // 2주

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    public String generateAccessToken(Authentication authentication) {
        byte[] bytes = DatatypeConverter.parseBase64Binary(jwtSecretKey);
        SecretKeySpec key = new SecretKeySpec(bytes, SignatureAlgorithm.HS256.getJcaName());

        JwtBuilder jwtBuilder = Jwts.builder()
            .setSubject(String.valueOf(authentication.getPrincipal()))
            .setHeader(createHeader())
            .setExpiration(createExpireDate())
            .signWith(key, SignatureAlgorithm.HS256);

        return jwtBuilder.compact();
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());

        return header;
    }

    private Date createExpireDate() {
        return Date.from(setExpireTime(getCurrentTime()).atZone(ZoneId.systemDefault()).toInstant());
    }

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    private LocalDateTime setExpireTime(LocalDateTime now) {
        return now.plusHours(5);
    }

    // public String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        generateAccessToken(Authentication authentication) {
    //     return Jwts.builder()
    //             .setSubject(String.valueOf(authentication.getPrincipal()))
    //             .setIssuedAt(new Date())
    //             .setExpiration(getExpirationDate(ACCESS_TOKEN_EXPIRATION_TIME))
    //             .signWith(getSignKey(),SignatureAlgorithm.HS512)
    //             .compact();
    // }

    public String generateRefreshToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(String.valueOf(authentication.getPrincipal()))
                .setIssuedAt(new Date())
                .setExpiration(getExpirationDate(REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    public Long getUserFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(claims.getSubject());
    }

    private Date getExpirationDate(int tokenExpirationTime) {
        Date currentDate = new Date();
        return new Date(currentDate.getTime() + tokenExpirationTime);
    }

    public JwtValidationType validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
            return JwtValidationType.VALID_JWT;
        } catch (SecurityException ex) {
            log.error(String.valueOf(JwtValidationType.INVALID_JWT_SIGNATURE));
            return JwtValidationType.INVALID_JWT_SIGNATURE;
        } catch (MalformedJwtException ex) {
            log.error(String.valueOf(JwtValidationType.INVALID_JWT_TOKEN));
            return JwtValidationType.INVALID_JWT_TOKEN;
        } catch (ExpiredJwtException ex) {
            log.error(String.valueOf(JwtValidationType.EXPIRED_JWT_TOKEN));
            return JwtValidationType.EXPIRED_JWT_TOKEN;
        } catch (UnsupportedJwtException ex) {
            log.error(String.valueOf(JwtValidationType.UNSUPPORTED_JWT_TOKEN));
            return JwtValidationType.UNSUPPORTED_JWT_TOKEN;
        } catch (IllegalArgumentException ex) {
            log.error(String.valueOf(JwtValidationType.EMPTY_JWT));
            return JwtValidationType.EMPTY_JWT;
        }
    }

    private Key getSignKey(){
        byte[] keyBytes = jwtSecretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
