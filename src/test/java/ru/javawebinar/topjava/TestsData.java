package ru.javawebinar.topjava;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class TestsData<T> {

    private String[] ignoringFields;

    public TestsData(String... ignoringFields) {
        this.ignoringFields = ignoringFields;
    }

    public void assertMatch(T actual, T expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields(ignoringFields).isEqualTo(expected);
    }

    @SafeVarargs
    public final void assertMatch(Iterable<T> actual, T... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public void assertMatch(Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields(ignoringFields).isEqualTo(expected);
    }

    public void assertEmpty(Iterable<T> emptyList) {
        assertThat(emptyList).isEmpty();
    }
}
