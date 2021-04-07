package com.company;

import java.util.Objects;

abstract class CandyBox {
    private String flavor, origin;

    public CandyBox() {
        this.flavor = this.origin = "";
    }
    public CandyBox(String flavor, String origin) {
        this.flavor = flavor;
        this.origin = origin;
    }

    public abstract double getVolume();
    public String getFlavor() { return flavor; }
    public String getOrigin() { return origin; }
    public void setFlavor(String flavor) { this.flavor = flavor; }
    public void setOrigin(String origin) { this.origin = origin; }

    @Override
    public String toString() { return "CandyBox{flavor='" + flavor + ", origin='" + origin + "'}"; }

    /**
     * To determine equality between two CandyBox objects the flavour, origin and volume must be the same
     * @param o The object
     * @return Bool
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof CandyBox)) return false;
        CandyBox candyBox = (CandyBox) o;
        return Objects.equals(flavor, candyBox.flavor) && Objects.equals(origin, candyBox.origin) &&
                getVolume() == ((CandyBox) o).getVolume();
    }

    @Override
    public int hashCode() {
        return Objects.hash(flavor, origin, getVolume());
    }
}