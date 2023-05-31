package ru.itis.zooshop.model.dto.offer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.zooshop.model.enums.CategoryEnum;
import ru.itis.zooshop.util.validation.NotEmptyImage;
import ru.itis.zooshop.util.validation.ValidImageFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class CreateOfferDTO {
    @NotEmpty(message = "Title can not be empty.")
    @Size(min = 3, max = 50)
    private String title;
    @NotNull(message = "Price can not be empty.")
    @DecimalMin(value = "0")
    private BigDecimal price;
    @NotNull(message = "Category can not be empty.")
    private CategoryEnum category;
    @NotEmptyImage(message = "Image is required.")
    @ValidImageFormat(message = "Invalid image format.")
    private MultipartFile[] imageUrl;
    @NotEmpty(message = "Description can not be empty.")
    @Size(min = 3, max = 150)
    private String description;


    public CreateOfferDTO setTitle(String title) {
        this.title = title;
        return this;
    }
    public CreateOfferDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public CreateOfferDTO setCategory(CategoryEnum category) {
        this.category = category;
        return this;
    }

}
