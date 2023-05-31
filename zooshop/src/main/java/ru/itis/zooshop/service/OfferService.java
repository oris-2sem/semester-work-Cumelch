package ru.itis.zooshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.zooshop.model.dto.offer.CreateOfferDTO;
import ru.itis.zooshop.model.dto.offer.UpdateOfferDTO;
import ru.itis.zooshop.model.entity.ImageEntity;
import ru.itis.zooshop.model.entity.OfferEntity;
import ru.itis.zooshop.model.entity.UserEntity;
import ru.itis.zooshop.model.view.OfferDetailsView;
import ru.itis.zooshop.model.view.OfferResponse;

import java.util.List;
import java.util.Optional;

public interface OfferService {

    void addOffer(CreateOfferDTO addOfferDTO, UserDetails userDetails);
    List<OfferDetailsView> getRecentOffers(int limit);
    Page<OfferDetailsView> getAllOffers(Pageable pageable);
    Page<OfferDetailsView> getAllOffers(Pageable pageable,boolean isActive);
    Page<OfferDetailsView> getAllUserOffers(String username, Pageable pageable);
    List<OfferEntity> getAllOffersBySeller(UserEntity seller);
    UpdateOfferDTO getEditOfferById(Long id);
    OfferDetailsView getOfferById(Long id);
    boolean isOwner(String username, Long offerId);
    Optional<OfferEntity> findById(Long offerId);
    void approveOfferById(Long id);
    OfferResponse getAllNotApproveOffers(int pageNo, int pageSize, String sortBy, String sortDir);
    OfferResponse getAllOffersAdminRest(int pageNo, int pageSize, String sortBy, String sortDir);
}
