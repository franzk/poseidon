package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

/**
 * Contains {@link com.nnk.springboot.services.OAuth2LoginService#oauth2Login(Principal)}
 * that create a new {@link com.nnk.springboot.domain.User} if the OAuth2 user does not exist
 * or update him if an attribute changed
 */
@Service
public class OAuth2LoginService {

    @Autowired
    private UserService userService;

    public User oauth2Login(Principal user)  {

        OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) user);

        Map<String, Object> userDetails = ((DefaultOAuth2User) authToken.getPrincipal()).getAttributes();

        // could call getClaims(user) here if needed

        String username = userDetails.get("login").toString();
        String fullname = userDetails.get("name").toString();


        Optional<User> databaseUser = userService.getByUsername(username);

        if (databaseUser.isEmpty()) {
            // if the user doesn't exist, let's create it !
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setFullname(fullname);
            newUser.setRole("USER");
            return userService.add(newUser);
        } else {
            // if the user, update the name if it has changed
            User existingUser = databaseUser.get();
            if (existingUser.getFullname() != fullname) {
                existingUser.setFullname(fullname);
                return userService.update(existingUser);
            }
            else {
                return existingUser;
            }
        }

    }



    /*private Map getClaims(Principal user) {
        OAuth2User principal = ((OAuth2AuthenticationToken) user).getPrincipal();

        OidcIdToken idToken = getIdToken(principal);

        if (idToken != null) {
            return idToken.getClaims();
        } else {
            return new HashMap<>();
        }
    }

    private OidcIdToken getIdToken(OAuth2User principal) {
        if (principal instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
            return oidcUser.getIdToken();
        }

        return null;
    }*/

}

