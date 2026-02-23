package se.ecutbildning.file_storage_api.dtos;

import lombok.Getter;
import lombok.Setter;
import se.ecutbildning.file_storage_api.controllers.FolderController;
import se.ecutbildning.file_storage_api.controllers.UserController;
import se.ecutbildning.file_storage_api.models.FileEntity;
import se.ecutbildning.file_storage_api.models.UserEntity;
import org.springframework.hateoas.RepresentationModel;
import java.util.Date;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
public class FileResponse extends RepresentationModel<FileResponse> {

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
        var response = new FileResponse(
                file.getId(),
                file.getName(),
                file.getContent(),
                file.getCreatedAt(),
                file.getCreator(),
                file.getFolder().getId()
        );

        response.add(linkTo(
                methodOn(FolderController.class).getFolderById(file.getFolder().getId(), false, null))
                .withRel("folder")
        );

        response.add(linkTo(
                methodOn(UserController.class).getUserById(file.getCreator().getId()))
                .withRel("user")
        );

        return response;
    }
}
