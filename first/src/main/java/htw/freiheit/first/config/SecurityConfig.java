package htw.freiheit.first.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig
{
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                .csrf( csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers(
                        "/user",
                        "/user/**",
                        "/role",
                        "/role/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/index.html",
                        "/swagger-ui/**"
                ).permitAll());

        return http.build();
    }
}
