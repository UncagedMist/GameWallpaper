package tbc.uncagedmist.gamewallpaper.Model;

public class Wallpapers {
    private String imageId;
    private String imageLink;
    public long viewCount;
    public long downloadCount;

    public Wallpapers() {
    }

    public Wallpapers(String imageId, String imageLink, long viewCount, long downloadCount) {
        this.imageId = imageId;
        this.imageLink = imageLink;
        this.viewCount = viewCount;
        this.downloadCount = downloadCount;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(long downloadCount) {
        this.downloadCount = downloadCount;
    }
}