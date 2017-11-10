package adompo.ayyash.behay.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RiwayatKondisiTubuhModel {

    @SerializedName("tanggal")
    @Expose
    private String tanggal;
    @SerializedName("timestamp")
    @Expose
    private Integer timestamp;
    @SerializedName("umur")
    @Expose
    private Umur umur;
    @SerializedName("imt")
    @Expose
    private Double imt;
    @SerializedName("z_score")
    @Expose
    private Integer zScore;
    @SerializedName("status_gizi")
    @Expose
    private String statusGizi;
    @SerializedName("berat_badan")
    @Expose
    private String beratBadan;
    @SerializedName("tinggi_badan")
    @Expose
    private Double tinggiBadan;
    @SerializedName("lemak_tubuh")
    @Expose
    private String lemakTubuh;

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Umur getUmur() {
        return umur;
    }

    public void setUmur(Umur umur) {
        this.umur = umur;
    }

    public Double getImt() {
        return imt;
    }

    public void setImt(Double imt) {
        this.imt = imt;
    }

    public Integer getZScore() {
        return zScore;
    }

    public void setZScore(Integer zScore) {
        this.zScore = zScore;
    }

    public String getStatusGizi() {
        return statusGizi;
    }

    public void setStatusGizi(String statusGizi) {
        this.statusGizi = statusGizi;
    }

    public String getBeratBadan() {
        return beratBadan;
    }

    public void setBeratBadan(String beratBadan) {
        this.beratBadan = beratBadan;
    }

    public Double getTinggiBadan() {
        return tinggiBadan;
    }

    public void setTinggiBadan(Double tinggiBadan) {
        this.tinggiBadan = tinggiBadan;
    }

    public String getLemakTubuh() {
        return lemakTubuh;
    }

    public void setLemakTubuh(String lemakTubuh) {
        this.lemakTubuh = lemakTubuh;
    }

}