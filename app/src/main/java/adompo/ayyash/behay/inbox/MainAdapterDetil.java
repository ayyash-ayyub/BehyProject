package adompo.ayyash.behay.inbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

import adompo.ayyash.behay.R;
import adompo.ayyash.behay.TipsSehatDetail;

//import adompo.ayyash.behay.ConfigUmum;


public class MainAdapterDetil extends RecyclerView.Adapter<MainAdapterDetil.MainHolderDetil> {

    ProgressDialog progressDialog;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    public static String a;




    public List<ItemObjectDetil.ObjectDetil.Results> resultsList;
    public Context context;

    public MainAdapterDetil(Context context, List<ItemObjectDetil.ObjectDetil.Results> resultsList) {
        this.context = context;
        this.resultsList = resultsList;
    }

    @Override
    public int getItemViewType(int position) {

        a = resultsList.get(position).sender;

        if (a=="1") {
// If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
// If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public MainHolderDetil onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        MainHolderDetil mainHolder;

        switch (viewType){
            case VIEW_TYPE_MESSAGE_SENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbox_kiri, parent, false);
                mainHolder = new MainHolderDetil(view);
                return mainHolder;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbox_kiri, parent, false);
                mainHolder = new MainHolderDetil(view);
                return mainHolder;
        }

        return null;
    }

    public void DeleteData(String Url) {
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url,
                new Response.Listener<String>() {;
                    @Override
                    public void onResponse(String response) {
                        Log.d("uye jadi hapus", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("uye gagal hapus", error.toString());

            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onBindViewHolder(MainHolderDetil holder, final int position) {
        holder.tvMsg.setText(Html.fromHtml(resultsList.get(position).pesan));
        holder.tvPengirim.setText(resultsList.get(position).nama_sender);
        holder.tvTgl.setText(resultsList.get(position).tanggal);
        if (resultsList.get(position).sender.equals("1")){
            holder.tvMsg.setGravity(View.TEXT_ALIGNMENT_VIEW_END);
        }


        final int idvalue = resultsList.get(position).id;

        holder.cardview_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


//                Toast.makeText(context, resultsList.get(position).id, Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(context, TipsSehatDetail.class);
//                i.putExtra("id", idvalue);
//                view.getContext().startActivity(i);



            }
        });
    }

    @Override
    public int getItemCount() {
        return this.resultsList.size();
    }




    public final static class MainHolderDetil extends RecyclerView.ViewHolder {

        ImageView imgNew;
        TextView tvMsg, tvPengirim, tvTgl;
        CardView cardview_item;

        MainHolderDetil(View itemView) {
            super(itemView);

            imgNew = (ImageView)itemView.findViewById(R.id.imgNew);
            tvMsg = (TextView) itemView.findViewById(R.id.tvMsg);
            tvTgl = (TextView) itemView.findViewById(R.id.tvTanggal);
            tvPengirim = (TextView) itemView.findViewById(R.id.tvPengirim);
            cardview_item = (CardView) itemView.findViewById(R.id.cvInbox);


        }
    }
}
