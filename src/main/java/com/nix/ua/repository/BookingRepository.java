package com.nix.ua.repository;

import com.nix.ua.dto.BookingDTO;
import com.nix.ua.model.Booking;
import com.nix.ua.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends PagingAndSortingRepository<Booking, String> {
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
    List<BookingDTO> getAllAcceptedBookingsByUserId(@Param("user_id") String userId);

    List<Booking> getAllByStatusAndUser_Username(Status status, String username);

    List<Booking> getAllByStatus(Status status);

    @Query(value = """
            SELECT * FROM booking
            WHERE NOT status = 'PENDING' AND (UPPER(booking_id) LIKE UPPER(CONCAT('%', :filter, '%'))
            OR UPPER(CAST(created AS varchar)) LIKE UPPER(CONCAT('%', :filter, '%'))
            OR UPPER(CAST(status AS varchar)) LIKE UPPER(CONCAT('%', :filter, '%'))
            OR UPPER(CAST(total_amount AS varchar)) LIKE UPPER(CONCAT('%', :filter, '%'))
            OR UPPER(CAST(total_price AS varchar)) LIKE UPPER(CONCAT('%', :filter, '%'))
            OR UPPER(user_id) LIKE UPPER(CONCAT('%', :filter, '%')))""", nativeQuery = true)
    Page<Booking> findAllBySortedAndFiltered(@Param("filter") String filter, PageRequest pageRequest);
}