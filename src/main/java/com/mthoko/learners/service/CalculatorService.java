package com.mthoko.learners.service;

import lombok.NonNull;

public interface CalculatorService {
    Integer add(@NonNull Integer a, @NonNull Integer b);

    Boolean compare(Integer a, Integer b);
}
