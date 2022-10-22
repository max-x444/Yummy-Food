package com.nix.ua.dto;

import com.nix.ua.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private String image;
    private String id;
    private LocalDateTime created;
    private LocalDateTime dishesPreparation;
    private String dish;
    private Status status;
    private Double totalPrice;
    private int totalAmount;
}