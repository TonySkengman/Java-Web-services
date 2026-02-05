package se.ecutbildning.file_storage_api.services;

import se.ecutbildning.file_storage_api.exceptions.CreateFolderException;
import se.ecutbildning.file_storage_api.models.FolderEntity;
import se.ecutbildning.file_storage_api.models.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FolderService {

    FolderEntity createFolder(CreateFolderRequest request, UserEntity user) throws CreateFolderException;
    List<FolderEntity> getAllFolders(int page, int size);
    Optional<FolderEntity> getFolderById(UUID folderId);
    Optional<FolderEntity> getFolderByIdWithFiles(UUID folderId);
    void deleteFolderById(UUID postId, UserEntity user);
}