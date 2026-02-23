package se.ecutbildning.file_storage_api.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import se.ecutbildning.file_storage_api.controllers.FolderController;
import se.ecutbildning.file_storage_api.controllers.UserController;
import se.ecutbildning.file_storage_api.models.FileEntity;
import se.ecutbildning.file_storage_api.models.FolderEntity;
import se.ecutbildning.file_storage_api.models.UserEntity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class FolderWithFilesResponse extends RepresentationModel<FolderWithFilesResponse> {
    private final UUID id;
    private String folderName;
    private Date createdAt;
    private UserResponse creator;
    private List<FileResponse> files;

    public FolderWithFilesResponse(
            UUID id,
            String folderName,
            Date createdAt,
            UserEntity creator,
            List<FileEntity> files
    ) {
        this.id = id;
        this.folderName = folderName;
        this.creator = UserResponse.fromModel(creator);
        this.createdAt = createdAt;
        this.files = files.stream().map(FileResponse::fromModel).toList();
    }

    public static FolderWithFilesResponse fromModel(FolderEntity folder) {
        var response = new FolderWithFilesResponse(
                folder.getId(),
                folder.getName(),
                folder.getCreatedAt(),
                folder.getCreator(),
                folder.getFiles()
        );

        response.add(linkTo(
                methodOn(FolderController.class).getFolderById(folder.getId(), true, null))
                .withSelfRel()
        );

        response.add(linkTo(
                methodOn(FolderController.class).getFolderById(folder.getId(), false, null))
                .withRel("folder-without-files")
        );

        response.add(linkTo(
                methodOn(UserController.class).getUserById(folder.getCreator().getId()))
                .withRel("creator")
        );

        return response;
    }
}

