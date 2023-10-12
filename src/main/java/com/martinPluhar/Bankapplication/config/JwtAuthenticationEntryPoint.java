package com.martinPluhar.Bankapplication.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
/**
 * Třída implementující rozhraní {@link AuthenticationEntryPoint}, která zpracovává přístupy na neautorizované zdroje.
 * Používá se v kombinaci s JWT autentizací pro zasílání odpovědí s HTTP stavovým kódem UNAUTHORIZED (401) a případně doplňujícími informacemi.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Metoda, která zpracovává přístupy na neautorizované zdroje.
     *
     * @param request HttpServletRequest objekt přijatého požadavku
     * @param response HttpServletResponse objekt pro odeslání odpovědi
     * @param authException Výjimka reprezentující selhání autentizace
     * @throws IOException Výjimka spojená s I/O operacemi
     * @throws ServletException Výjimka spojená s chybami servletu
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Odeslat HTTP stavový kód UNAUTHORIZED (401) a případně zprávu o chybě autentizace
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}