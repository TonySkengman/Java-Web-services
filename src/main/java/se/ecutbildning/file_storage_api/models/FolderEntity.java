package se.ecutbildning.file_storage_api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity(name = "folders")
@Getter
@Setter
@NoArgsConstructor
public class FolderEntity {

    @Id
    private final UUID id = UUID.randomUUID();

    private String content;
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity creator;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FileEntity> files = new ArrayList<>();

    public FolderEntity(String content, UserEntity creator) {
        this.content = content;
        this.creator = creator;
        this.createdAt = new Date();
    }
}
