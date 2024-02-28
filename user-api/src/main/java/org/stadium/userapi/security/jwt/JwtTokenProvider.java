package org.stadium.userapi.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.stadium.corelib.domain.User;
import org.stadium.userapi.controller.errors.BadRequestAlertException;
import org.stadium.userapi.security.enumaration.JwtTokenStatus;

import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private long jwtExpirationInMs;


    public String generateToken(Authentication authentication) {

        User userPrincipal = (User) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs * 86_400_000);
        Claims claims = Jwts.claims().setSubject(String.valueOf(userPrincipal.getId()));
        claims.put("fullName", userPrincipal.getFirstname() + " " + userPrincipal.getLastname());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public Map<String, JwtTokenStatus> validateToken(String authToken) throws BadRequestAlertException {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return Map.of("status", JwtTokenStatus.VALID);
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
            return Map.of("status", JwtTokenStatus.SIGNATURE_ERROR);
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
            return Map.of("status", JwtTokenStatus.MALFORMED);
        } catch (ExpiredJwtException ex) {
            logger.error("Expired Jwt token");
            return Map.of("status", JwtTokenStatus.EXPIRED);
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
            return Map.of("status", JwtTokenStatus.UNSUPPORTED);
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
            return Map.of("status", JwtTokenStatus.ILLEGAL_ARGUMENT);
        } catch (Exception exception) {
            return Map.of("status", JwtTokenStatus.INVALID);
        }
    }
}
