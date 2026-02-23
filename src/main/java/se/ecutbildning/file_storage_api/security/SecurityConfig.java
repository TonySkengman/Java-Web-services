package se.ecutbildning.file_storage_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import se.ecutbildning.file_storage_api.services.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            UserService userService,
            JwtService jwtService,
            OAuth2SuccessHandler oauth2SuccessHandler
    ) {
        http.csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(userService)
                .oauth2Login(oauth ->
                        oauth.successHandler(oauth2SuccessHandler)
                )
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/user/register").permitAll()
                            .requestMatchers("/user/login").permitAll()
                            .anyRequest().authenticated();
                })
                .addFilterAfter(new CustomAuthenticationFilter(userService, jwtService), OAuth2LoginAuthenticationFilter.class);

        return http.build();
    }
}
