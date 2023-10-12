package com.martinPluhar.Bankapplication.config;

import com.martinPluhar.Bankapplication.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
/**
 * Třída JwtTokenProvider je zodpovědná za generování a zpracování JWT tokenů pro autentizaci uživatelů v aplikaci.
 */
@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration}")
    private long jwtExpirationDate;

    /**
     * Generuje JWT token na základě autentizačních informací uživatele.
     *
     * @param authentication Informace o autentizovaném uživateli
     * @return JWT token
     */
    public String generateToken(Authentication authentication) {
        String userName = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.stream()
                .filter(authority -> authority.getAuthority().startsWith("ROLE_")) // Filtruje role, např. "ROLE_ADMIN"
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(""); // Výchozí hodnota, pokud uživatel nemá žádnou roli

        return Jwts.builder()
                .setSubject(userName)
                .claim("role", role)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
    }

    /**
     * Vytváří a vrací klíč pro podepisování a ověřování JWT tokenů.
     *
     * @return Klíč pro podepisování a ověřování JWT tokenů
     */
    private Key key() {
        byte[] bytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(bytes);
    }

    /**
     * Získá uživatelské jméno (subjekt) z JWT tokenu.
     *
     * @param token JWT token
     * @return Uživatelské jméno
     */
    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * Ověřuje platnost JWT tokenu.
     *
     * @param token JWT token
     * @return True, pokud je token platný; jinak False
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (ExpiredJwtException | IllegalArgumentException | SignatureException | MalformedJwtException e) {
            throw new RuntimeException(e);
        }
    }
}
