package com.nnk.springboot.api;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exceptions.RuleNameNotFoundException;
import com.nnk.springboot.services.RuleNameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD Api controller for {@link com.nnk.springboot.domain.RuleName} entities
 *
 */
@RestController
@RequestMapping("/api/ruleName")
public class RuleNameApi {

    private final RuleNameService ruleNameService;

    public RuleNameApi(RuleNameService ruleNameService) {
        this.ruleNameService = ruleNameService;
    }

    @PostMapping("")
    public ResponseEntity<RuleName> createRuleName(@RequestBody @Valid RuleName ruleName) {
        return new ResponseEntity<>(ruleNameService.add(ruleName), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<RuleName>> getRuleNames() {
        return new ResponseEntity<>(ruleNameService.getAll(), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<RuleName> getRuleName(@RequestParam Integer id) throws RuleNameNotFoundException {
        return new ResponseEntity<>(ruleNameService.getById(id), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<RuleName> updateRuleName(@RequestBody @Valid RuleName ruleName) {
        return new ResponseEntity<>(ruleNameService.update(ruleName), HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteRuleName(@RequestParam Integer id) {
        ruleNameService.deleteById(id);
        return new ResponseEntity<>("RuleName deleted !", HttpStatus.OK);
    }

}
