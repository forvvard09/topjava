package ru.javawebinar.topjava;

import ru.javawebinar.topjava.to.MealTo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AssertToMatcher {

    //AssertDTO
    public static void assertMatchTo(List<MealTo> actual, List<MealTo> expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
