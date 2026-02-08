package se.ecutbildning.file_storage_api.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.ecutbildning.file_storage_api.dtos.CreateFileRequest;
import se.ecutbildning.file_storage_api.exceptions.*;
import se.ecutbildning.file_storage_api.models.FileEntity;
import se.ecutbildning.file_storage_api.models.UserEntity;
import se.ecutbildning.file_storage_api.repositories.FileRepository;
import se.ecutbildning.file_storage_api.repositories.FolderRepository;
import se.ecutbildning.file_storage_api.repositories.UserRepository;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultFileService implements FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    @Override
    public FileEntity createFile(CreateFileRequest request, UserEntity user) throws CreateFileException {
        var folder = folderRepository.findById(request.getFolderId())
                .orElseThrow(CreateFileFolderNotFoundException::new);

        if (!folder.getCreator().getId().equals(user.getId())) {
            throw new CreateFileFolderNotFoundException();
        }

        if (request.getContent().length() < 5) {
            throw new InvalidFileContentException();
        }

        var file = new FileEntity(request.getContent(), user, folder);
        file = fileRepository.save(file);
        log.info("Created file with id '{}' for folder '{}'", file.getId(), folder.getId());

        return file;
    }

    @Override
    public void deleteFileById(UUID fileId, UserEntity user) {
        var file = fileRepository.findById(fileId)
                .orElseThrow(DeleteFileNotFoundException::new);

        if (!file.getCreator().getId().equals(user.getId())) {
            throw new DeleteFileNotFoundException();
        }

        fileRepository.delete(file);
    }

    @Override
    public Optional<FileEntity> getFileById(UUID fileId, UserEntity user) {
        return fileRepository.findByIdAndOwner(fileId, user);
    }
}

