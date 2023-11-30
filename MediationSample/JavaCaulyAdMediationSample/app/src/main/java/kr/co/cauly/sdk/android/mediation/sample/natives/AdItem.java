package kr.co.cauly.sdk.android.mediation.sample.natives;

public class AdItem {

    public String id;
    public String image;
    public String linkUrl;
    public String image_content_type;
    public String subtitle;
    public String icon;
    public String description;

    public AdItem(String id, String image, String linkUrl, String image_content_type,String subtitle,String icon,String description) {
        this.id = id;
        this.image = image;
        this.linkUrl = linkUrl;
        this.image_content_type = image_content_type;
        this.subtitle = subtitle;
        this.icon = icon;
        this.description = description;
    }
    public AdItem() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public void getSubtile(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImage_content_type() {
        return image_content_type;
    }

    public void setImage_content_type(String image_content_type) {
        this.image_content_type = image_content_type;
    }
}
