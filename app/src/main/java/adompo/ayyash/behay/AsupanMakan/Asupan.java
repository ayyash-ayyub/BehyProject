package adompo.ayyash.behay.AsupanMakan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by adwiarifin on 11/9/2017.
 */

public class Asupan {
    @SerializedName("id_makanan")
    @Expose
    public String idMakanan;

    @SerializedName("makanan")
    @Expose
    public String makanan;

    @SerializedName("jumlah")
    @Expose
    public String jumlah;

    @SerializedName("besaran_makanan")
    @Expose
    public String besaranMakanan;

    @SerializedName("id_besaran_makanan")
    @Expose
    public String idBesaranMakanan;

    @SerializedName("kalori")
    @Expose
    public String kalori;

    @SerializedName("protein")
    @Expose
    public String protein;

    @SerializedName("lemak")
    @Expose
    public String lemak;

    @SerializedName("karbohidrat")
    @Expose
    public String karbohidrat;
}
