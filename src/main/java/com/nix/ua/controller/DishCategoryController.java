package com.nix.ua.controller;

import com.nix.ua.model.DishCategory;
import com.nix.ua.service.DishCategoryService;
import com.nix.ua.service.PageService;
import com.nix.ua.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@RestController
@RequestMapping("/api/dish-category")
public class DishCategoryController {
    private final DishCategoryService dishCategoryService;
    private final PageService<DishCategory> pageService;

    @Autowired
    public DishCategoryController(PageService<DishCategory> pageService, DishCategoryService dishCategoryService) {
        this.pageService = pageService;
        this.dishCategoryService = dishCategoryService;
    }

    @PostMapping("/create/postman")
    public DishCategory create(@RequestBody DishCategory dishCategory) {
        return dishCategoryService.create(dishCategory);
    }

    @PostMapping("/create")
    public ModelAndView create(DishCategory dishCategory, @RequestParam("file") MultipartFile file,
                               int size, ModelAndView modelAndView) {
        final Optional<String> optionalImage = ImageUtil.saveImage(file);
        optionalImage.ifPresent(dishCategory::setImage);
        dishCategoryService.create(dishCategory);
        modelAndView.setViewName("redirect:/api/dish-category/get-all/admin?size=" + size + "&page=1");
        return modelAndView;
    }

    @PutMapping("/update")
    public ModelAndView update(DishCategory dishCategory, @RequestParam("file") MultipartFile file,
                               int size, ModelAndView modelAndView) {
        final Optional<String> optionalImage = ImageUtil.saveImage(file);
        optionalImage.ifPresent(dishCategory::setImage);
        dishCategoryService.update(dishCategory);
        modelAndView.setViewName("redirect:/api/dish-category/get-all/admin?size=" + size + "&page=1");
        return modelAndView;
    }

    @DeleteMapping("/delete")
    public ModelAndView delete(String id, int size, ModelAndView modelAndView) {
        dishCategoryService.delete(id);
        modelAndView.setViewName("redirect:/api/dish-category/get-all/admin?size=" + size + "&page=1");
        return modelAndView;
    }

    @GetMapping("/get-all")
    public ModelAndView getAll(ModelAndView modelAndView) {
        final Iterable<DishCategory> dishCategories = dishCategoryService.getAll();
        modelAndView.addObject("dishCategories", dishCategories);
        modelAndView.setViewName("category/category");
        return modelAndView;
    }

    @GetMapping("/get-all/admin")
    public ModelAndView getAll(@RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size, ModelAndView modelAndView) {
        return pageService.getPaginated(page, size, dishCategoryService.getAll(), "category/categoryAdmin", modelAndView);
    }
}