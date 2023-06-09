package ru.itis.zooshop.model.view;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@RequiredArgsConstructor
public class OfferDetailsView {
    private Long id;
    private String title;
    private BigDecimal price;
    private String description;
    private List<String> imagesUrls;
    private LocalDate createdOn;
    private String category;
    private String sellerFirstName;
    private String sellerLastName;
    private String sellerUsername;
    private String sellerEmail;
    private String sellerPhone;
    private boolean isActive;

    public String getSellerPhone() {
        return sellerPhone;
    }

    public OfferDetailsView setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
        return this;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public OfferDetailsView setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
        return this;
    }

    public boolean isActive() {
        return isActive;
    }

    public OfferDetailsView setActive(boolean active) {
        isActive = active;
        return this;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public OfferDetailsView setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
        return this;
    }

    public String getSellerFullName() {
        return sellerFirstName.concat(" ").concat(sellerLastName);
    }

    public String getDescription() {
        return description;
    }

    public OfferDetailsView setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<String> getImagesUrls() {
        return imagesUrls;
    }

    public OfferDetailsView setImagesUrls(List<String> imagesUrls) {
        this.imagesUrls = imagesUrls;
        return this;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public OfferDetailsView setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public OfferDetailsView setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getSellerFirstName() {
        return sellerFirstName;
    }

    public OfferDetailsView setSellerFirstName(String sellerFirstName) {
        this.sellerFirstName = sellerFirstName;
        return this;
    }

    public String getSellerLastName() {
        return sellerLastName;
    }

    public OfferDetailsView setSellerLastName(String sellerLastName) {
        this.sellerLastName = sellerLastName;
        return this;
    }

    public Long getId() {
        return id;
    }

    public OfferDetailsView setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public OfferDetailsView setTitle(String title) {
        this.title = title;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OfferDetailsView setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
