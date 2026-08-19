package com.optiroute.backend.entity.cost;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cost_rule")
@Getter
@Setter
public class CostRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cost_parameter_id", nullable = false)
    private Long costParameterId;

    @Column(nullable = false)
    private boolean active = true;
}