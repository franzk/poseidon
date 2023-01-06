package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

/**
 * CurvePoint entity
 */
@Data
@Entity
@Table(name = "curvepoint")
public class CurvePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull(message = "{curvePoint.curveId.NotNull}")
    Integer curveId;

    Timestamp asOfDate;

    @NotNull(message = "{curvePoint.term.NotNull}")
    Double term;

    @NotNull(message = "{curvePoint.value.NotNull}")
    @Column(name = "_value")
    Double value;

    Timestamp creationDate;



}
