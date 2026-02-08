package se.ecutbildning.file_storage_api.dtos;

import lombok.Getter;
import lombok.Setter;
import se.ecutbildning.file_storage_api.models.FileEntity;
import se.ecutbildning.file_storage_api.models.UserEntity;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class FileResponse {

    private final UUID id;
    private String fileName;
    private String content;
    private Date createdAt;
    private UserResponse creator;
    private UUID folderId;

    public FileResponse(UUID id, String fileName, String content, Date createdAt, UserEntity user, UUID folderId) {
        this.id = id;
        this.fileName = fileName;
        this.content = content;
        this.createdAt = createdAt;
        this.creator = UserResponse.fromModel(user);
        this.folderId = folderId;
    }

    public static FileResponse fromModel(FileEntity file) {
        return new FileResponse(
                file.getId(),
                file.getFileName(),
                file.getContent(),
                file.getCreatedAt(),
                file.getCreator(),
                file.getFolder().getId()
        );
    }
}
