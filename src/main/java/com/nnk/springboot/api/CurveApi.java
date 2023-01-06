package com.nnk.springboot.api;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.CurvePointNotFoundException;
import com.nnk.springboot.services.CurvePointService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD Api controller for {@link com.nnk.springboot.domain.CurvePoint} entities
 *
 */
@RestController
@RequestMapping("/api/curvePoint")
public class CurveApi {

    private final CurvePointService curvePointService;

    public CurveApi(CurvePointService curvePointService) {
        this.curvePointService = curvePointService;
    }

    @PostMapping("")
    public ResponseEntity<CurvePoint> createCurvePoint(@RequestBody @Valid CurvePoint curvePoint) {
        return new ResponseEntity<>(curvePointService.add(curvePoint), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CurvePoint>> getCurvePoints() {
        return new ResponseEntity<>(curvePointService.getAll(), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<CurvePoint> getCurvePoint(@RequestParam Integer id) throws CurvePointNotFoundException {
        return new ResponseEntity<>(curvePointService.getById(id), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<CurvePoint> updateCurvePoint(@RequestBody @Valid CurvePoint curvePoint) {
        return new ResponseEntity<>(curvePointService.update(curvePoint), HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteCurvePoint(@RequestParam Integer id) {
        curvePointService.deleteById(id);
        return new ResponseEntity<>("CurvePoint deleted !", HttpStatus.OK);
    }

}
