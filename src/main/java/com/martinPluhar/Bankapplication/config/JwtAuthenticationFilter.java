package com.martinPluhar.Bankapplication.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtr pro zpracování JWT (JSON Web Token) autentizace. Tento filtr se stará o ověření a zpracování JWT tokenu, který je přijat v HTTP hlavičce "Authorization".
 * Pokud je token platný a autorizace proběhne úspěšně, uživatel je autorizován a informace o uživateli jsou uloženy v kontextu zabezpečení Spring Security.
 */
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    /**
     * Metoda pro zpracování HTTP požadavků a autentizaci na základě JWT tokenu.
     *
     * @param request HttpServletRequest objekt přijatého požadavku
     * @param response HttpServletResponse objekt pro odeslání odpovědi
     * @param filterChain FilterChain pro pokračování v řetězci filtrů
     * @throws ServletException Výjimka spojená s chybami servletu
     * @throws IOException Výjimka spojená s I/O operacemi
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Metoda pro získání JWT tokenu z HTTP hlavičky "Authorization".
     *
     * @param request HttpServletRequest objekt přijatého požadavku
     * @return JWT token nebo null, pokud token není k dispozici
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}