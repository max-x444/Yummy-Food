package com.nix.ua.repository;

import com.nix.ua.model.DishCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishCategoryRepository extends CrudRepository<DishCategory, String> {
}