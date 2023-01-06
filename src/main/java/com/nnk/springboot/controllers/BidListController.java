package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.BidListNotFoundException;
import com.nnk.springboot.services.AuthService;
import com.nnk.springboot.services.BidListService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;


/**
 * CRUD Controller that launch ThymeLeaf renderings.
 * <br>
 * Concerns {@link com.nnk.springboot.domain.BidList} entities
 */
@Log4j2
@Controller
public class BidListController {

    private final BidListService bidListService;

    private final AuthService authService;

    public BidListController(BidListService bidListService, AuthService authService) {
        this.bidListService = bidListService;
        this.authService = authService;
    }

    @RequestMapping("/")
    public void baseUrl(HttpServletResponse response) throws IOException {
        response.sendRedirect("/bidList/list");
    }

    @RequestMapping("/bidList/list")
    public String home(Model model) {
        log.info("BidListController List");
        model.addAttribute("bidLists", bidListService.getAll());
        model.addAttribute("loggedUser", authService.getLoggedUser());
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(Model model) {
        log.info("BidListController addBidForm");
        model.addAttribute(new BidList());
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        log.info("BidListController validate");
        if (result.hasErrors()) {
            return "bidList/add";
        }
        bidListService.add(bid);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws BidListNotFoundException {
        log.info("BidListController showUpdateForm");
        model.addAttribute(bidListService.getById(id));
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                            BindingResult result, Model model) {
        log.info("BidListController updateBid");
        bidList.setBidListId(id);

        if (result.hasErrors()) {
            model.addAttribute("bidList", bidList);
            return "bidList/update";
        }

        bidListService.update(bidList);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        log.info("BidListController deleteBid");
        bidListService.deleteById(id);
        return "redirect:/bidList/list";
    }
}
