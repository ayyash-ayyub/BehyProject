package adompo.ayyash.behay.AktifitasFisikUser;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class AktifitasFisikUserModel {
    @SerializedName("aktifitas_fisik")
    public List<Results> listAktifitasFisik;

    class Results {
        @SerializedName("id")
        int id;

        @SerializedName("kode")
        String kode;

        @SerializedName("aktifitas")
        String aktifitas;

        @SerializedName("durasi")
        String durasi;

        @SerializedName("kalori")
        double kalori;
    }
}
