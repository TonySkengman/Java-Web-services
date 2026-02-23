package se.ecutbildning.file_storage_api.dtos;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import se.ecutbildning.file_storage_api.controllers.FolderController;
import se.ecutbildning.file_storage_api.controllers.UserController;
import se.ecutbildning.file_storage_api.models.FolderEntity;
import se.ecutbildning.file_storage_api.models.UserEntity;
import java.util.Date;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
public class FolderResponse extends RepresentationModel<FolderResponse>  {

    private final UUID id;
    private String folderName;
    private Date createdAt;
    private UserResponse creator;

    public FolderResponse(UUID id, String folderName, Date createdAt, UserEntity creator) {
        this.id = id;
        this.folderName = folderName;
        this.creator = UserResponse.fromModel(creator);
        this.createdAt = createdAt;
    }

    public static FolderResponse fromModel(FolderEntity folder) {
        var response = new FolderResponse(
                folder.getId(),
                folder.getName(),
                folder.getCreatedAt(),
                folder.getCreator()
        );


        response.add(linkTo(
                methodOn(UserController.class).getUserById(folder.getCreator().getId()))
                .withRel("user")
        );

        response.add(linkTo(
                methodOn(FolderController.class).getFolderById(folder.getId(), false, null))
                .withSelfRel()
        );

        return response;
    }
}
