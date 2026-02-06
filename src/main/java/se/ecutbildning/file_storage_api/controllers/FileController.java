package se.ecutbildning.file_storage_api.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.ecutbildning.file_storage_api.dtos.*;
import se.ecutbildning.file_storage_api.exceptions.CreateFileFolderNotFoundException;
import se.ecutbildning.file_storage_api.exceptions.DeleteFileNotFoundException;
import se.ecutbildning.file_storage_api.exceptions.InvalidFileContentException;
import se.ecutbildning.file_storage_api.models.UserEntity;
import se.ecutbildning.file_storage_api.services.FileService;
import se.ecutbildning.file_storage_api.services.FolderService;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;
    private final FolderService folderService;

    @PostMapping
    public ResponseEntity<?> createFile(
            @RequestBody CreateFileRequest request,
            @AuthenticationPrincipal UserEntity user
    ) {
        try {
            var file = fileService.createFile(request, user);
            return ResponseEntity.created(URI.create("/file"))
                    .body(FileResponse.fromModel(file));
        } catch (CreateFileFolderNotFoundException ignored) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Folder could not be found"));
        } catch (InvalidFileContentException ignored) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("File must be at least 6 characters"));
        } catch (Exception exception) {
            log.error("Error creating file", exception);
            return ResponseEntity
                    .internalServerError()
                    .body(new ErrorResponse("Unexpected error"));
        }
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteFileById(
            @PathVariable UUID fileId,
            @AuthenticationPrincipal UserEntity user
    ) {
        try {
            fileService.deleteFileById(fileId, user);
            return ResponseEntity.noContent().build();
        } catch (DeleteFileNotFoundException ignored) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("File could not be found"));
        } catch (Exception exception) {
            log.error("Error deleting file", exception);
            return ResponseEntity
                    .internalServerError()
                    .body(new ErrorResponse("Unexpected error"));
        }
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<FileResponse> getFileById(
            @PathVariable UUID fileId
    ) {
        return fileService.getFileById(fileId)
                .map(file -> ResponseEntity.ok(
                        FileResponse.fromModel(file)
                ))
                .orElse(ResponseEntity.notFound().build());
    }
}