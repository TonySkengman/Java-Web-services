package se.ecutbildning.file_storage_api.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.ecutbildning.file_storage_api.dtos.CreateFolderRequest;
import se.ecutbildning.file_storage_api.dtos.ErrorResponse;
import se.ecutbildning.file_storage_api.dtos.FolderResponse;
import se.ecutbildning.file_storage_api.dtos.FolderWithFilesResponse;
import se.ecutbildning.file_storage_api.exceptions.DeleteFileNotFoundException;
import se.ecutbildning.file_storage_api.exceptions.DeleteFolderNotFoundException;
import se.ecutbildning.file_storage_api.exceptions.InvalidFolderContentException;
import se.ecutbildning.file_storage_api.models.UserEntity;
import se.ecutbildning.file_storage_api.services.FolderService;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/folder")
@RequiredArgsConstructor
@Slf4j
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    public ResponseEntity<?> createFolder(
            @RequestBody CreateFolderRequest request,
            @AuthenticationPrincipal UserEntity user
    ) {
        try {
            var folder = folderService.createFolder(request, user);
            return ResponseEntity.created(URI.create("/folder")).body(FolderResponse.fromModel(folder));
        } catch (InvalidFolderContentException ignored) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("Content must be between 5 and 200 characters"));
        } catch (Exception exception) {
            log.error("Error creating folder", exception);
            return ResponseEntity
                    .internalServerError()
                    .body(new ErrorResponse("Unexpected error"));
        }
    }

    @GetMapping("/all")
    public List<FolderResponse> getAllFolders(
            @RequestParam int size,
            @RequestParam int page
    ) {
        return folderService
                .getAllFolders(page, size)
                .stream()
                .map(FolderResponse::fromModel)
                .toList();
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<?> getFolderById(
            @PathVariable UUID folderId,
            @RequestParam(required = false) boolean includeFiles
    ) {
        if (includeFiles) {
            return folderService.getFolderByIdWithFiles(folderId)
                    .map(folder -> ResponseEntity.ok(FolderWithFilesResponse.fromModel(folder)))
                    .orElse(ResponseEntity.notFound().build());
        } else {
            return folderService.getFolderById(folderId)
                    .map(folder -> ResponseEntity.ok(FolderResponse.fromModel(folder)))
                    .orElse(ResponseEntity.notFound().build());
        }
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<?> deleteFolderById(
            @PathVariable UUID folderId,
            @AuthenticationPrincipal UserEntity user
    ) {
        try {
            folderService.deleteFolderById(folderId, user);
            return ResponseEntity.noContent().build();
        } catch (DeleteFolderNotFoundException ignored) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Folder could not be found"));
        } catch (Exception exception) {
            log.error("Error deleting folder", exception);
            return ResponseEntity
                    .internalServerError()
                    .body(new ErrorResponse("Unexpected error"));
        }
    }
}
