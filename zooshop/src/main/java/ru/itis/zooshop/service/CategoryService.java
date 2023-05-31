package ru.itis.zooshop.service;


import ru.itis.zooshop.model.entity.CategoryEntity;
import ru.itis.zooshop.model.enums.CategoryEnum;
import ru.itis.zooshop.model.view.CategoryView;
import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<CategoryView> getAllCategories();
    public Optional<CategoryEntity> findByName(CategoryEnum categoryEnum);
}
