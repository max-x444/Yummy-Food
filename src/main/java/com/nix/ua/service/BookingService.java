package com.nix.ua.service;

import com.nix.ua.dto.BookingDTO;
import com.nix.ua.model.Booking;
import com.nix.ua.model.enums.Status;
import com.nix.ua.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService extends MainService<Booking> {
    private final BookingRepository bookingRepository;

    @Autowired
    private BookingService(BookingRepository bookingRepository) {
        super(bookingRepository);
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking create(Booking item) {
        return bookingRepository.save(Booking.builder()
                .status(item.getStatus())
                .created(item.getCreated())
                .dishesPreparationTime(item.getDishesPreparationTime())
                .user(item.getUser())
                .totalAmount(item.getTotalAmount())
                .totalPrice(item.getTotalPrice())
                .build());
    }

    @Override
    public Booking update(Booking item) {
        if (findById(item.getId()).isPresent()) {
            return bookingRepository.save(Booking.builder()
                    .id(item.getId())
                    .status(item.getStatus())
                    .created(item.getCreated())
                    .dishesPreparationTime(item.getDishesPreparationTime())
                    .user(item.getUser())
                    .totalAmount(item.getTotalAmount())
                    .totalPrice(item.getTotalPrice())
                    .build());
        } else {
            return null;
        }
    }

    public Optional<Booking> findBookingByStatusAndUserUsername(Status status, String username) {
        return bookingRepository.findBookingByStatusAndUser_Username(status, username);
    }

    public int getTotalAmount(String userId) {
        return bookingRepository.getTotalAmount(userId);
    }

    public Double getTotalPrice(String userId) {
        return bookingRepository.getTotalPrice(userId);
    }

    public List<BookingDTO> getAllAcceptedBookings(String userId) {
        return bookingRepository.getAllAcceptedBookings(userId);
    }

    public Iterable<Booking> getAllAcceptedAndReadyBookings(Status status) {
        return bookingRepository.getAllByStatusNot(status);
    }
}