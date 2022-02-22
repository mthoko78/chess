package com.mthoko.learners.domain.account;

import com.mthoko.learners.service.CalculatorService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class CalculatorServiceImplTest {
    // See pom.xml for exclusion to ensure we are using Junit 5

    // See https://www.youtube.com/watch?v=W40mpZP9xQQ for the different types of unit testing you get.
    // See https://www.baeldung.com/junit-5 for what you can do with Junit 5.

    @Autowired
    CalculatorService calculatorService;

    @BeforeAll
    static void setup() {
        log.info("\n\n@BeforeAll - EXECUTES ONCE BEFORE ALL TEST METHODS IN THIS CLASS\n");
    }

    @BeforeEach
    void init() {
        log.info("@BeforeEach - EXECUTES BEFORE EACH TEST METHOD IN THIS CLASS");
    }

    @AfterEach
    void tearDown() {
        log.info("@AfterEach - EXECUTED AFTER EACH TEST METHOD");
    }

    @AfterAll
    static void done() {
        log.info("@AfterAll - EXECUTED AFTER ALL TEST METHODS");
    }

    @Test
    void add() {
        // given
        var a = Integer.valueOf(1);
        var b = Integer.valueOf(1);

        // when
        var result = calculatorService.add(a, b);

        // then
        assertEquals(Integer.valueOf(2), result);
    }

    @DisplayName("This is our comparison test of integers")
    @Test
    void compare() {
        // given
        var a = Integer.valueOf(1);
        var b = Integer.valueOf(1);

        // when
        var result = calculatorService.compare(a, b);

        // then
        assertTrue(result);
    }

    @Test
    @Disabled
        // this test won't execute when all tests are run with builds and running all tests of this class.
        // this test can still be run, when individually executed.
    void example_lambda_expressions_test() {
        assertTrue(Stream.of(1, 2, 3)
                .mapToInt(i -> i)
                .sum() > 5, () -> "Sum should be greater than 5");
    }

    @Test
    void compare_exception_test() {
        // given
        Integer a = null;
        Integer b = null;

        // then
        assertThrows(NullPointerException.class, () ->
                // when
                calculatorService.compare(a, b)
        );
    }

    @ParameterizedTest
    @MethodSource("addArguments")
    void add_parameterized_values(Integer a, Integer b, Integer expectedResult) {
        // given - provided by arguments

        // when
        var result = calculatorService.add(a, b);

        // then
        assertEquals(expectedResult, result);
    }

    private static Stream<Arguments> addArguments() {
        return Stream.of(
                Arguments.of(1, 1, 2),
                Arguments.of(1, 2, 3),
                Arguments.of(1, 3, 4),
                Arguments.of(1, 4, 5)
        );
    }

    @ParameterizedTest
    @MethodSource("compareArguments")
    void compare_parameterized_values(Integer a, Integer b, Boolean expectedResult) {
        // given - provided by arguments

        // when
        var result = calculatorService.compare(a, b);

        // then
        assertEquals(expectedResult, result);
    }

    private static Stream<Arguments> compareArguments() {
        return Stream.of(
                Arguments.of(1, 1, true),
                Arguments.of(1, 2, false),
                Arguments.of(3, 3, true),
                Arguments.of(1, 4, false)
        );
    }

}