package ru.itis.zooshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.zooshop.model.entity.ImageEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    Optional<ImageEntity> findByPublicId(String publicId);

    Optional<ImageEntity> findByImageUrl(String imageUrl);

    List<ImageEntity> findAllByIdGreaterThan(Long id);
}
