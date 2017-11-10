package adompo.ayyash.behay.AsupanMakan;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class AktifitasSarapanModel {
    @SerializedName("list_asupan_makanan")
    public List<Results> listAktifitasSarapan;

    class Results {
        @SerializedName("id")
        int id;

        @SerializedName("kode")
        String kode;


        @SerializedName("kalori")
        String kalori;

        @SerializedName("besaran_makanan")
        String besaran_makanan;

        @SerializedName("nama_makanan")
        String nama_makanan;

        @SerializedName("gambar")
        String gambar;




    }
}
