package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.RatingNotFoundException;
import com.nnk.springboot.services.AuthService;
import com.nnk.springboot.services.RatingService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * CRUD Controller that launch ThymeLeaf renderings.
 * <br>
 * Concerns {@link com.nnk.springboot.domain.Rating} entities
 */
@Log4j2
@Controller
public class RatingController {

    private final RatingService ratingService;
    private final AuthService authService;

    public RatingController(RatingService ratingService, AuthService authService) {
        this.ratingService = ratingService;
        this.authService = authService;
    }

    @RequestMapping("/rating/list")
    public String home(Model model)
    {
        log.info("RatingController home");
        model.addAttribute("ratings", ratingService.getAll());
        model.addAttribute("loggedUser", authService.getLoggedUser());
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Model model) {
        log.info("RatingController addRatingForm");
        model.addAttribute(new Rating());
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        log.info("RatingController validate");
        if (result.hasErrors()) {
            return "rating/add";
        }
        ratingService.add(rating);
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws RatingNotFoundException {
        log.info("RatingController showUpdateForm");
        model.addAttribute(ratingService.getById(id));
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                             BindingResult result, Model model) {
        log.info("RatingController updateRating");
        rating.setId(id);
        if (result.hasErrors()) {
            model.addAttribute(rating);
            return "rating/update";
        }
        ratingService.update(rating);
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        log.info("RatingController deleteRating");
        ratingService.deleteById(id);
        return "redirect:/rating/list";
    }
}
