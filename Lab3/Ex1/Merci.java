package com.company;

class Merci extends CandyBox {
    double length, width, height;

    public Merci() {
        super();
        length = width = height = 0;
    }
    public Merci(String flavor, String origin, int length, int width, int height) {
        super(flavor, origin);
        this.length = length;
        this.width = width;
        this.height = height;
    }

    @Override
    public double getVolume() { return length * width * height; }

    @Override
    public String toString() { return "The " + getOrigin() + " " + getFlavor() + " has volume " + getVolume(); }
}