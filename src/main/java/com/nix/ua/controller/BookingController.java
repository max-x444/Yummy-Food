package com.nix.ua.controller;

import com.nix.ua.dto.BookingDTO;
import com.nix.ua.model.Booking;
import com.nix.ua.model.User;
import com.nix.ua.model.enums.Status;
import com.nix.ua.service.BookingService;
import com.nix.ua.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/booking")
public class BookingController {
    private static final Random RANDOM = new Random();
    private static final int MAX_COOKING_TIME_IN_MINUTES = 3;
    private static final int MIN_COOKING_TIME_IN_MINUTES = 1;
    private final BookingService bookingService;
    private final PageService<Booking> pageService;

    @Autowired
    public BookingController(BookingService bookingService, PageService<Booking> pageService) {
        this.bookingService = bookingService;
        this.pageService = pageService;
    }

    @PutMapping("/update")
    public ModelAndView update(@AuthenticationPrincipal User user, ModelAndView modelAndView) {
        final Optional<Booking> optionalBooking =
                bookingService.findBookingByStatusAndUserUsername(Status.PENDING, user.getUsername());
        if (optionalBooking.isPresent()) {
            final Booking booking = optionalBooking.get();
            booking.setStatus(Status.ACCEPTED);
            booking.setCreated(LocalDateTime.now());
            booking.setDishesPreparationTime(LocalDateTime.now().plusMinutes(
                    RANDOM.nextInt(MAX_COOKING_TIME_IN_MINUTES) + MIN_COOKING_TIME_IN_MINUTES));
            booking.setTotalAmount(bookingService.getTotalAmount(booking.getUser().getId()));
            booking.setTotalPrice(bookingService.getTotalPrice(booking.getUser().getId()));
            bookingService.update(booking);
        }
        modelAndView.setViewName("redirect:/api/booking/get-all");
        return modelAndView;
    }

    @GetMapping("/get-all")
    public ModelAndView getAll(@AuthenticationPrincipal User user, ModelAndView modelAndView) {
        final List<BookingDTO> bookings = bookingService.getAllAcceptedBookingsByUserId(user.getId());
        modelAndView.addObject("bookings", bookings);
        modelAndView.setViewName("booking/booking");
        return modelAndView;
    }

    @GetMapping("/get-all/admin")
    public ModelAndView getAll(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size,
                               @RequestParam("sort") Optional<String> sort, @RequestParam("filter") Optional<String> filter,
                               @RequestParam("direction") Optional<Boolean> direction, ModelAndView modelAndView) {
        final String pageSort = sort.orElse("total_price");
        final String pageFilter = filter.orElse("");
        final boolean pageDirection = direction.orElse(true);
        modelAndView.addObject("sort", pageSort);
        modelAndView.addObject("filter", pageFilter);
        modelAndView.addObject("direction", pageDirection);
        modelAndView.setViewName("booking/bookingAdmin");
        return pageService.getPaginated(page, size, pageSort, pageFilter, pageDirection, modelAndView);
    }
}