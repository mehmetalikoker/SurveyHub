package org.ing.surveyhub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * GEÇİCİ yapılandırma. spring-boot-starter-security classpath'te olduğu için
 * bu bean olmadan Spring Boot varsayılan olarak TÜM istekleri login arkasına alır.
 * Gerçek admin kimlik doğrulaması (form login, ROLE_ADMIN, seed kullanıcı) ayrı
 * bir adımda eklenecek; o zaman bu sınıf tamamen değişecek.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}

