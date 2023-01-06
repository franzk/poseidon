package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Rating entity
 */
@Data
@Entity
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    Integer id;
    @NotBlank(message = "{rating.moodysRating.NotBlank}")
    String moodysRating;

    @NotBlank(message = "{rating.sandPRating.NotBlank}")
    String sandPRating;

    @NotBlank(message = "{rating.fitchRating.NotBlank}")
    String fitchRating;

    @NotNull(message = "{rating.orderNumber.NotNull}")
    Integer orderNumber;

}
