package com.mthoko.learners.persistence.entity;


import com.mthoko.learners.persistence.entity.common.AbstractBaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

@AllArgsConstructor
@Data
@Entity(name = "calculator")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class CalculatorEntity extends AbstractBaseEntity {

    @Column(name = "value_a")
    private Integer valueA;

    @Column(name = "value_b")
    private Integer valueB;

    @Column(name = "add_result")
    private Integer addResult;

    @Column(name = "compare_result")
    private Boolean compareResult;

}
