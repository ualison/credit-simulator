package com.credit.simulator.consumer.domain.model.enums;

public enum AgeRangeEnum {
    UP_TO_25(0, 25),
    FROM_26_TO_40(26, 40),
    FROM_41_TO_60(41, 60),
    ABOVE_60(61, Integer.MAX_VALUE);

    private final int minAge;
    private final int maxAge;

    AgeRangeEnum(int minAge, int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public int getMinAge() { return minAge; }
    public int getMaxAge() { return maxAge; }
}