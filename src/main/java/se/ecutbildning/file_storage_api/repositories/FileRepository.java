package se.ecutbildning.file_storage_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.ecutbildning.file_storage_api.models.FileEntity;
import se.ecutbildning.file_storage_api.models.UserEntity;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, UUID> {

    @Query("""
        SELECT f FROM files f
        WHERE f.id = :id AND f.folder.creator = :creator
    """)
    Optional<FileEntity> findByIdAndOwner(
            @Param("id") UUID id,
            @Param("creator") UserEntity creator
    );
}