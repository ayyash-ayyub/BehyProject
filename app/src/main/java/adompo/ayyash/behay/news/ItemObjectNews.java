package adompo.ayyash.behay.news;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ItemObjectNews {
    public class ObjectAkatifitas {
        @SerializedName("news")
        public List<Results> news;

        public class Results {
            @SerializedName("id")
            public int id;

            @SerializedName("kategori")
            public String kategori;

            @SerializedName("gambar")
            public String gambar;

            @SerializedName("judul")
            public String judul;

            @SerializedName("tanggal")
            public String tanggal;


        }
    }
}
