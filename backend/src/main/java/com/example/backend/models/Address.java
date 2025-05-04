package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @Column(name = "address_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;


    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    @JsonBackReference  // ✅ Ngăn vòng lặp khi serialize JSON từ Address -> User
    private User user;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String ward;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String city;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    @Column(name = "houseNumber", nullable = false)
    private String houseNumber;
}
