package se.ecutbildning.file_storage_api.controllers;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.ecutbildning.file_storage_api.dtos.CreateUserRequest;
import se.ecutbildning.file_storage_api.dtos.ErrorResponse;
import se.ecutbildning.file_storage_api.dtos.LoginUserRequest;
import se.ecutbildning.file_storage_api.dtos.UserResponse;
import se.ecutbildning.file_storage_api.exceptions.IllegalPasswordException;
import se.ecutbildning.file_storage_api.exceptions.IllegalUsernameException;
import se.ecutbildning.file_storage_api.exceptions.UserAlreadyExistsException;
import se.ecutbildning.file_storage_api.services.UserService;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        try {
            var user = userService.createUser(request.getUsername(), request.getPassword());
            return ResponseEntity.created(URI.create("/user")).body(UserResponse.fromModel(user));
        } catch (IllegalPasswordException exception) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message", "Invalid password",
                            "errors", exception.getErrors()
                    ));
        } catch (IllegalUsernameException ignored) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Invalid username, must have at least 5 characters"));
        } catch (UserAlreadyExistsException ignored) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("That username is taken"));
        } catch (Exception exception) {
            log.error("Error creating user", exception);
            return ResponseEntity
                    .internalServerError()
                    .body(new ErrorResponse("Unexpected error"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginUserRequest request) {
        try {
            var token = userService.authenticateUser(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(token);
        } catch (AuthException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Wrong username or password"));
        }
        catch (Exception exception) {
            log.error("Error creating user", exception);
            return ResponseEntity
                    .internalServerError()
                    .body(new ErrorResponse("Unexpected error"));
        }
    }
}
