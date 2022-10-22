package com.nix.ua.service;

import com.nix.ua.model.Dish;
import com.nix.ua.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DishService extends MainService<Dish> {
    private final DishRepository dishRepository;

    @Autowired
    private DishService(DishRepository dishRepository) {
        super(dishRepository);
        this.dishRepository = dishRepository;
    }

    @Override
    public Dish create(Dish item) {
        return dishRepository.save(Dish.builder()
                .name(item.getName())
                .image(item.getImage())
                .description(item.getDescription())
                .weight(item.getWeight())
                .calories(item.getCalories())
                .price(item.getPrice())
                .dishCategory(item.getDishCategory())
                .build());
    }

    @Override
    public Dish update(Dish item) {
        if (findById(item.getId()).isPresent()) {
            return dishRepository.save(Dish.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .image(item.getImage())
                    .description(item.getDescription())
                    .weight(item.getWeight())
                    .calories(item.getCalories())
                    .price(item.getPrice())
                    .dishCategory(item.getDishCategory())
                    .build());
        } else {
            return null;
        }
    }

    public Iterable<Dish> getAllDishByCategoryName(String name) {
        return dishRepository.getAllByDishCategory_Name(name);
    }
}