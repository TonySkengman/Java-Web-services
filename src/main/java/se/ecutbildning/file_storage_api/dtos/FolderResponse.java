package se.ecutbildning.file_storage_api.dtos;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import se.ecutbildning.file_storage_api.models.FolderEntity;
import se.ecutbildning.file_storage_api.models.UserEntity;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class FolderResponse {

    private final UUID id;
    private String content;
    private Date createdAt;
    private UserResponse creator;

    public FolderResponse(UUID id, String content, Date createdAt, UserEntity creator) {
        this.id = id;
        this.content = content;
        this.creator = UserResponse.fromModel(creator);
        this.createdAt = createdAt;
    }

    public static FolderResponse fromModel(FolderEntity folder) {
        return new FolderResponse(
                folder.getId(),
                folder.getContent(),
                folder.getCreatedAt(),
                folder.getCreator()
        );
    }
}
