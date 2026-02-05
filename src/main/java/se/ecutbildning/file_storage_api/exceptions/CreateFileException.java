package se.ecutbildning.file_storage_api.exceptions;

public class CreateFileException extends RuntimeException {
    public CreateFileException() {}

    public CreateFileException(String message) {
        super(message);
    }

    public CreateFileException(String message, Throwable inner) {
        super(message, inner);
    }
}
