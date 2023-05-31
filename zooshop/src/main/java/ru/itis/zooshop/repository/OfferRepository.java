package ru.itis.zooshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itis.zooshop.model.entity.OfferEntity;
import ru.itis.zooshop.model.entity.UserEntity;

import java.util.List;

@Repository
public interface OfferRepository extends
        JpaRepository<OfferEntity,Long>,
        JpaSpecificationExecutor<OfferEntity> {
    List<OfferEntity> findByOrderByCreatedOnDesc();
    void deleteAllBySeller(UserEntity user);
    List<OfferEntity> findAllBySeller(UserEntity seller);
    Page<OfferEntity> findAllByIsActive(boolean isActive, Pageable pageable);
    Page<OfferEntity> findAllBySellerUsername(String username, Pageable pageable);
}
