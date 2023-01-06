package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exceptions.RuleNameNotFoundException;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CRUD methods for {@link com.nnk.springboot.domain.RuleName} entities
 */
@Service
public class RuleNameService {

    private final RuleNameRepository ruleNameRepository;

    public RuleNameService(RuleNameRepository ruleNameRepository) {
        this.ruleNameRepository = ruleNameRepository;
    }

    public RuleName add(RuleName ruleName) {
        return ruleNameRepository.save(ruleName);
    }

    public RuleName update(RuleName ruleName) {
        return ruleNameRepository.save(ruleName);
    }

    public RuleName getById(int id) throws RuleNameNotFoundException {
        return ruleNameRepository.findById(id).orElseThrow(RuleNameNotFoundException::new);
    }
    public List<RuleName> getAll() {
        return ruleNameRepository.findAll();
    }

    public void deleteById(int id) {
        ruleNameRepository.deleteById(id);
    }

}
