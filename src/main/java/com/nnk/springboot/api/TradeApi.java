package com.nnk.springboot.api;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.TradeNotFoundException;
import com.nnk.springboot.services.TradeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD Api controller for {@link com.nnk.springboot.domain.Trade} entities
 *
 */
@RestController
@RequestMapping("/api/trade")
public class TradeApi {
    private final TradeService tradeService;

    public TradeApi(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @PostMapping("")
    public ResponseEntity<Trade> createTrade(@RequestBody @Valid Trade trade) {
        return new ResponseEntity<>(tradeService.add(trade), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Trade>> getTrades() {
        return new ResponseEntity<>(tradeService.getAll(), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Trade> getTrade(@RequestParam Integer id) throws TradeNotFoundException {
        return new ResponseEntity<>(tradeService.getById(id), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<Trade> updateTrade(@RequestBody @Valid Trade trade) {
        return new ResponseEntity<>(tradeService.update(trade), HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteTrade(@RequestParam Integer id) {
        tradeService.deleteById(id);
        return new ResponseEntity<>("Trade deleted !", HttpStatus.OK);
    }

}
