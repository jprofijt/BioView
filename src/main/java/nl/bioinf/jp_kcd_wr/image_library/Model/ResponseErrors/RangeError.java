package nl.bioinf.jp_kcd_wr.image_library.Model.ResponseErrors;

import org.springframework.http.HttpStatus;

public class RangeError extends NormalErrorResponse{
    private double minimum;
    private double maximum;

    public RangeError(String message, HttpStatus status, double minimum, double maximum) {
        super(message, status);
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
