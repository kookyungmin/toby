package net.happykoo.toby.constant;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum Level {
    BRONZE(1),
    SILVER(2),
    GOLD(3);

    private final int value;

    public int getIntValue() {
        return value;
    }

    public Level getNextLevel() {
        switch (this) {
            case BRONZE: return SILVER;
            case SILVER: return GOLD;
            default: return null;
        }
    }

    public static Level valueOf(int value) {
       return Arrays.asList(Level.values())
               .stream()
               .filter(level -> level.getIntValue() == value)
               .findFirst()
               .orElseThrow(() -> new AssertionError("Unknown value: " + value));
    }
}
