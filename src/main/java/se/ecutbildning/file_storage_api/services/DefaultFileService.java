package se.ecutbildning.file_storage_api.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.ecutbildning.file_storage_api.exceptions.CreateFileException;
import se.ecutbildning.file_storage_api.exceptions.CreateFileFolderNotFoundException;
import se.ecutbildning.file_storage_api.exceptions.InvalidFileContentException;
import se.ecutbildning.file_storage_api.models.FileEntity;
import se.ecutbildning.file_storage_api.models.UserEntity;
import se.ecutbildning.file_storage_api.repositories.FileRepository;
import se.ecutbildning.file_storage_api.repositories.FolderRepository;
import se.ecutbildning.file_storage_api.repositories.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultFileService implements FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    @Override
    public FileEntity createFile(CreateFileRequest request, UserEntity user) throws CreateFileException {
        var post = folderRepository.findById(request.getFolderId())
                .orElseThrow(CreateFileFolderNotFoundException::new);

        if (request.getContent().length() < 5) {
            throw new InvalidFileContentException();
        }

        var file = new FileEntity(request.getContent(), user, post);
        file = fileRepository.save(file);
        log.info("Created file with id '{}' for folder '{}'", file.getId(), post.getId());

        return file;
    }
}
