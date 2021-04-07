package com.company;

interface Minus {
    public void minus(float nr);
}
interface Plus {
    public void plus(float nr);
}
interface Mult {
    public void mult(float nr);
}
interface Div {
    public void div(float nr);
}

class Operation implements Minus, Plus, Mult, Div {
    private float nr;

    Operation(float nr) { this.nr = nr; }

    @Override
    public void minus(float nr) { this.nr -= nr; }
    @Override
    public void plus(float nr) { this.nr += nr; }
    @Override
    public void mult(float nr) { this.nr *= nr; }
    @Override
    public void div(float nr) { this.nr /= nr; }

    @Override
    public String toString() {
        return "Operation{" +
                "nr=" + nr +
                '}';
    }
}

public class Ex3 {
    public static void main(String[] args) {
        // (0 + 2) * 3 / 4 - 5 = -3.5
        Operation x = new Operation(0);
        x.plus(2);
        x.mult(3);
        x.div(4);
        x.minus(5);
        System.out.println(x);
    }
}
