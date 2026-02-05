package se.ecutbildning.file_storage_api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity(name = "files")
@Getter
@Setter
@NoArgsConstructor
public class FileEntity {

    @Id
    private final UUID id = UUID.randomUUID();

    private String content;
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private UserEntity creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private FolderEntity folder;

    public FileEntity (String content, UserEntity creator, FolderEntity folder) {
        this.content = content;
        this.creator = creator;
        this.folder = folder;
        this.createdAt = new Date();
    }
}
