package ru.itis.zooshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itis.zooshop.model.entity.CategoryEntity;
import ru.itis.zooshop.model.entity.CommentEntity;
import ru.itis.zooshop.model.entity.OfferEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    Optional<List<CommentEntity>> findAllByOffer(OfferEntity offer);

    @Query("SELECT c FROM CommentEntity c WHERE c.author IN (SELECT u FROM UserEntity u WHERE u.username = :username)")
    Optional<List<CommentEntity>> findAllByUsername(String username);
}
