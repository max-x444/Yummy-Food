package com.nix.ua.service;

import com.nix.ua.model.DishCategory;
import com.nix.ua.repository.DishCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DishCategoryService extends MainService<DishCategory> {
    private final DishCategoryRepository dishCategoryRepository;

    @Autowired
    private DishCategoryService(DishCategoryRepository dishCategoryRepository) {
        super(dishCategoryRepository);
        this.dishCategoryRepository = dishCategoryRepository;
    }

    @Override
    public DishCategory create(DishCategory item) {
        return dishCategoryRepository.save(DishCategory.builder()
                .name(item.getName())
                .image(item.getImage())
                .build());
    }

    @Override
    public DishCategory update(DishCategory item) {
        if (findById(item.getId()).isPresent()) {
            return dishCategoryRepository.save(DishCategory.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .image(item.getImage())
                    .build());
        } else {
            return null;
        }
    }
}