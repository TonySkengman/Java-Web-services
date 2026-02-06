package se.ecutbildning.file_storage_api.dtos;

import lombok.Getter;
import lombok.Setter;
import se.ecutbildning.file_storage_api.models.UserEntity;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class UserResponse {

    private final UUID id;
    private String username;
    private Date createdAt;

    public UserResponse(UUID id, String username, Date createdAt) {
        this.id = id;
        this.username = username;
        this.createdAt = createdAt;
    }

    public static UserResponse fromModel(UserEntity user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getCreatedAt()
        );
    }
}
