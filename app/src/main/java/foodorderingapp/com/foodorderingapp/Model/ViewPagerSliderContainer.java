package foodorderingapp.com.foodorderingapp.Model;

public class ViewPagerSliderContainer {
    private String heading;
    private int imgId;
    private String description;

    public ViewPagerSliderContainer(String heading, int imgId, String description)
    {
        this.heading = heading;
        this.imgId = imgId;
        this.description = description;
    }

    public String getHeading()
    {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
