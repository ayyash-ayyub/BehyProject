package adompo.ayyash.behay.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class KaloriAsupanMakananModel {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("kalori")
    @Expose
    public String kalori;
}
