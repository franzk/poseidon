package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.RuleName;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Basic CRUD repository for {@link com.nnk.springboot.domain.RuleName} entities
 */
public interface RuleNameRepository extends JpaRepository<RuleName, Integer> {
}
