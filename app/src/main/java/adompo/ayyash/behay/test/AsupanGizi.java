package adompo.ayyash.behay.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AsupanGizi {

    @SerializedName("lemak")
    @Expose
    public Double lemak;
    @SerializedName("kalori")
    @Expose
    public Double kalori;
    @SerializedName("protein")
    @Expose
    public Double protein;
    @SerializedName("karbohidrat")
    @Expose
    public Double karbohidrat;

}