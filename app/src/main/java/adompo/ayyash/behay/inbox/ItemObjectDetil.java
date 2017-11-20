package adompo.ayyash.behay.inbox;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ItemObjectDetil {
    public class ObjectDetil {
        @SerializedName("pesan")
        public List<Results> pesan;

        public class Results {
            @SerializedName("id")
            public int id;

            @SerializedName("sender")
            public String sender;

            @SerializedName("nama_sender")
            public String nama_sender;

            @SerializedName("subjek")
            public String subjek;

            @SerializedName("pesan")
            public String pesan;

            @SerializedName("updated_at")
            public String tanggal;


        }
    }
}
