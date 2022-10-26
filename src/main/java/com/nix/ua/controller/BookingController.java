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
    private static final int MAX_TIME = 3;
    private static final int MIN_TIME = 1;
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
            booking.setDishesPreparationTime(LocalDateTime.now().plusMinutes(RANDOM.nextInt(MAX_TIME) + MIN_TIME));
            booking.setTotalAmount(bookingService.getTotalAmount(booking.getUser().getId()));
            booking.setTotalPrice(bookingService.getTotalPrice(booking.getUser().getId()));
            bookingService.update(booking);
        }
        modelAndView.setViewName("redirect:/api/booking/get-all");
        return modelAndView;
    }

    @GetMapping("/get-all")
    public ModelAndView getAll(@AuthenticationPrincipal User user, ModelAndView modelAndView) {
        final List<BookingDTO> bookings = bookingService.getAllAcceptedBookings(user.getId());
        modelAndView.addObject("bookings", bookings);
        modelAndView.setViewName("booking/booking");
        return modelAndView;
    }

    @GetMapping("/get-all/admin")
    public ModelAndView getAll(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size,
                               ModelAndView modelAndView) {
        return pageService.getPaginated(page, size, bookingService.getAllAcceptedAndReadyBookings(Status.PENDING),
                "booking/bookingAdmin", modelAndView);
    }

    @GetMapping("/search")
    public ModelAndView search(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size,
                               @RequestParam("filter") Optional<String> filter, ModelAndView modelAndView) {
        filter.ifPresent(s -> modelAndView.addObject("filter", s));
        return pageService.getPaginated(page, size, bookingService.search(Status.PENDING, filter),
                "booking/bookingAdmin", modelAndView);
    }
}