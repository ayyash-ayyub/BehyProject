package adompo.ayyash.behay.test;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KaloriAsupanMakananList {

    @SerializedName("kalori_asupan_makanan")
    @Expose
    public List<KaloriAsupanMakananModel> kaloriAsupanMakanan = null;

}