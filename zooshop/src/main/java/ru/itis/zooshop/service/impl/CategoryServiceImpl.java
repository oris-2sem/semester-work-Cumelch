package ru.itis.zooshop.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.itis.zooshop.model.entity.CategoryEntity;
import ru.itis.zooshop.model.enums.CategoryEnum;
import ru.itis.zooshop.model.view.CategoryView;
import ru.itis.zooshop.repository.CategoryRepository;
import ru.itis.zooshop.service.CategoryService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    public List<CategoryView> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(c->mapper.map(c, CategoryView.class))
                .collect(Collectors.toList());
    }

    public Optional<CategoryEntity> findByName(CategoryEnum categoryEnum) {
        return this.categoryRepository.findByName(categoryEnum);
    }

    public CategoryRepository getRepository() {
        return this.categoryRepository;
    }
}
