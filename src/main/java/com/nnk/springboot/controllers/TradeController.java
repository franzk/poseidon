package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.TradeNotFoundException;
import com.nnk.springboot.services.AuthService;
import com.nnk.springboot.services.TradeService;
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
 * Concerns {@link com.nnk.springboot.domain.Trade} entities
 */
@Log4j2
@Controller
public class TradeController {

    private final TradeService tradeService;
    private final AuthService authService;

    public TradeController(TradeService tradeService, AuthService authService) {
        this.tradeService = tradeService;
        this.authService = authService;
    }


    @RequestMapping("/trade/list")
    public String home(Model model)
    {
        log.info("TradeController home");
        model.addAttribute("trades", tradeService.getAll());
        model.addAttribute("loggedUser", authService.getLoggedUser());
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addTradeForm(Model model) {
        log.info("TradeController addUser");
        model.addAttribute(new Trade());
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        log.info("TradeController validate");
        if (result.hasErrors()) {
            return "trade/add";
        }
        tradeService.add(trade);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws TradeNotFoundException {
        log.info("TradeController showUpdateForm");
        model.addAttribute(tradeService.getById(id));
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                             BindingResult result, Model model) {
        log.info("TradeController updateTrade");

        trade.setTradeId(id);
        if (result.hasErrors()) {
            model.addAttribute(trade);
            return "trade/update";
        }

        tradeService.update(trade);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        log.info("TradeController deleteTrade");
        tradeService.deleteById(id);
        return "redirect:/trade/list";
    }
}
