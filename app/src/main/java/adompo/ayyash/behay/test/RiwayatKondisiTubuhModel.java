package adompo.ayyash.behay.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RiwayatKondisiTubuhModel {

    @SerializedName("tanggal")
    @Expose
    public String tanggal;
    @SerializedName("timestamp")
    @Expose
    public Integer timestamp;
    @SerializedName("umur")
    @Expose
    public Umur umur;
    @SerializedName("imt")
    @Expose
    public Double imt;
    @SerializedName("z_score")
    @Expose
    public Integer zScore;
    @SerializedName("status_gizi")
    @Expose
    public String statusGizi;
    @SerializedName("berat_badan")
    @Expose
    public String beratBadan;
    @SerializedName("tinggi_badan")
    @Expose
    public Double tinggiBadan;
    @SerializedName("lemak_tubuh")
    @Expose
    public String lemakTubuh;

}