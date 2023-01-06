package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.TradeNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CRUD methods for {@link com.nnk.springboot.domain.Trade} entities
 */
@Service
public class TradeService {

    private final TradeRepository tradeRepository;

    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    public Trade add(Trade rating) {
        return tradeRepository.save(rating);
    }

    public Trade update(Trade rating) {
        return tradeRepository.save(rating);
    }

    public Trade getById(int id) throws TradeNotFoundException {
        return tradeRepository.findById(id).orElseThrow(TradeNotFoundException::new);
    }
    public List<Trade> getAll() {
        return tradeRepository.findAll();
    }

    public void deleteById(int id) {
        tradeRepository.deleteById(id);
    }

}
