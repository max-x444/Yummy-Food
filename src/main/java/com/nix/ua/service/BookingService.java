package com.nix.ua.service;

import com.nix.ua.dto.BookingDTO;
import com.nix.ua.model.Booking;
import com.nix.ua.model.enums.Status;
import com.nix.ua.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@EnableScheduling
public class BookingService extends MainService<Booking> {
    private static final int ONE_SECOND = 1000;
    private static final int ONE_MINUTE = ONE_SECOND * 60;
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

    public List<Booking> getAllByStatusAndUserUsername(Status status, String username) {
        return bookingRepository.getAllByStatusAndUser_Username(status, username);
    }

    public List<BookingDTO> getAllAcceptedBookingsByUserId(String userId) {
        return bookingRepository.getAllAcceptedBookingsByUserId(userId);
    }

    public List<Booking> getAllByStatus(Status status) {
        return bookingRepository.getAllByStatus(status);
    }

    public Page<Booking> findAllBySortedAndFiltered(@Param("filter") String filter, PageRequest pageRequest) {
        return bookingRepository.findAllBySortedAndFiltered(filter, pageRequest);
    }

    public void clear(String username) {
        final List<Booking> pendingBookings = getAllByStatusAndUserUsername(Status.PENDING, username);
        bookingRepository.deleteAll(pendingBookings);
    }

    @Scheduled(fixedRate = ONE_MINUTE)
    private void checkPreparationDishesTime() {
        final List<Booking> bookings = getAllByStatus(Status.ACCEPTED);
        bookings.stream()
                .filter(x -> x.getDishesPreparationTime() != null && LocalDateTime.now().isAfter(x.getDishesPreparationTime()))
                .forEach(x -> {
                    x.setStatus(Status.READY);
                    update(x);
                });
    }
}