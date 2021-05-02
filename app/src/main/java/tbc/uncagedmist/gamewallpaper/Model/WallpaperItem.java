package tbc.uncagedmist.gamewallpaper.Model;

public class WallpaperItem {

    public String imageUrl;
    public String categoryId;
    public long viewCount;
    public long downloadCount;

    public WallpaperItem() {
    }

    public WallpaperItem(String imageUrl, String categoryId, long viewCount, long downloadCount) {
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.viewCount = viewCount;
        this.downloadCount = downloadCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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