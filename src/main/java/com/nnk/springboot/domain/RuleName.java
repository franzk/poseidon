package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * RuleName entity
 */
@Data
@Entity
@Table(name = "rulename")
public class RuleName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotBlank(message = "{ruleName.name.NotBlank}")
    String name;

    @NotBlank(message = "{ruleName.description.NotBlank}")
    String description;

    @NotBlank(message = "{ruleName.json.NotBlank}")
    String json;

    @NotBlank(message = "{ruleName.template.NotBlank}")
    String template;

    @NotBlank(message = "{ruleName.sqlStr.NotBlank}")
    String sqlStr;

    @NotBlank(message = "{ruleName.sqlPart.NotBlank}")
    String sqlPart;
}
