package ru.itis.zooshop.service;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.zooshop.model.entity.ImageEntity;

import java.io.IOException;

public interface CloudinaryService {

    ImageEntity uploadPhoto(MultipartFile photo) throws IOException;

    void deletePhoto(ImageEntity imageEntity) throws IOException;
}
