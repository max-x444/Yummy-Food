package com.nix.ua.service;

import com.nix.ua.dto.BookingDTO;
import com.nix.ua.model.Booking;
import com.nix.ua.model.enums.Status;
import com.nix.ua.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public Iterable<Booking> search(Status status, Optional<String> stringOptional) {
        final List<Booking> collect = StreamSupport.stream(bookingRepository.getAllByStatusNot(status).spliterator(), false)
                .collect(Collectors.toList());
        final List<Booking> filteredList = new ArrayList<>();
        if (stringOptional.isPresent()) {
            final String filter = stringOptional.get();
            for (Booking booking : collect) {
                if (booking.getStatus().name().contains(filter)) {
                    filteredList.add(booking);
                    continue;
                }
                if (booking.getUser().getId().contains(filter)) {
                    filteredList.add(booking);
                    continue;
                }
                if (booking.getCreated().toString().contains(filter)) {
                    filteredList.add(booking);
                    continue;
                }
                if (booking.getId().contains(filter)) {
                    filteredList.add(booking);
                    continue;
                }
                if (String.valueOf(booking.getTotalAmount()).contains(filter)) {
                    filteredList.add(booking);
                    continue;
                }
                if (String.valueOf(booking.getTotalPrice()).contains(filter)) {
                    filteredList.add(booking);
                    continue;
                }
//            if (booking.getDishesPreparationTime().toString().contains(filter)) {
//                filteredList.add(booking);
//                break;
//            }
            }
        }
        return filteredList;
    }
}