package com.taot.tdxdata;

public enum PriceMode {

    HUNDRED(100.0), THOUSAND(1000.0);

    private final double divider;

    private PriceMode(double divider) {
        this.divider = divider;
    }

    public double getDivider() {
        return divider;
    }
}
