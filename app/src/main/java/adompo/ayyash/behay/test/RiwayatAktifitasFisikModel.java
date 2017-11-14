package adompo.ayyash.behay.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RiwayatAktifitasFisikModel {

    @SerializedName("kalori")
    @Expose
    public String kalori;
    @SerializedName("tanggal")
    @Expose
    public String tanggal;
    @SerializedName("timestamp")
    @Expose
    public String timestamp;

}