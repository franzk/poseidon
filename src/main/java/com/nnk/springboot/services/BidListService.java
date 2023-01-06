package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.BidListNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CRUD methods for {@link com.nnk.springboot.domain.BidList} entities
 */
@Service
public class BidListService {

    private final BidListRepository bidListRepository;

    public BidListService(BidListRepository bidListRepository) {
        this.bidListRepository = bidListRepository;
    }

    public BidList add(BidList bidList) {
        return bidListRepository.save(bidList);
    }

    public BidList update(BidList bidList) {
        return bidListRepository.save(bidList);
    }

    public List<BidList> getAll() {
        return bidListRepository.findAll();
    }

    public BidList getById(int id) throws BidListNotFoundException {
       return bidListRepository.findById(id).orElseThrow(BidListNotFoundException::new);
    }

    public void deleteById(int id) {
        bidListRepository.deleteById(id);
    }
}
