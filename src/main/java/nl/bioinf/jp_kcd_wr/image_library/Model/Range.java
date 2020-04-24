package nl.bioinf.jp_kcd_wr.image_library.Model;

public class Range {
    private double minimum;
    private double maximum;

    public Range(double minimum, double maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public double getMinimum() {
        return minimum;
    }


    public double getMaximum() {
        return maximum;
    }

}
