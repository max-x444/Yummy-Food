package com.nix.ua.repository;

import com.nix.ua.dto.BookingDTO;
import com.nix.ua.model.Booking;
import com.nix.ua.model.enums.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends CrudRepository<Booking, String> {
    Optional<Booking> findBookingByStatusAndUser_Username(Status status, String username);

    @Query(value = """
            SELECT SUM(amount) FROM booking_dish
            FULL JOIN booking USING(booking_id)
            FULL JOIN dish USING(dish_id)
            WHERE user_id = :user_id AND status = 'PENDING'""", nativeQuery = true)
    int getTotalAmount(@Param("user_id") String userId);

    @Query(value = """
            SELECT SUM(price * amount) FROM booking_dish
            FULL JOIN booking USING(booking_id)
            FULL JOIN dish USING(dish_id)
            WHERE user_id = :user_id AND status = 'PENDING'""", nativeQuery = true)
    Double getTotalPrice(@Param("user_id") String userId);

    @Query(value = """
            SELECT new com.nix.ua.dto.BookingDTO(d.image, b.id, b.created, b.dishesPreparationTime,
             d.name, b.status, b.totalPrice, b.totalAmount) FROM BookingDish bd
            JOIN Booking b ON bd.booking.id = b.id
            JOIN Dish d ON bd.dish.id = d.id
            WHERE b.user.id = :user_id AND (b.status = 'ACCEPTED' OR b.status = 'READY')""")
    List<BookingDTO> getAllAcceptedBookings(@Param("user_id") String userId);

    Iterable<Booking> getAllByStatusNot(Status status);

    List<Booking> getAllByStatusAndUser_Username(Status status, String username);

    List<Booking> getAllByStatus(Status status);
}