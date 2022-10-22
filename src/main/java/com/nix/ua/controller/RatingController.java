package com.nix.ua.controller;

import com.nix.ua.model.Rating;
import com.nix.ua.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rating")
public class RatingController implements CrudController<Rating> {
    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    public Rating findById(String id) {
        return ratingService.findById(id).orElse(null);
    }

    @Override
    public Iterable<Rating> getAll() {
        return ratingService.getAll();
    }

    @Override
    public Rating create(Rating item) {
        return ratingService.create(item);
    }

    @Override
    public Rating update(Rating item) {
        return ratingService.update(item);
    }

    @Override
    public void delete(String id) {
        ratingService.delete(id);
    }
}