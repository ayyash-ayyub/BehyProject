package adompo.ayyash.behay.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RiwayatPemenuhanGiziModel {

    @SerializedName("tanggal")
    @Expose
    public String tanggal;
    @SerializedName("timestamp")
    @Expose
    public Integer timestamp;
    @SerializedName("energi")
    @Expose
    public Double energi;
    @SerializedName("lemak")
    @Expose
    public Double lemak;
    @SerializedName("protein")
    @Expose
    public Double protein;
    @SerializedName("karbohidrat")
    @Expose
    public Double karbohidrat;

}