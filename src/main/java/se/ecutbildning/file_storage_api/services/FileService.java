package se.ecutbildning.file_storage_api.services;

import se.ecutbildning.file_storage_api.dtos.CreateFileRequest;
import se.ecutbildning.file_storage_api.exceptions.CreateFileException;
import se.ecutbildning.file_storage_api.models.FileEntity;
import se.ecutbildning.file_storage_api.models.UserEntity;
import java.util.Optional;
import java.util.UUID;

public interface FileService {

    FileEntity createFile(CreateFileRequest request, UserEntity user) throws CreateFileException;
    void deleteFileById(UUID fileId, UserEntity user);
    Optional<FileEntity> getFileById(UUID fileId, UserEntity user);

}
