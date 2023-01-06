package com.nnk.springboot.domain;

import com.nnk.springboot.annotations.Password;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import net.bytebuddy.utility.RandomString;

/**
 * User entity
 */
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "{user.username.NotBlank}")
    private String username;

    @Password(message = "{user.password.NotValid}")
    private String password = "Aa!2" + RandomString.make(64); // Random password for OAuth2 users.
                                                                    // Still it's unused, it must respect the constraints to avoid errors on add or update

    @NotBlank(message = "{user.fullname.NotBlank}")
    private String fullname;

    @NotBlank(message = "{user.role.NotBlank}")
    private String role;

}
