package se.ecutbildning.file_storage_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.ecutbildning.file_storage_api.models.FolderEntity;

import java.util.Optional;
import java.util.UUID;

public interface FolderRepository extends JpaRepository<FolderEntity, UUID> {
    @Query("SELECT p FROM folders p LEFT JOIN FETCH p.files WHERE p.id = :id")
    Optional<FolderEntity> findByIdWithFiles(@Param("id") UUID id);
}
