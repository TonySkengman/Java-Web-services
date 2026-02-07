package se.ecutbildning.file_storage_api.dtos;

import lombok.Getter;
import lombok.Setter;
import se.ecutbildning.file_storage_api.models.FileEntity;
import se.ecutbildning.file_storage_api.models.FolderEntity;
import se.ecutbildning.file_storage_api.models.UserEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class FolderWithFilesResponse {
    private final UUID id;
    private String content;
    private Date createdAt;
    private UserResponse creator;
    private List<FileResponse> files;

    public FolderWithFilesResponse(
            UUID id,
            String content,
            Date createdAt,
            UserEntity creator,
            List<FileEntity> files
    ) {
        this.id = id;
        this.content = content;
        this.creator = UserResponse.fromModel(creator);
        this.createdAt = createdAt;
        this.files = files.stream().map(FileResponse::fromModel).toList();
    }

    public static FolderWithFilesResponse fromModel(FolderEntity folder) {
        return new FolderWithFilesResponse(
                folder.getId(),
                folder.getName(),
                folder.getCreatedAt(),
                folder.getCreator(),
                folder.getFiles()
        );
    }
}
