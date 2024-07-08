package com.smsoft.sociallogintemplate.infrastructure.security;

import com.smsoft.sociallogintemplate.application.service.UserService;
import com.smsoft.sociallogintemplate.common.util.CookieUtils;
import com.smsoft.sociallogintemplate.infrastructure.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final AppProperties appProperties;
    private final UserService userService;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(appProperties.getAuth().getTokenSecret().getBytes());
    }

    public void createAndSetTokens(Authentication authentication, HttpServletResponse response) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        createAndSetTokens(userPrincipal.getId(), response);
    }
    public void createAndSetTokens(Long userId, HttpServletResponse response) {
        long accessTokenValidity = appProperties.getAuth().getAccessTokenExpirationMsec();
        long refreshTokenValidity = appProperties.getAuth().getRefreshTokenExpirationMsec();

        TokenInfo accessTokenInfo = createToken(userId, accessTokenValidity);
        TokenInfo refreshTokenInfo = createToken(userId, refreshTokenValidity);

        setCookie(response, accessTokenInfo, refreshTokenValidity);
        userService.saveRefreshToken(userId, refreshTokenInfo.token(),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(refreshTokenInfo.expiryDate()), ZoneId.systemDefault()));
    }

    private TokenInfo createToken(Long userId, long expirationTime) {
        long now = System.currentTimeMillis();
        long expiryDate = now + expirationTime;

        String token = Jwts.builder()
                .setSubject(Long.toString(userId))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(expiryDate))
                .signWith(key)
                .compact();

        return new TokenInfo(token, expiryDate, expirationTime);
    }

    private void setCookie(HttpServletResponse response, TokenInfo tokenInfo, long tokenValidity) {
        long maxAge = tokenValidity / 1000;
        CookieUtils.addCookie(response, "access_token", tokenInfo.token, (int) maxAge);
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException e) {
            log.debug("Token expired", e);
            throw e;
        } catch (JwtException | IllegalArgumentException ex) {
            log.debug("Invalid token", ex);
            return false;
        }
    }

    public record TokenInfo(String token, long expiryDate, long tokenValidity) {
    }
}