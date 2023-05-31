package ru.itis.zooshop.model.dto.offer;

import lombok.Getter;
import ru.itis.zooshop.model.enums.CategoryEnum;
import javax.validation.constraints.PositiveOrZero;

@Getter
public class SearchOfferDTO {
    private String name;
    @PositiveOrZero
    private Integer minPrice;
    @PositiveOrZero
    private Integer maxPrice;
    private CategoryEnum category;
    private boolean isActive = true;

    public SearchOfferDTO setActive(boolean active) {
        isActive = active;
        return this;
    }

    public SearchOfferDTO setCategory(CategoryEnum category) {
        this.category = category;
        return this;
    }

    public SearchOfferDTO setName(String name) {
        this.name = name;
        return this;
    }

    public SearchOfferDTO setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
        return this;
    }

    public SearchOfferDTO setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
        return this;
    }

    public boolean isEmpty() {
        return (name == null || name.isEmpty()) &&
                minPrice == null &&
                maxPrice == null;
    }

    @Override
    public String toString() {
        return "SearchOfferDTO{" +
                "name='" + name + '\'' +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                '}';
    }


}