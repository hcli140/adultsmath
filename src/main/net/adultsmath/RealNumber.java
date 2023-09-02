package main.net.adultsmath;

public record RealNumber (double value) {

    public RealNumber add (RealNumber a) {return new RealNumber(a.value + this.value);}

    public RealNumber subtract (RealNumber a) {return new RealNumber(this.value - a.value);}

    public RealNumber multiply (RealNumber a) {return new RealNumber(a.value * this.value);}

    public RealNumber divide (RealNumber a) {return new RealNumber(this.value / a.value);}

    public RealNumber power (RealNumber n) {return new RealNumber(Math.pow(this.value, n.value));}

    public RealNumber root (RealNumber n) {return new RealNumber(Math.pow(this.value, 1.0/n.value));}


    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
