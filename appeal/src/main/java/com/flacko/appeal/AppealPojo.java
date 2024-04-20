package com.flacko.appeal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "appeals")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AppealPojo implements Appeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_key", nullable = false)
    private Long primaryKey;

    @Column(nullable = false)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppealStatus appealStatus;

    @Column(name = "payment_id", nullable = false)
    private String paymentId;

}