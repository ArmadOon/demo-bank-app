package com.martinPluhar.Bankapplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * Konfigurační třída pro zabezpečení webové aplikace s využitím Spring Security.
 * Tato třída aktivuje Spring Security a nastavuje šifrování hesel.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Metoda pro vytvoření a konfiguraci Spring Bean pro šifrování hesel.
     *
     * @return BCryptPasswordEncoder - objekt pro šifrování hesel
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}