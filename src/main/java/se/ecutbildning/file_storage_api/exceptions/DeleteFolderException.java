package se.ecutbildning.file_storage_api.exceptions;

public class DeleteFolderException  extends RuntimeException {
    public DeleteFolderException() {}

    public DeleteFolderException(String message) {
        super(message);
    }

    public DeleteFolderException(String message, Throwable inner) {
        super(message, inner);
    }
}
