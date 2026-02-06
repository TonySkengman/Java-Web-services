package se.ecutbildning.file_storage_api.exceptions;

public class DeleteFileException  extends RuntimeException {
    public DeleteFileException() {}

    public DeleteFileException(String message) {
        super(message);
    }

    public DeleteFileException(String message, Throwable inner) {
        super(message, inner);
    }
}
