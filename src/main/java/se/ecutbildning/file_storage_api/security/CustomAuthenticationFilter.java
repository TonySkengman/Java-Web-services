package se.ecutbildning.file_storage_api.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import se.ecutbildning.file_storage_api.services.UserService;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken token) {
            handleOauthAuthentication(request, response, filterChain, token);
        } else {
            handleJwtAuthentication(request, response, filterChain);
        }
    }

    private void handleOauthAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain,
            OAuth2AuthenticationToken token
    ) throws ServletException, IOException {
        var userOpt = userService.getUserByOidc(token.getName(), token.getAuthorizedClientRegistrationId());
        if (userOpt.isEmpty()) {
            SecurityContextHolder.getContext().setAuthentication(null);
            response.setStatus(401);
            return;
        }

        var user = userOpt.get();

        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        user, user.getPassword(), user.getAuthorities()
                ));
        filterChain.doFilter(request, response);

    }

    private void handleJwtAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring("Bearer ".length());

        UUID userId;
        try {
            userId = jwtService.validateToken(token);
        } catch (JWTVerificationException exception) {
            response.setStatus(401);
            return;
        }

        var optUser = userService.getUserById(userId);
        if (optUser.isEmpty()) {
            response.setStatus(401);
            return;
        }

        var user = optUser.get();

        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        user, user.getPassword(), user.getAuthorities()
                ));
        filterChain.doFilter(request, response);
    }
}