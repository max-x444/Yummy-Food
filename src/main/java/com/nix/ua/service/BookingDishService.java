package com.nix.ua.service;

import com.nix.ua.model.Booking;
import com.nix.ua.model.BookingDish;
import com.nix.ua.model.Dish;
import com.nix.ua.model.User;
import com.nix.ua.model.enums.Status;
import com.nix.ua.repository.BookingDishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingDishService extends MainService<BookingDish> {
    private final BookingDishRepository bookingDishRepository;
    private final BookingService bookingService;

    @Autowired
    private BookingDishService(BookingDishRepository bookingDishRepository, BookingService bookingService) {
        super(bookingDishRepository);
        this.bookingDishRepository = bookingDishRepository;
        this.bookingService = bookingService;
    }

    @Override
    public BookingDish create(BookingDish item) {
        return bookingDishRepository.save(BookingDish.builder()
                .amount(item.getAmount())
                .booking(item.getBooking())
                .dish(item.getDish())
                .build());
    }

    @Override
    public BookingDish update(BookingDish item) {
        if (findById(item.getId()).isPresent()) {
            return bookingDishRepository.save(BookingDish.builder()
                    .id(item.getId())
                    .amount(item.getAmount())
                    .booking(item.getBooking())
                    .dish(item.getDish())
                    .build());
        } else {
            return null;
        }
    }

    public List<BookingDish> getAllByBookingStatusAndBookingUserUsername(Status status, String username) {
        return bookingDishRepository.getAllByBooking_StatusAndBooking_User_UsernameOrderById(status, username);
    }

    public void create(User user, Dish dish, int amount) {
        final List<BookingDish> bookingDishList =
                getAllByBookingStatusAndBookingUserUsername(Status.PENDING, user.getUsername());
        final List<BookingDish> filterBookingDishList = bookingDishList.stream()
                .filter(x -> x.getDish().getId().equals(dish.getId()) && x.getBooking().getStatus() == Status.PENDING)
                .collect(Collectors.toList());
        final Booking booking = getBooking(bookingDishList, user);
        if (filterBookingDishList.isEmpty()) {
            create(BookingDish.builder()
                    .booking(booking)
                    .dish(dish)
                    .amount(amount)
                    .build());
        } else {
            final BookingDish updateBookingDish = filterBookingDishList.get(0);
            updateBookingDish.setAmount(updateBookingDish.getAmount() + amount);
            update(updateBookingDish);
        }
    }

    public void clearBookings(String username) {
        final List<BookingDish> pendingBookings = getAllByBookingStatusAndBookingUserUsername(Status.PENDING, username);
        if (pendingBookings.isEmpty()) {
            bookingService.clear(username);
        }
    }

    private Booking getBooking(List<BookingDish> bookingDishList, User user) {
        boolean checkPending = false;
        for (BookingDish bookingDish : bookingDishList) {
            if (bookingDish.getBooking().getStatus() == Status.PENDING) {
                checkPending = true;
                break;
            }
        }
        if (!checkPending) {
            return Booking.builder()
                    .status(Status.PENDING)
                    .user(user)
                    .build();
        } else {
            return bookingDishList.get(0).getBooking();
        }
    }
}