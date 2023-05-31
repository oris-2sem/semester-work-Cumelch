package ru.itis.zooshop.service.impl;

import org.springframework.stereotype.Service;
import ru.itis.zooshop.model.entity.ImageEntity;
import ru.itis.zooshop.repository.ImageRepository;
import ru.itis.zooshop.service.ImageService;
import ru.itis.zooshop.service.impl.CloudinaryServiceImpl;

import java.io.IOException;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    private final Long INITIAL_LAST_IMAGE_ENTITY_ID = 29L;
    private final ImageRepository imageRepository;
    private final CloudinaryServiceImpl cloudinaryServiceImpl;

    public ImageServiceImpl(ImageRepository imageRepository, CloudinaryServiceImpl cloudinaryServiceImpl) {
        this.imageRepository = imageRepository;
        this.cloudinaryServiceImpl = cloudinaryServiceImpl;
    }
    public List<ImageEntity> saveAll(List<ImageEntity> imageEntities) {
        return this.imageRepository.saveAll(imageEntities);
    }

    public void deleteAll(List<ImageEntity> imageEntities) {
         this.imageRepository.deleteAll(imageEntities);
    }

    public void deleteAllNotInitialPhotos() {
        List<ImageEntity> allByIdGreater =
                this.imageRepository
                .findAllByIdGreaterThan(INITIAL_LAST_IMAGE_ENTITY_ID);
        for (ImageEntity imageEntity : allByIdGreater) {
            try {
                cloudinaryServiceImpl.deletePhoto(imageEntity);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
