package de.rwi.bitside.codingchallenge.discount;

public class DiscountNotFoundException extends RuntimeException {

    public DiscountNotFoundException() {
        super("Discount not found");
    }
}
