package adompo.ayyash.behay.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("nama")
    @Expose
    public String nama;
    @SerializedName("tanggal_lahir")
    @Expose
    public String tanggalLahir;

}