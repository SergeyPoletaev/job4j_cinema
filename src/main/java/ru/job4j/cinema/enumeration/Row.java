package ru.job4j.cinema.enumeration;

public enum Row {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);

    private final int value;

    Row(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
