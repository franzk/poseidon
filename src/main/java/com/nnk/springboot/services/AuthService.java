package com.nnk.springboot.services;

import com.nnk.springboot.domain.LoggedUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Contains {@link AuthService#getLoggedUser()} that returns information about the logged user
 */
@Service
public class AuthService {

    public LoggedUser getLoggedUser() {
        LoggedUser user = new LoggedUser();


        if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {

            user.setName(SecurityContextHolder.getContext().getAuthentication().getName());

            user.setAdmin(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(
                                     a -> a.getAuthority().equals("ADMIN")));

        }
        else {
            user.setName(
                ((OAuth2AuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication()).getPrincipal().getAttribute("login"));

            user.setAdmin(false);
        }

        return user;
    }
}
