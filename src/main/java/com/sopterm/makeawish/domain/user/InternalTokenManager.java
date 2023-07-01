package com.sopterm.makeawish.domain.user;

import com.sopterm.makeawish.exception.WrongTokenException;
import com.sopterm.makeawish.service.social.CustomMemberDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import static com.sopterm.makeawish.common.message.ErrorMessage.*;

@RequiredArgsConstructor
@Service
public class InternalTokenManager {

    @Value("${jwt.secret}")
    private String jwtSecretKey;
    private final CustomMemberDetailsService memberDetailsService;

    private final ZoneId KST = ZoneId.of("Asia/Seoul");

    public String createAuthAccessToken(Long userId) {
        val signatureAlgorithm= SignatureAlgorithm.HS256;
        val secretKeyBytes = DatatypeConverter.parseBase64Binary(jwtSecretKey);
        val signingKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());
        val exp = new Date().toInstant().atZone(KST)
                .toLocalDateTime().plusHours(2).atZone(KST).toInstant();
        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .setExpiration(Date.from(exp))
                .signWith(signingKey, signatureAlgorithm)
                .compact();
    }

    public String createAuthRefreshToken(Long userId) {
        val signatureAlgorithm= SignatureAlgorithm.HS256;
        val secretKeyBytes = DatatypeConverter.parseBase64Binary(jwtSecretKey);
        val signingKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());
        val exp = new Date().toInstant().atZone(KST)
                .toLocalDateTime().plusDays(14).atZone(KST).toInstant();
        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .setExpiration(Date.from(exp))
                .signWith(signingKey, signatureAlgorithm)
                .compact();
    }

    public boolean verifyAuthToken (String token) {
        try {
            val claims = getClaimsFromToken(token);
            val now = LocalDateTime.now(KST);
            val exp = claims.getExpiration().toInstant().atZone(KST).toLocalDateTime();
            return !exp.isBefore(now);
        } catch (SignatureException | ExpiredJwtException e) {
            throw new WrongTokenException(INVALID_TOKEN.getMessage());
        }
    }

    public String getUserIdFromAuthToken (String token) {
        try {
            val claims = getClaimsFromToken(token);
            val now = LocalDateTime.now(KST);
            val exp = claims.getExpiration().toInstant().atZone(KST).toLocalDateTime();
            if (exp.isBefore(now)) {
                throw new WrongTokenException(INVALID_TOKEN.getMessage());
            }
            return claims.getSubject();
        } catch (SignatureException e) {
            throw new SignatureException(WRONG_SIGNATURE.getMessage());
        }
    }

    public Authentication getAuthentication(String token) {
        val userId = getUserIdFromAuthToken(token);
        val userDetails = memberDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(
            userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    private Claims getClaimsFromToken (String token) throws SignatureException {
        return Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
