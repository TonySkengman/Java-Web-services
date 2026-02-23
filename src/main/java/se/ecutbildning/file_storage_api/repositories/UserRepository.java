package se.ecutbildning.file_storage_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.ecutbildning.file_storage_api.models.UserEntity;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByOidcIdAndOidcProvider(String oidcId, String oidcProvider);

}
