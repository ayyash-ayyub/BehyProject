package adompo.ayyash.behay.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Urt {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("gambar")
    @Expose
    private Object gambar;
    @SerializedName("besaran_makanan")
    @Expose
    private String besaranMakanan;
    @SerializedName("kalori")
    @Expose
    private String kalori;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getGambar() {
        return gambar;
    }

    public void setGambar(Object gambar) {
        this.gambar = gambar;
    }

    public String getBesaranMakanan() {
        return besaranMakanan;
    }

    public void setBesaranMakanan(String besaranMakanan) {
        this.besaranMakanan = besaranMakanan;
    }

    public String getKalori() {
        return kalori;
    }

    public void setKalori(String kalori) {
        this.kalori = kalori;
    }

}