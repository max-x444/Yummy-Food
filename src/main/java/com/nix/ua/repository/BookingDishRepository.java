package com.nix.ua.repository;

import com.nix.ua.model.BookingDish;
import com.nix.ua.model.enums.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingDishRepository extends CrudRepository<BookingDish, String> {
    List<BookingDish> getAllByBooking_StatusAndBooking_User_UsernameOrderById(Status status, String username);
}