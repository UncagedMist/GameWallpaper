package tbc.uncagedmist.gamewallpaper.Model;

public class Rating {
    private String wallpaperId;
    private String rateValue;

    public Rating() {
    }

    public Rating(String wallpaperId, String rateValue) {
        this.wallpaperId = wallpaperId;
        this.rateValue = rateValue;
    }

    public String getWallpaperId() {
        return wallpaperId;
    }

    public void setWallpaperId(String wallpaperId) {
        this.wallpaperId = wallpaperId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }
}
