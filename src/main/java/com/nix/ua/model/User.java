package com.nix.ua.model;

import com.nix.ua.model.enums.Role;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Data
@Entity(name = "usr")
@Builder
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "user_id")
    private String id;

    private String name;
    private String surname;

    @Column(unique = true)
    @Size(min = 10, max = 30)
    private String email;

    @Column(unique = true)
    @Size(min = 5, max = 20)
    private String username;

    @Size(min = 5, max = 20)
    private String password;

    @Size(min = 10, max = 20)
    private String phone;

    @Column(length = 10_000_000)
    private String avatar;

    @Enumerated(value = EnumType.STRING)
    private Role role;
}