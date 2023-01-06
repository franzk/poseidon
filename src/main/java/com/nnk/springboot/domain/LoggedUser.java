package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Contains information about the currently logged user
 */
@Getter
@Setter
public class LoggedUser {
    private String name;
    private boolean admin;

}
