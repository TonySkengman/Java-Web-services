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

    /**
     * Skapar en ny fil i en användares mapp.
     *
     * @param request filskapandeförfrågan
     * @param user filens ägare
     * @return den skapade FileEntity
     * @throws CreateFileException om mappen inte finns, användaren inte äger mappen eller innehållet är ogiltigt
     */

    @Override
    public FileEntity createFile(CreateFileRequest request, UserEntity user) throws CreateFileException {
        var folder = folderRepository.findById(request.getFolderId())
                .orElseThrow(CreateFileFolderNotFoundException::new);

        if (!folder.getCreator().getId().equals(user.getId())) {
            throw new CreateFileFolderNotFoundException();
        }

        if (request.getFileName() == null || request.getFileName().isBlank() || request.getFileName().length() < 3) {
            throw new InvalidFileNameException();
        }

        if (request.getContent().length() < 5) {
            throw new InvalidFileContentException();
        }

        var file = new FileEntity(request.getFileName(), request.getContent(), user, folder);
        file = fileRepository.save(file);
        log.info("Created file with id '{}' for folder '{}'", file.getId(), folder.getId());

        return file;
    }

    /**
     * Tar bort en fil baserat på ID för en specifik användare.
     *
     * @param fileId filens UUID
     * @param user ägare
     * @throws DeleteFileNotFoundException om filen inte finns eller användaren inte är ägare
     */

    @Override
    public void deleteFileById(UUID fileId, UserEntity user) {
        var file = fileRepository.findById(fileId)
                .orElseThrow(DeleteFileNotFoundException::new);

        if (!file.getCreator().getId().equals(user.getId())) {
            throw new DeleteFileNotFoundException();
        }

        fileRepository.delete(file);
    }

    /**
     * Hämtar en fil baserat på ID för en specifik användare.
     *
     * @param fileId filens UUID
     * @param user ägare
     * @return ett Optional som innehåller filen om den finns
     */

    @Override
    public Optional<FileEntity> getFileById(UUID fileId, UserEntity user) {
        return fileRepository.findByIdAndOwner(fileId, user);
    }
}

