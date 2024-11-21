package de.rwi.bitside.codingchallenge.basket;

public class BasketNotFoundException extends RuntimeException {

    public BasketNotFoundException() {
        super("Basket not found");
    }
}
