package com.johan.db_cloud.model;

import jakarta.persistence.GeneratedValue;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Table(name = "plans")
@Builder
public class Plan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "max_instances", nullable = false)
    private Integer maxInstances;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "is_free")
    private Boolean isFree;

}