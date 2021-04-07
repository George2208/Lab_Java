package com.company;

class Milka extends CandyBox {
    double length;

    public Milka() {
        super();
        length = 0;
    }
    public Milka(String flavor, String origin, int length) {
        super(flavor, origin);
        this.length = length;
    }

    @Override
    public double getVolume() { return length * length * length; }

    @Override
    public String toString() { return "The " + getOrigin() + " " + getFlavor() + " has volume " + getVolume(); }
}