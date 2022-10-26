package com.nix.ua.controller;

import com.nix.ua.model.Dish;
import com.nix.ua.service.DishService;
import com.nix.ua.service.PageService;
import com.nix.ua.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@RestController
@RequestMapping("/api/dish")
public class DishController {
    private final DishService dishService;
    private final PageService<Dish> pageService;

    @Autowired
    public DishController(DishService dishService, PageService<Dish> pageService) {
        this.dishService = dishService;
        this.pageService = pageService;
    }

    @GetMapping("/find-by-id/{id}")
    public ModelAndView findById(@PathVariable("id") String id, ModelAndView modelAndView) {
        final Dish dish = dishService.findById(id).orElse(null);
        modelAndView.addObject("dish", dish);
        modelAndView.setViewName("dish/fullDish");
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView create(Dish dish, @RequestParam("file") MultipartFile file, int size, ModelAndView modelAndView) {
        final Optional<String> optionalImage = ImageUtil.saveImage(file);
        optionalImage.ifPresent(dish::setImage);
        dishService.create(dish);
        modelAndView.setViewName("redirect:/api/dish/get-all/admin?size=" + size + "&page=1");
        return modelAndView;
    }

    @PutMapping("/update")
    public ModelAndView update(Dish dish, @RequestParam("file") MultipartFile file, int size, ModelAndView modelAndView) {
        final Optional<String> optionalImage = ImageUtil.saveImage(file);
        optionalImage.ifPresent(dish::setImage);
        dishService.update(dish);
        modelAndView.setViewName("redirect:/api/dish/get-all/admin?size=" + size + "&page=1");
        return modelAndView;
    }

    @DeleteMapping("/delete")
    public ModelAndView delete(String id, int size, ModelAndView modelAndView) {
        dishService.delete(id);
        modelAndView.setViewName("redirect:/api/dish/get-all/admin?size=" + size + "&page=1");
        return modelAndView;
    }

    @GetMapping("/get-all/{name}")
    public ModelAndView getAllDishByCategoryName(@PathVariable("name") String name, ModelAndView modelAndView) {
        final Iterable<Dish> dishes = dishService.getAllDishByCategoryName(name);
        modelAndView.addObject("dishes", dishes);
        modelAndView.setViewName("dish/dish");
        return modelAndView;
    }

    @GetMapping("/get-all/admin")
    public ModelAndView getAllDishByCategoryName(@RequestParam("page") Optional<Integer> page,
                                                 @RequestParam("size") Optional<Integer> size, ModelAndView modelAndView) {
        return pageService.getPaginated(page, size, dishService.getAll(), "dish/dishAdmin", modelAndView);
    }
}