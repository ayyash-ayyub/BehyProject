package adompo.ayyash.behay.test;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailMakan {

    @SerializedName("makanan")
    @Expose
    private Makanan makanan;
    @SerializedName("urt")
    @Expose
    private List<Urt> urt = null;

    public Makanan getMakanan() {
        return makanan;
    }

    public void setMakanan(Makanan makanan) {
        this.makanan = makanan;
    }

    public List<Urt> getUrt() {
        return urt;
    }

    public void setUrt(List<Urt> urt) {
        this.urt = urt;
    }

}