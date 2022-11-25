package ru.job4j.cinema.enums;

public enum Cell {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);

    private final int value;

    Cell(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
