package adompo.ayyash.behay.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SummaryModel {

    @SerializedName("exist")
    @Expose
    public Integer exist;
    @SerializedName("nilai_imt")
    @Expose
    public Double nilaiImt;
    @SerializedName("status_gizi")
    @Expose
    public String statusGizi;
    @SerializedName("berat_badan")
    @Expose
    public Integer beratBadan;
    @SerializedName("lemak_tubuh")
    @Expose
    public Integer lemakTubuh;
    @SerializedName("kalori_aktifitas_fisik")
    @Expose
    public Double kaloriAktifitasFisik;
    @SerializedName("asupan_gizi")
    @Expose
    public AsupanGizi asupanGizi;
    @SerializedName("kebutuhan_gizi")
    @Expose
    public KebutuhanGizi kebutuhanGizi;
    @SerializedName("pemenuhan_gizi")
    @Expose
    public PemenuhanGizi pemenuhanGizi;

}