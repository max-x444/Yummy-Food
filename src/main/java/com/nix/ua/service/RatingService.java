package com.nix.ua.service;

import com.nix.ua.model.Rating;
import com.nix.ua.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService extends MainService<Rating> {
    private final RatingRepository ratingRepository;

    @Autowired
    private RatingService(RatingRepository ratingRepository) {
        super(ratingRepository);
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Rating create(Rating item) {
        return ratingRepository.save(Rating.builder()
                .comment(item.getComment())
                .date(item.getDate())
                .grade(item.getGrade())
                .user(item.getUser())
                .dish(item.getDish())
                .build());
    }

    @Override
    public Rating update(Rating item) {
        if (findById(item.getId()).isPresent()) {
            return ratingRepository.save(Rating.builder()
                    .id(item.getId())
                    .comment(item.getComment())
                    .date(item.getDate())
                    .grade(item.getGrade())
                    .user(item.getUser())
                    .dish(item.getDish())
                    .build());
        } else {
            return null;
        }
    }
}