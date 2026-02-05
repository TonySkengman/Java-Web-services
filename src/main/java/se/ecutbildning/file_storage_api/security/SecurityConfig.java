package se.ecutbildning.file_storage_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import se.ecutbildning.file_storage_api.services.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            UserService userService,
            JwtService jwtService
    ) {
        http.csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(userService)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/user/register").permitAll()
                            .requestMatchers("/user/login").permitAll()
                            .requestMatchers(HttpMethod.GET, "/folder/all").permitAll()
                            .requestMatchers(HttpMethod.GET, "/folder/*").permitAll()
                            .anyRequest().authenticated();
                })
                .addFilterBefore(new JwtAuthenticationFilter(userService, jwtService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
