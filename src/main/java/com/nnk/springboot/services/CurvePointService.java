package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.CurvePointNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CRUD methods for {@link com.nnk.springboot.domain.CurvePoint} entities
 */
@Service
public class CurvePointService {

    private final CurvePointRepository curvePointRepository;

    public CurvePointService(CurvePointRepository curvePointRepository) {
        this.curvePointRepository = curvePointRepository;
    }

    public CurvePoint add(CurvePoint curvePoint) {
        return curvePointRepository.save(curvePoint);
    }

    public CurvePoint update(CurvePoint curvePoint) {
        return curvePointRepository.save(curvePoint);
    }

    public CurvePoint getById(int id) throws CurvePointNotFoundException {
        return curvePointRepository.findById(id).orElseThrow(CurvePointNotFoundException::new);
    }
    public List<CurvePoint> getAll() {
        return curvePointRepository.findAll();
    }

    public void deleteById(int id) {
        curvePointRepository.deleteById(id);
    }

}
