package de.rwi.bitside.codingchallenge.discount;

public enum DiscountType {

    BUY_1_GET_1_FREE(50.0),
    TEN_PERCENT_OFF(10.0);

    private final double percentage;

    DiscountType(double percentage) {
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }
}
