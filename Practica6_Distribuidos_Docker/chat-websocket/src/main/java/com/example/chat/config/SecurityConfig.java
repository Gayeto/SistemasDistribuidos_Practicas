package com.example.chat.config; // Asegúrate de que el paquete sea correcto

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Permite TODAS las solicitudes a CUALQUIER PATH.
                // ¡Esto es SOLO para depuración y NO seguro para producción!
                .authorizeHttpRequests(authorize -> authorize
                    .anyRequest().permitAll() // Permitir todo
                )
                .csrf(csrf -> csrf.disable()); // Desactivar CSRF, aún necesario para WebSocket

        return http.build();
    }
}