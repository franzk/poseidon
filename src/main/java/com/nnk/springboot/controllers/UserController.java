package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.UserNotFoundException;
import com.nnk.springboot.services.AuthService;
import com.nnk.springboot.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
 * Concerns {@link com.nnk.springboot.domain.User} entities
 * <br>
 * Endpoints are restricted to ADMIN users,
 * see {@link com.nnk.springboot.config.SpringSecurityConfig}
 */
@Log4j2
@Controller
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @RequestMapping("/user/list")
    public String home(Model model)
    {
        log.info("UserController home");
        model.addAttribute("users", userService.getAll());
        model.addAttribute("loggedUser", authService.getLoggedUser());
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUserForm(Model model) {
        log.info("UserController addUser");
        model.addAttribute(new User());
        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {
        log.info("UserController validate");

        if (result.hasErrors()) {
            model.addAttribute(user);
            return "user/add";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        userService.add(user);
        return "redirect:/user/list";

    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) throws UserNotFoundException {
        log.info("UserController showUpdateForm");

        User user = userService.getById(id);
        user.setPassword("");
        model.addAttribute(user);
        return "user/update";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        log.info("UserController updateUser");

        user.setId(id);
        if (result.hasErrors()) {
            model.addAttribute(user);
            return "user/update";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));

        userService.update(user);
        return "redirect:/user/list";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        log.info("UserController updateUser");

        userService.deleteById(id);
        return "redirect:/user/list";
    }
}
