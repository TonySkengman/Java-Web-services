package se.ecutbildning.file_storage_api.models;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity implements UserDetails {

    @Id
    private final UUID id = UUID.randomUUID();

    private String username;
    private String password;

    private Date createdAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private List<FolderEntity> folders = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private List<FileEntity> files = new ArrayList<>();

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
        this.createdAt = new Date();
        this.folders = new ArrayList<>();
        this.files = new ArrayList<>();
    }

    @Override
    @Nonnull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }
}
