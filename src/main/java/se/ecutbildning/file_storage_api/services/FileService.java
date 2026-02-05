package se.ecutbildning.file_storage_api.services;

import se.ecutbildning.file_storage_api.exceptions.CreateFileException;
import se.ecutbildning.file_storage_api.models.FileEntity;
import se.ecutbildning.file_storage_api.models.UserEntity;

public interface FileService {

    FileEntity createFile(CreateFileRequest request, UserEntity user) throws CreateFileException;

}
