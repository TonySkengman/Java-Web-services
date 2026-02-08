package se.ecutbildning.file_storage_api.services;

import jakarta.security.auth.message.AuthException;
import org.springframework.security.core.userdetails.UserDetailsService;
import se.ecutbildning.file_storage_api.exceptions.CreateUserException;
import se.ecutbildning.file_storage_api.models.UserEntity;
import java.util.Optional;
import java.util.UUID;

public interface UserService extends UserDetailsService {
    UserEntity createUser(String username, String password) throws CreateUserException;
    String authenticateUser(String username, String password) throws AuthException;
    Optional<UserEntity> getUserById(UUID userId);
}
