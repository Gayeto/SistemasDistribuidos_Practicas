import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers("/", "/ws-chat/**", "/topic/**", "/app/**").permitAll()  // Permite acceso sin autenticación
                .anyRequest().denyAll()  // Bloquea todo lo demás
                .and()
                .csrf().disable();  // Desactiva CSRF (necesario para WebSockets)

        return http.build();
    }
}
