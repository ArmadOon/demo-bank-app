package com.martinPluhar.Bankapplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



/**
 * Konfigurační třída pro zabezpečení webové aplikace s využitím Spring Security.
 * Definuje šifrování hesel a nastavuje filtry zabezpečení pro jednotlivé HTTP požadavky.
 */
@Configuration
public class SecurityConfig {

    /**
     * Metoda pro vytvoření a konfiguraci Spring Bean pro šifrování hesel.
     *
     * @return BCryptPasswordEncoder - objekt pro šifrování hesel
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Konfigurace zabezpečení HTTP požadavků a nastavení filtrů.
     *
     * @param httpSecurity HttpSecurity objekt pro konfiguraci zabezpečení
     * @return SecurityFilterChain - filtrování a zabezpečení HTTP požadavků
     * @throws Exception Výjimka, pokud dojde k chybě při konfiguraci
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Zakázat CSRF ochranu
        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize ->
                        // Povolit HTTP POST na /api/user bez autentizace, ostatní požadavky vyžadují autentizaci
                        authorize.requestMatchers(HttpMethod.POST, "/api/user").permitAll()
                                .anyRequest().authenticated());

        // Nastavit správu relací na STATELESS (bez uchovávání relací)
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Vrátit konfigurovaný HttpSecurity objekt
        return httpSecurity.build();
    }
}