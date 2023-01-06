package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Basic CRUD repository for {@link com.nnk.springboot.domain.Trade} entities
 */
public interface TradeRepository extends JpaRepository<Trade, Integer> {
}
