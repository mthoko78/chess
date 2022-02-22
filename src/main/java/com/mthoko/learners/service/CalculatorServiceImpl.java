package com.mthoko.learners.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CalculatorServiceImpl implements CalculatorService {
    // Data types used here is specific for testing demo.

    @Override
    public Integer add(@NonNull Integer a, @NonNull Integer b) {
        return a + b;
    }

    @Override
    public Boolean compare(Integer a, Integer b) {
        if (Objects.isNull(a) || Objects.isNull(b)) {
            throw new NullPointerException("Null value aren't accepted.");
        }

        return a.equals(b);
    }

}
