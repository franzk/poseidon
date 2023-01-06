package com.nnk.springboot.controllers;

import com.nnk.springboot.exceptions.UserNotFoundException;
import com.nnk.springboot.services.OAuth2LoginService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.Principal;

/**
 * Handle OAuth2 Login
 * <br>
 * Call {@link com.nnk.springboot.services.OAuth2LoginService#oauth2Login(Principal)}
 * that either create a new user or update an existing user if attributes changed.
 */
@Controller
@RequestMapping("app")
public class OAuthController {

    private final OAuth2LoginService oAuth2LoginService;

    public OAuthController(OAuth2LoginService oAuth2LoginService) {
        this.oAuth2LoginService = oAuth2LoginService;
    }

    @GetMapping("/oauth2login")
    @RolesAllowed("USER")
    public void oauth2Login(Principal user, HttpServletResponse response) throws IOException {
        oAuth2LoginService.oauth2Login(user);
        response.sendRedirect("/bidList/list");
    }
}
