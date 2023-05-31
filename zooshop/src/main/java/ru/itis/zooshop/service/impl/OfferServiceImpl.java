package ru.itis.zooshop.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.zooshop.exceptions.ExceptionConstants;
import ru.itis.zooshop.exceptions.ImageNotFoundException;
import ru.itis.zooshop.exceptions.OfferNotFoundException;
import ru.itis.zooshop.exceptions.UserNotFoundException;
import ru.itis.zooshop.model.dto.offer.CreateOfferDTO;
import ru.itis.zooshop.model.dto.offer.SearchOfferDTO;
import ru.itis.zooshop.model.dto.offer.UpdateOfferDTO;
import ru.itis.zooshop.model.entity.CategoryEntity;
import ru.itis.zooshop.model.entity.ImageEntity;
import ru.itis.zooshop.model.entity.OfferEntity;
import ru.itis.zooshop.model.entity.UserEntity;
import ru.itis.zooshop.model.enums.UserRoleEnum;
import ru.itis.zooshop.model.view.OfferDetailsView;
import ru.itis.zooshop.model.view.OfferResponse;
import ru.itis.zooshop.repository.OfferRepository;
import ru.itis.zooshop.repository.OfferSpecification;
import ru.itis.zooshop.repository.UserRepository;
import ru.itis.zooshop.exceptions.CategoryNotFoundException;
import ru.itis.zooshop.service.OfferService;
import ru.itis.zooshop.service.impl.CategoryServiceImpl;
import ru.itis.zooshop.service.impl.CloudinaryServiceImpl;
import ru.itis.zooshop.service.impl.ImageServiceImpl;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final CategoryServiceImpl categoryServiceImpl;
    private final ImageServiceImpl imageServiceImpl;
    private final ModelMapper mapper;
    private final CloudinaryServiceImpl cloudinaryServiceImpl;

    public OfferServiceImpl(OfferRepository offerRepository, UserRepository userRepository, CategoryServiceImpl categoryServiceImpl, ImageServiceImpl imageServiceImpl, ModelMapper mapper, CloudinaryServiceImpl cloudinaryServiceImpl) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.categoryServiceImpl = categoryServiceImpl;
        this.imageServiceImpl = imageServiceImpl;
        this.mapper = mapper;
        this.cloudinaryServiceImpl = cloudinaryServiceImpl;
    }
    @Transactional
    public void addOffer(CreateOfferDTO addOfferDTO, UserDetails userDetails) {
        OfferEntity newOffer = mapper.map(addOfferDTO, OfferEntity.class);
        UserEntity seller = userRepository
                .findByUsername(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);
        System.out.println(seller);
        CategoryEntity categoryEntity = categoryServiceImpl
                .findByName(addOfferDTO.getCategory())
                .orElseThrow(CategoryNotFoundException::new);

        List<ImageEntity> imageEntityList =
                Arrays.stream(addOfferDTO.getImageUrl())
                .map(this::uploadImageEntity)
                .toList();

        imageEntityList.forEach(e->e.setOffer(newOffer));

        this.imageServiceImpl.saveAll(imageEntityList);

        newOffer
                .setImagesEntities(imageEntityList)
                .setCategory(categoryEntity)
                .setSeller(seller)
                .setActive(false)
                .setCreatedOn(LocalDateTime.now());

        if (userDetails.getAuthorities()
                .stream()
                .anyMatch(o->o.getAuthority().equals("ROLE_ADMIN"))) {
            newOffer.setActive(true);
        }
        offerRepository.save(newOffer);
    }

    public List<OfferDetailsView> getRecentOffers(int limit) {
        List<OfferEntity> byOrderByCreatedOnDesc =
                this.offerRepository
                        .findByOrderByCreatedOnDesc()
                        .stream()
                        .filter(OfferEntity::isActive)
                        .toList();

        if (byOrderByCreatedOnDesc.size() == 0) {
            return null;
        }

        if (byOrderByCreatedOnDesc.size() < limit) {
            return byOrderByCreatedOnDesc
                    .stream()
                    .map(this::map)
                    .collect(Collectors.toList());
        }

        return byOrderByCreatedOnDesc.subList(0, limit)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public Page<OfferDetailsView> getAllOffers(Pageable pageable) {
        return  this.offerRepository
                .findAllByIsActive(true,pageable)
                .map(this::map);
    }

    public Page<OfferDetailsView> getAllOffers(Pageable pageable,boolean isActive) {
        return  this.offerRepository
                .findAllByIsActive(isActive,pageable)
                .map(this::map);
    }

    public Page<OfferDetailsView> getAllUserOffers(String username, Pageable pageable) {
        this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format(ExceptionConstants.USER_NOT_FOUND, username)));
        return this.offerRepository
                .findAllBySellerUsername(username, pageable)
                .map(this::map);
    }

    public List<OfferEntity> getAllOffersBySeller(UserEntity seller) {
        return this.offerRepository.findAllBySeller(seller);
    }

    public UpdateOfferDTO getEditOfferById(Long id) {
        OfferEntity offerEntity =
                this.offerRepository
                .findById(id)
                .orElseThrow(() -> new OfferNotFoundException(id));

        OfferDetailsView map = mapper.map(offerEntity, OfferDetailsView.class);

        UpdateOfferDTO updateOfferDTO = mapper.map(map, UpdateOfferDTO.class);
        updateOfferDTO.setImageUrl(offerEntity.getImagesEntities().get(0).getImageUrl());
        return updateOfferDTO;
    }

    public OfferDetailsView getOfferById(Long id) {
        OfferEntity offerEntity = this.offerRepository
                .findById(id)
                .orElseThrow(() -> new OfferNotFoundException(id));

        UserEntity seller = this.userRepository
                .findById(offerEntity.getSeller().getId())
                .orElseThrow(UserNotFoundException::new);

        OfferDetailsView offerDetailsView = map(offerEntity);

        return offerDetailsView
                .setSellerFirstName(seller.getFirstName())
                .setSellerLastName(seller.getLastName())
                .setSellerUsername(seller.getUsername())
                .setSellerEmail(seller.getEmail())
                .setSellerPhone(seller.getPhone());
    }

    public boolean isOwner(String username, Long offerId) {
        boolean isOwner = offerRepository.
                findById(offerId).
                filter(o -> o.getSeller().getUsername().equals(username)).
                isPresent();

        if (isOwner) {
            return true;
        }

        return userRepository.
                findByUsername(username).
                filter(this::isAdmin).
                isPresent();
    }
    private boolean isAdmin(UserEntity user) {
        return user.getUserRoles().
                stream().
                anyMatch(r -> r.getUserRoleEnum() == UserRoleEnum.ADMIN);
    }
    private OfferDetailsView map(OfferEntity offerEntity) {
        return mapper.map(offerEntity, OfferDetailsView.class)
                .setImagesUrls(
                        offerEntity.getImagesEntities()
                        .stream()
                        .map(ImageEntity::getImageUrl)
                                .collect(Collectors.toList()));
    }

    @Transactional
    public void deleteOfferById(Long id) {
        OfferEntity offer = this.offerRepository
                .findById(id)
                .orElseThrow(() -> new OfferNotFoundException(id));

        deleteOfferImageCloudinary(offer);
        offer.setSeller(null);
        this.offerRepository.save(offer);
        this.offerRepository.delete(offer);
    }

    @Transactional
    public void editOffer(Long id, UpdateOfferDTO editOffer) {
        OfferEntity offerById = this.offerRepository
                .findById(id)
                .orElseThrow(() -> new OfferNotFoundException(id));

        CategoryEntity categoryEntity = categoryServiceImpl
                .findByName(editOffer.getCategory())
                .orElseThrow(CategoryNotFoundException::new);

        offerById
                .setTitle(editOffer.getTitle())
                .setPrice(editOffer.getPrice())
                .setDescription(editOffer.getDescription())
                .setCategory(categoryEntity);

        MultipartFile[] newImages = editOffer.getImages();
        if (newImages.length > 0 && !newImages[0].getOriginalFilename().isEmpty()) {
            deleteOldImagesAndUploadNewOnes(offerById, newImages);
        }

        this.offerRepository.save(offerById);
    }

    private void deleteOldImagesAndUploadNewOnes(OfferEntity offerById, MultipartFile[] newImages) {
        deleteOfferImageCloudinary(offerById);

        this.imageServiceImpl.deleteAll(offerById.getImagesEntities());
        offerById.removeAllImages();

        List<ImageEntity> newImagesEntities =
                Arrays.stream(newImages)
                        .map(this::uploadImageEntity)
                        .collect(Collectors.toList());
        newImagesEntities.forEach(i -> i.setOffer(offerById));

        offerById.setImagesEntities(newImagesEntities);
        this.imageServiceImpl.saveAll(newImagesEntities)
                 .forEach(offerById::addImage);
    }

    private void deleteOfferImageCloudinary(OfferEntity offerById) {
        for (ImageEntity imageEntity : offerById.getImagesEntities()) {
            try {
                this.cloudinaryServiceImpl.deletePhoto(imageEntity);
            } catch (IOException e) {
                throw new ImageNotFoundException();
            }
        }
    }

    private ImageEntity uploadImageEntity(MultipartFile image) {
        System.out.println(image);
        ImageEntity imageEntity;
        try {
             imageEntity = this.cloudinaryServiceImpl.uploadPhoto(image);
        } catch (IOException e) {
            throw new ImageNotFoundException(e.getMessage());
        }
        return imageEntity;
    }

    public Page<OfferDetailsView> searchOffer(SearchOfferDTO searchOfferDTO, Pageable pageable) {
        return this.offerRepository
                .findAll(new OfferSpecification(searchOfferDTO, categoryServiceImpl.getRepository()), pageable)
                .map(this::map);
    }

    public OfferResponse getAllOffersAdminRest(int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = getPageable(pageNo, pageSize, sortBy, sortDir);

        Page<OfferDetailsView> offers = this.getAllOffers(pageable,true);
        List<OfferDetailsView> listOfOffers = offers.getContent();

        return new OfferResponse()
                .setContent(listOfOffers)
                .setPageNo(offers.getNumber())
                .setPageSize(offers.getSize())
                .setTotalElements(offers.getTotalElements())
                .setTotalPages(offers.getTotalPages())
                .setLast(offers.isLast());
    }

    private static Pageable getPageable(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        return PageRequest.of(pageNo, pageSize, sort);
    }

    public long getAllOffersCount() {
        return this.offerRepository.count();
    }

    public OfferResponse getAllNotApproveOffers(int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = getPageable(pageNo, pageSize, sortBy, sortDir);

        Page<OfferDetailsView> offers = this.getAllOffers(pageable,false);

        List<OfferDetailsView> listOfOffers = offers.getContent();

        return new OfferResponse()
                .setContent(listOfOffers)
                .setPageNo(offers.getNumber())
                .setPageSize(offers.getSize())
                .setTotalElements(offers.getTotalElements())
                .setTotalPages(offers.getTotalPages())
                .setLast(offers.isLast());

    }

    public void approveOfferById(Long id) {
        Optional<OfferEntity> offerEntity = this.offerRepository.findById(id);
        offerEntity.ifPresent(offer -> this.offerRepository.save(offer.setActive(true).setCreatedOn(LocalDateTime.now())));
    }

    public Optional<OfferEntity> findById(Long offerId) {
        return this.offerRepository.findById(offerId);
    }
}
