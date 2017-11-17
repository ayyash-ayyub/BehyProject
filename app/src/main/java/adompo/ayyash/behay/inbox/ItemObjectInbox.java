package adompo.ayyash.behay.inbox;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ItemObjectInbox {
    public class ObjectInbox {
        @SerializedName("list_inbox")
        public List<Results> list_inbox;

        public class Results {
            @SerializedName("id")
            public int id;

            @SerializedName("nama")
            public String nama;

            @SerializedName("subjek")
            public String subjek;

            @SerializedName("mark")
            public String mark;

            @SerializedName("tanggal")
            public String tanggal;


        }
    }
}
