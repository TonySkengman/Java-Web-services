package se.ecutbildning.file_storage_api.dtos;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateFolderRequest {
    private String folderName;
}
