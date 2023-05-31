package ru.itis.zooshop.model.dto.offer;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.zooshop.model.enums.CategoryEnum;
import ru.itis.zooshop.util.validation.ValidImageFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
public class UpdateOfferDTO {
    @NotEmpty(message = "Title can not be empty.")
    @Size(min = 3, max = 50)
    private String title;
    @NotNull(message = "Price can not be empty.")
    @DecimalMin(value = "0")
    private BigDecimal price;
    @NotNull(message = "Category can not be empty.")
    private CategoryEnum category;
    private String imageUrl;
    @ValidImageFormat
    private MultipartFile[] images;
    @NotEmpty(message = "Description can not be empty.")
    @Size(min = 3, max = 150)
    private String description;


    public UpdateOfferDTO setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public UpdateOfferDTO setImages(MultipartFile[] images) {
        this.images = images;
        return this;
    }

    public UpdateOfferDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public UpdateOfferDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public UpdateOfferDTO setCategory(CategoryEnum category) {
        this.category = category;
        return this;
    }

    public UpdateOfferDTO setDescription(String description) {
        this.description = description;
        return this;
    }
}
