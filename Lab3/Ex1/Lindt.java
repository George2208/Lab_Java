package com.company;

class Lindt extends CandyBox {
    double radius, height;

    public Lindt() {
        super();
        radius = height = 0;
    }
    public Lindt(String flavor, String origin, int radius, int height) {
        super(flavor, origin);
        this.radius = radius;
        this.height = height;
    }

    @Override
    public double getVolume() { return 2 * Math.PI * radius * height; }

    @Override
    public String toString() { return "The " + getOrigin() + " " + getFlavor() + " has volume " + getVolume(); }
}