package adompo.ayyash.behay.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RiwayatAsupanGiziModel {

    @SerializedName("tanggal")
    @Expose
    public String tanggal;
    @SerializedName("timestamp")
    @Expose
    public Integer timestamp;
    @SerializedName("kalori")
    @Expose
    public Double kalori;
    @SerializedName("protein")
    @Expose
    public Double protein;
    @SerializedName("lemak")
    @Expose
    public Double lemak;
    @SerializedName("karbohidrat")
    @Expose
    public Double karbohidrat;

}