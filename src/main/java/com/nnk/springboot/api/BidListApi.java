package com.nnk.springboot.api;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.BidListNotFoundException;
import com.nnk.springboot.services.BidListService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD Api controller for {@link com.nnk.springboot.domain.BidList} entities
 *
 */
@RestController
@RequestMapping("/api/bidList")
public class BidListApi {

    private final BidListService bidListService;

    public BidListApi(BidListService bidListService) {
        this.bidListService = bidListService;
    }

    @PostMapping("")
    public ResponseEntity<BidList> createBidList(@RequestBody @Valid BidList bidList) {
        return new ResponseEntity<>(bidListService.add(bidList), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<BidList>> getBidLists() {
        return new ResponseEntity<>(bidListService.getAll(), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<BidList> getBidList(@RequestParam Integer id) throws BidListNotFoundException {
        return new ResponseEntity<>(bidListService.getById(id), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<BidList> updateBidList(@RequestBody @Valid BidList bidList) {
        return new ResponseEntity<>(bidListService.update(bidList), HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteBidList(@RequestParam Integer id) {
        bidListService.deleteById(id);
        return new ResponseEntity<>("BidList deleted !", HttpStatus.OK);
    }
}
