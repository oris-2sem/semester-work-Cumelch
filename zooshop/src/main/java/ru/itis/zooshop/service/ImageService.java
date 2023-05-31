package ru.itis.zooshop.service;

import ru.itis.zooshop.model.entity.ImageEntity;

import java.util.List;

public interface ImageService {
    List<ImageEntity> saveAll(List<ImageEntity> imageEntities);
    void deleteAll(List<ImageEntity> imageEntities);
    void deleteAllNotInitialPhotos();
}
