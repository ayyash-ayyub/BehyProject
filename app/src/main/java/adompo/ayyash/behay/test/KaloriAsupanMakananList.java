package adompo.ayyash.behay.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KaloriAsupanMakananList {

    @SerializedName("kalori_asupan_makanan")
    @Expose
    public List<KaloriAsupanMakananModel> kaloriAsupanMakanan = null;

}