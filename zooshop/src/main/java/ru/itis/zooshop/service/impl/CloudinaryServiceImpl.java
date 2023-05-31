package ru.itis.zooshop.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.zooshop.model.entity.ImageEntity;
import ru.itis.zooshop.service.CloudinaryService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Long INITIAL_LAST_IMAGE_ENTITY_ID = 29L;
    private final String CLOUD_NAME = "dt3julaa1";
    private final String API_KEY = "665922269678922";
    private final String API_SECRET = "BhJf6YKz92ZMWT_TcD16KlxcYhw";

    private final String IMAGE_FOLDER = "/";
    private final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", CLOUD_NAME,
            "api_key", API_KEY,
            "api_secret", API_SECRET,
            "secure", true));

@Override
    public ImageEntity uploadPhoto(MultipartFile photo) throws IOException {
        uploadPhotoToServer(photo);

        Map uploadResult = cloudinary.uploader()
                .upload(new File(IMAGE_FOLDER + photo.getOriginalFilename()), ObjectUtils.emptyMap());

        Object url = uploadResult.get("url");
        Object publicId = uploadResult.get("public_id");

        ImageEntity imageEntity = new ImageEntity()
                .setImageUrl(url.toString())
                .setPublicId(publicId.toString());

        deletePhotoFromServer(photo.getOriginalFilename());

        return imageEntity;
    }
@Override
    public void deletePhoto(ImageEntity imageEntity) throws IOException {

        if (imageEntity.getId() > INITIAL_LAST_IMAGE_ENTITY_ID) {
            cloudinary.uploader().destroy(imageEntity.getPublicId(),ObjectUtils.emptyMap());
        }
    }

    private void deletePhotoFromServer(String fileName) throws IOException {
        File file = new File(IMAGE_FOLDER + fileName);
        FileUtils.forceDelete(file);
    }

    private void uploadPhotoToServer(MultipartFile photo) throws IOException {
        Files.copy(photo.getInputStream(),
                Paths.get(IMAGE_FOLDER + File.separator + photo.getOriginalFilename()),
                StandardCopyOption.REPLACE_EXISTING);
    }

}
