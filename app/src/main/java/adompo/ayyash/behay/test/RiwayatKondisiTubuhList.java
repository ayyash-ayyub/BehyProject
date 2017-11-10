package adompo.ayyash.behay.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;



public class RiwayatKondisiTubuhList {

    @SerializedName("riwayat_kondisi_tubuh")
    @Expose
    private List<RiwayatKondisiTubuhModel> riwayatKondisiTubuh = null;

    public List<RiwayatKondisiTubuhModel> getRiwayatKondisiTubuh() {
        return riwayatKondisiTubuh;
    }

    public void setRiwayatKondisiTubuh(List<RiwayatKondisiTubuhModel> riwayatKondisiTubuh) {
        this.riwayatKondisiTubuh = riwayatKondisiTubuh;
    }
}
