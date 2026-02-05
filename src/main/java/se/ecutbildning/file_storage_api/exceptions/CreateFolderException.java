package se.ecutbildning.file_storage_api.exceptions;

public class CreateFolderException extends RuntimeException {
    public CreateFolderException() {}

    public CreateFolderException(String message) {
        super(message);
    }

    public CreateFolderException(String message, Throwable inner) {
        super(message, inner);
    }
}
