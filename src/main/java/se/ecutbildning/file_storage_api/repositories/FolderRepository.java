package se.ecutbildning.file_storage_api.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.ecutbildning.file_storage_api.models.FolderEntity;
import se.ecutbildning.file_storage_api.models.UserEntity;
import java.util.Optional;
import java.util.UUID;

public interface FolderRepository extends JpaRepository<FolderEntity, UUID> {

    Page<FolderEntity> findAllByCreator(UserEntity creator, Pageable pageable);
    Optional<FolderEntity> findByIdAndCreator(UUID id, UserEntity creator);

    @Query("""
        SELECT f FROM folders f
        LEFT JOIN FETCH f.files
        WHERE f.id = :id AND f.creator = :creator
    """)
    Optional<FolderEntity> findByIdWithFilesAndCreator(
            @Param("id") UUID id,
            @Param("creator") UserEntity creator
    );
}