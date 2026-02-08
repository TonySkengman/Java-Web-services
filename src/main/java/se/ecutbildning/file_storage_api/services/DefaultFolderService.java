package se.ecutbildning.file_storage_api.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se.ecutbildning.file_storage_api.dtos.CreateFolderRequest;
import se.ecutbildning.file_storage_api.exceptions.CreateFolderException;
import se.ecutbildning.file_storage_api.exceptions.DeleteFolderNotFoundException;
import se.ecutbildning.file_storage_api.exceptions.InvalidFolderContentException;
import se.ecutbildning.file_storage_api.models.FolderEntity;
import se.ecutbildning.file_storage_api.models.UserEntity;
import se.ecutbildning.file_storage_api.repositories.FolderRepository;
import se.ecutbildning.file_storage_api.repositories.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultFolderService implements FolderService {

    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    @Override
    public FolderEntity createFolder(CreateFolderRequest request, UserEntity user) throws CreateFolderException {
        if (request.getFolderName().length() < 3 || request.getFolderName().length() > 49) {
            throw new InvalidFolderContentException();
        }

        var folder = new FolderEntity(request.getFolderName(), user);
        folder = folderRepository.save(folder);
        log.info("Folder with id '{}' created", folder.getId());
        return folder;
    }

    @Override
    public List<FolderEntity> getAllFolders(int page, int size, UserEntity user) {
        if (size > 100) {
            size = 100;
        }

        if (size < 0) {
            size = 0;
        }

        if (page < 0) {
            page = 0;
        }

        Pageable pageable = PageRequest.of(page, size);
        return folderRepository.findAllByCreator(user, pageable).getContent();
    }

    @Override
    public Optional<FolderEntity> getFolderById(UUID folderId, UserEntity user) {
        return folderRepository.findByIdAndCreator(folderId, user);
    }

    @Override
    public Optional<FolderEntity> getFolderByIdWithFiles(UUID folderId, UserEntity user) {
        return folderRepository.findByIdWithFilesAndCreator(folderId, user);
    }

    @Override
    public void deleteFolderById(UUID folderId, UserEntity user) {
        var folder = folderRepository.findById(folderId)
                .orElseThrow(DeleteFolderNotFoundException::new);

        if (!folder.getCreator().getId().equals(user.getId())) {
            throw new DeleteFolderNotFoundException();
        }

        folderRepository.delete(folder);
    }
}
