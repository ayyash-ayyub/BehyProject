package adompo.ayyash.behay.inbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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


public class MainAdapterDetil extends RecyclerView.Adapter<MainAdapterDetil.MainHolderInbox> {

    ProgressDialog progressDialog;



    public List<ItemObjectInbox.ObjectInbox.Results> resultsList;
    public Context context;

    public MainAdapterDetil(Context context, List<ItemObjectInbox.ObjectInbox.Results> resultsList) {
        this.context = context;
        this.resultsList = resultsList;
    }



    @Override
    public MainHolderInbox onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbox, parent, false);
        MainHolderInbox mainHolder = new MainHolderInbox(view);
        return mainHolder;
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
    public void onBindViewHolder(MainHolderInbox holder, final int position) {
        holder.tvSubjek.setText(resultsList.get(position).subjek);
        holder.tvPengirim.setText(resultsList.get(position).nama);


        final int idvalue = resultsList.get(position).id;

        holder.cardview_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


//                Toast.makeText(context, resultsList.get(position).id, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, TipsSehatDetail.class);
                i.putExtra("id", idvalue);
                view.getContext().startActivity(i);



            }
        });
    }

    @Override
    public int getItemCount() {
        return this.resultsList.size();
    }




    public final static class MainHolderInbox extends RecyclerView.ViewHolder {

        ImageView imgNew;
        TextView tvSubjek, tvPengirim;
        CardView cardview_item;

        MainHolderInbox(View itemView) {
            super(itemView);

            imgNew = (ImageView)itemView.findViewById(R.id.imgNew);
            tvSubjek = (TextView) itemView.findViewById(R.id.tvSubject);
            tvPengirim = (TextView) itemView.findViewById(R.id.tvPengirim);
            cardview_item = (CardView) itemView.findViewById(R.id.cvInbox);


        }
    }
}
