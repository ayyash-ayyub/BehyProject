package adompo.ayyash.behay.AsupanMakan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by adwiarifin on 11/9/2017.
 */

public class AsupanModel {
    @SerializedName("asupan_makanan")
    @Expose
    public List<Asupan> listAsupan = null;
}
