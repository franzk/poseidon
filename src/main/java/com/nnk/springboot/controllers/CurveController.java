package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.CurvePointNotFoundException;
import com.nnk.springboot.services.AuthService;
import com.nnk.springboot.services.CurvePointService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

/**
 * CRUD Controller that launch ThymeLeaf renderings.
 * <br>
 * Concerns {@link com.nnk.springboot.domain.CurvePoint} entities
 */
@Log4j2
@Controller
public class CurveController {

    private final CurvePointService curvePointService;
    private final AuthService authService;

    public CurveController(CurvePointService curvePointService, AuthService authService) {
        this.curvePointService = curvePointService;
        this.authService = authService;
    }

    @RequestMapping("/curvePoint/list")
    public String home(Model model)
    {
        log.info("CurveController List");
        model.addAttribute("curvePoints", curvePointService.getAll());
        model.addAttribute("loggedUser", authService.getLoggedUser());
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(Model model) {
        log.info("CurveController addCurvePointForm");
        model.addAttribute(new CurvePoint());
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        log.info("CurveController validate");
        if (result.hasErrors()) {
            return "curvePoint/add";
        }
        curvePointService.add(curvePoint);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws CurvePointNotFoundException {
        log.info("CurveController showUpdateForm");
        model.addAttribute(curvePointService.getById(id));
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                             BindingResult result, Model model) {
        log.info("CurveController updateCurvePoint");

        curvePoint.setId(id);

        if (result.hasErrors()) {
            model.addAttribute(curvePoint);
            return "curvePoint/update";
        }

        curvePointService.update(curvePoint);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {
        log.info("CurveController deleteCurvePoint");
        curvePointService.deleteById(id);
        return "redirect:/curvePoint/list";
    }
}
