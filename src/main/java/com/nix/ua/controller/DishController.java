package com.nix.ua.controller;

import com.nix.ua.model.Dish;
import com.nix.ua.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/dish")
public class DishController {
    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping("/create")
    public Dish create(@RequestBody @Valid Dish item) {
        return dishService.create(item);
    }

    @GetMapping("/find-by-id/{id}")
    public ModelAndView findById(@PathVariable("id") String id, ModelAndView modelAndView) {
        final Dish dish = dishService.findById(id).orElse(null);
        modelAndView.addObject("dish", dish);
        modelAndView.setViewName("dish/fullDish");
        return modelAndView;
    }

    @GetMapping("/get-all/{name}")
    public ModelAndView getAllDishByCategoryName(@PathVariable("name") String name, ModelAndView modelAndView) {
        final Iterable<Dish> dishes = dishService.getAllDishByCategoryName(name);
        modelAndView.addObject("dishes", dishes);
        modelAndView.setViewName("dish/dish");
        return modelAndView;
    }
}