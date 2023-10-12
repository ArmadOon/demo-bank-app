package com.martinPluhar.Bankapplication.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Konfigurační třída pro zabezpečení webové aplikace s využitím Spring Security.
 * Definuje šifrování hesel a nastavuje filtry zabezpečení pro jednotlivé HTTP požadavky.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

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
     * Konfigurace AuthenticationManager pro zpracování autentizačních požadavků.
     *
     * @param configuration Konfigurační objekt pro autentizaci
     * @return AuthenticationManager pro zpracování autentizace
     * @throws Exception Výjimka, pokud dojde k chybě při konfiguraci
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Konfigurace AuthenticationProvider pro zpracování autentizačních požadavků s využitím UserDetailsService.
     *
     * @return AuthenticationProvider pro zpracování autentizace
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
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
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        // Povolit HTTP POST na /api/user a /api/user/login bez autentizace, ostatní požadavky vyžadují autentizaci
                        authorize.requestMatchers(HttpMethod.POST, "/api/user", "/api/user/login").permitAll()
                                .anyRequest().authenticated());

        // Nastavit správu relací na STATELESS (bez uchovávání relací)
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Přidat JWT autentizační filtr před standardním UsernamePasswordAuthenticationFilter
        httpSecurity.authenticationProvider(authenticationProvider());
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Vrátit konfigurovaný HttpSecurity objekt
        return httpSecurity.build();
    }
}
