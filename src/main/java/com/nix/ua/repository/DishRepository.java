package com.nix.ua.repository;

import com.nix.ua.model.Dish;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends CrudRepository<Dish, String> {
    Iterable<Dish> getAllByDishCategory_Id(String id);
}