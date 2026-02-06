package se.ecutbildning.file_storage_api.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateFileRequest {
    private String content;
    private UUID folderId;
}
