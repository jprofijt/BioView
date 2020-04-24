package nl.bioinf.jp_kcd_wr.image_library.Model;

public class regionOfInterestState {
    private int Id;
    private double ph;
    private double temp;
    private int o2;
    private int Co2;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public double getPh() {
        return ph;
    }

    public void setPh(double ph) {
        this.ph = ph;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getO2() {
        return o2;
    }

    public void setO2(int o2) {
        this.o2 = o2;
    }

    public int getCo2() {
        return Co2;
    }

    public void setCo2(int co2) {
        Co2 = co2;
    }
}
