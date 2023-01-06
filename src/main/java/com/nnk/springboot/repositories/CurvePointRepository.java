package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.CurvePoint;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Basic CRUD repository for {@link com.nnk.springboot.domain.CurvePoint} entities
 */
public interface CurvePointRepository extends JpaRepository<CurvePoint, Integer> {

}
