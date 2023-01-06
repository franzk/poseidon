package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Basic CRUD repository for {@link com.nnk.springboot.domain.Rating} entities
 */
public interface RatingRepository extends JpaRepository<Rating, Integer> {

}
