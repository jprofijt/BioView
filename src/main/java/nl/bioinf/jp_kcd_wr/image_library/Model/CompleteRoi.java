package nl.bioinf.jp_kcd_wr.image_library.Model;

import java.util.List;

public class CompleteRoi {

    private double ph;
    private double temp;
    private int o2;
    private int co2;
    private List<String> tags;
    private int image;

    public CompleteRoi(List<String> tags, regionOfInterestState state, int image) {
        this.tags = tags;
        this.ph = state.getPh();
        this.temp = state.getTemp();
        this.o2 = state.getO2();
        this.co2 = state.getCo2();
        this.image = image;
    }

    public double getPh() {
        return ph;
    }

    public double getTemp() {
        return temp;
    }

    public int getO2() {
        return o2;
    }

    public int getCo2() {
        return co2;
    }

    public List<String> getTags() {
        return tags;
    }

    public int getImage() {
        return image;
    }
}
