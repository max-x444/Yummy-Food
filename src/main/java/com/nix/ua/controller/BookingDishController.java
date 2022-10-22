package com.nix.ua.controller;

import com.nix.ua.model.BookingDish;
import com.nix.ua.model.Dish;
import com.nix.ua.model.User;
import com.nix.ua.model.enums.Status;
import com.nix.ua.service.BookingDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/booking-dish")
public class BookingDishController {
    private final BookingDishService bookingDishService;

    @Autowired
    public BookingDishController(BookingDishService bookingDishService) {
        this.bookingDishService = bookingDishService;
    }

    @PutMapping("/update")
    public ModelAndView update(String id, int amount, ModelAndView modelAndView) {
        final Optional<BookingDish> optionalBookingDish = bookingDishService.findById(id);
        if (optionalBookingDish.isPresent()) {
            final BookingDish bookingDish = optionalBookingDish.get();
            bookingDish.setAmount(amount);
            bookingDishService.update(bookingDish);
        }
        modelAndView.setViewName("redirect:/api/booking-dish/get-all");
        return modelAndView;
    }

    @DeleteMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable String id, ModelAndView modelAndView) {
        bookingDishService.delete(id);
        modelAndView.setViewName("redirect:/api/booking-dish/get-all");
        return modelAndView;
    }

    @DeleteMapping("/clear")
    public ModelAndView clear(@AuthenticationPrincipal User user, ModelAndView modelAndView) {
        final List<BookingDish> bookingDishes =
                bookingDishService.getAllByBookingStatusAndBookingUserUsername(Status.PENDING, user.getUsername());
        bookingDishes.stream()
                .map(BookingDish::getId)
                .forEach(bookingDishService::delete);
        modelAndView.setViewName("redirect:/api/booking-dish/get-all");
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView create(@AuthenticationPrincipal User user, @ModelAttribute Dish dish, ModelAndView modelAndView, int amount) {
        bookingDishService.create(user, dish, amount);
        modelAndView.setViewName("redirect:/api/booking-dish/get-all");
        return modelAndView;
    }

    @GetMapping("/get-all")
    public ModelAndView getAll(@AuthenticationPrincipal User user, ModelAndView modelAndView) {
        if (user == null) {
            modelAndView.setViewName("redirect:/api/home");
            return modelAndView;
        }
        final List<BookingDish> bookingDishes =
                bookingDishService.getAllByBookingStatusAndBookingUserUsername(Status.PENDING, user.getUsername());
        modelAndView.addObject("bookingDishes", bookingDishes);
        modelAndView.setViewName("basket/basket");
        return modelAndView;
    }
}