package se.ecutbildning.file_storage_api.dtos;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class ErrorResponse {

    private String message;
    private Date createdAt;

    public ErrorResponse(String message) {
        this.message = message;
        this.createdAt = new Date();
    }
}
