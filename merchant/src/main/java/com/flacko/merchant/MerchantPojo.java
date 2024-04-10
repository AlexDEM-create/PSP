package com.flacko.merchant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity(name = "merchants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "userid", nullable = false)
    private String userid;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate = Instant.now();

    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate = createdDate;
}

    //TODO merchantid --> id, primary key , userId

}
