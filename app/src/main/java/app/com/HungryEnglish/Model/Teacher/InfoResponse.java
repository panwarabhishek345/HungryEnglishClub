package app.com.HungryEnglish.Model.Teacher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by R'jul on 7/15/2017.
 */

public class InfoResponse {
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("link1")
    @Expose
    private String link1;
    @SerializedName("link2")
    @Expose
    private String link2;
    @SerializedName("link3")
    @Expose
    private String link3;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
