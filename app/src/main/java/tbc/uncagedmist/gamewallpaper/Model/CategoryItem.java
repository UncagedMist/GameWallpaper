package tbc.uncagedmist.gamewallpaper.Model;

public class CategoryItem {

    public String name;
    public String imageLink;
    public String desc;

    public CategoryItem() {
    }

    public CategoryItem(String name, String imageLink, String desc) {
        this.name = name;
        this.imageLink = imageLink;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}