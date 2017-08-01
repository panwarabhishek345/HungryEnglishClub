package app.com.HungryEnglish.Model.Teacher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by R'jul on 7/15/2017.
 */

public class InfoResponse {
    @SerializedName("image1")
    @Expose
    private String image1;

    @SerializedName("image2")
    @Expose
    private String image2;

    @SerializedName("image3")
    @Expose
    private String image3;
    @SerializedName("link1")
    @Expose
    private String link1;
    @SerializedName("link2")
    @Expose
    private String link2;
    @SerializedName("link3")
    @Expose
    private String link3;

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getLink1() {
        return link1;
    }

    public void setLink1(String link1) {
        this.link1 = link1;
    }

    public String getLink2() {
        return link2;
    }

    public void setLink2(String link2) {
        this.link2 = link2;
    }

    public String getLink3() {
        return link3;
    }

    public void setLink3(String link3) {
        this.link3 = link3;
    }

}
