package adompo.ayyash.behay.test;

import com.google.gson.annotations.SerializedName;

import java.util.List;



public class AktifitasFisikModel {
    @SerializedName("list_aktifitas_fisik")
    public List<Results> listAktifitasFisik;

    class Results {
        @SerializedName("id")
        int id;

        @SerializedName("kode")
        String kode;

        @SerializedName("gambar")
        String gambar;

        @SerializedName("aktifitas")
        String aktifitas;

        @SerializedName("mets")
        double mets;
    }
}
