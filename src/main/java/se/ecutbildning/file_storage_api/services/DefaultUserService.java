package se.ecutbildning.file_storage_api.services;

import jakarta.annotation.Nonnull;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.ecutbildning.file_storage_api.exceptions.*;
import se.ecutbildning.file_storage_api.models.UserEntity;
import se.ecutbildning.file_storage_api.repositories.UserRepository;
import se.ecutbildning.file_storage_api.security.JwtService;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Skapar en ny användare med angivet användarnamn och lösenord.
     *
     * @param username användarnamnet för den nya användaren
     * @param password lösenordet för den nya användaren
     * @return den skapade UserEntity
     * @throws CreateUserException om användarnamn eller lösenord är ogiltigt eller användaren redan finns
     */

    @Override
    public UserEntity createUser(String username, String password) throws CreateUserException {
        if (username.isBlank() || username.length() < 4) {
            throw new IllegalUsernameException();
        }

        var passwordErrors = new ArrayList<PasswordError>();
        if (password.isBlank() || password.length() < 5) {
            passwordErrors.add(PasswordError.TOO_SHORT);
        }

        if (!password.matches(".*[A-Z].*")) {
            passwordErrors.add(PasswordError.REQUIRES_ONE_UPPERCASE);
        }

        if (!password.matches(".*\\d.*")) {
            passwordErrors.add(PasswordError.REQUIRES_ONE_NUMBER);
        }

        if (!passwordErrors.isEmpty()) {
            throw new IllegalPasswordException(passwordErrors);
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        password = passwordEncoder.encode(password);

        var user = new UserEntity(username, password);
        user = userRepository.save(user);
        log.info("User '{}' with name '{}' created.", user.getId(), user.getUsername());

        return user;
    }

    @Override
    public UserEntity createOidcUser(String username, String oidcId, String oidcProvider) throws CreateUserException {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        var user = new UserEntity(username, null);
        user.setOidcId(oidcId);
        user.setOidcProvider(oidcProvider);

        user = userRepository.save(user);
        log.info("OIDC User '{}' with name '{}' created.", user.getId(), user.getUsername());

        return user;
    }


    /**
     * Autentiserar en användare och returnerar en JWT-token om autentisering lyckas.
     *
     * @param username användarnamn
     * @param password lösenord
     * @return en JWT-token
     * @throws AuthException om autentisering misslyckas
     */

    @Override
    public String authenticateUser(String username, String password) throws AuthException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(AuthException::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException();
        }

        return jwtService.generateToken(user.getId());
    }

    @Override
    public Optional<UserEntity> getUserByOidc(String oidcId, String oidcProvider) {
        return userRepository.findByOidcIdAndOidcProvider(oidcId, oidcProvider);
    }

    /**
     * Hämtar en användare baserat på UUID.
     *
     * @param userId användarens UUID
     * @return ett Optional som innehåller användaren om den finns
     */

    @Override
    public Optional<UserEntity> getUserById(UUID userId) {
        return userRepository.findById(userId);
    }


    /**
     * Laddar en användare baserat på användarnamn för Spring Security.
     *
     * @param username användarnamn
     * @return UserDetails
     * @throws UsernameNotFoundException om användarnamnet inte finns
     */

    @Override
    @Nonnull
    public UserDetails loadUserByUsername(@Nonnull String username)
            throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
    }
}
