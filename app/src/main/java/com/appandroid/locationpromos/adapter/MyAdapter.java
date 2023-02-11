package com.appandroid.locationpromos.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.appandroid.locationpromos.R;
import com.appandroid.locationpromos.addCompany;
import com.appandroid.locationpromos.database.dbhelper;
import com.appandroid.locationpromos.models.BrandModel;
import com.appandroid.locationpromos.utils.BitmapConvert;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {


    ArrayList<BrandModel> mDataList;
    LayoutInflater layoutInflater;
    Context context;
    private ValueFilter valueFilter;
    private ArrayList<BrandModel> mStringFilterList;
    dbhelper dbGest;

    public MyAdapter(Context context, ArrayList<BrandModel> data, dbhelper db) {

        layoutInflater = LayoutInflater.from(context);
        this.mDataList = data;
        this.mStringFilterList = data;
        this.context = context;
        this.dbGest = db;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.listitem, parent, false);

        MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final BrandModel brandModel = mDataList.get(position);
        holder.setData(brandModel, position);
        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context.getApplicationContext(), "ff", Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView bandnametxt, longitudetxt, latitudetxt, txtdescription, txtnotification;
        ImageView imagalogo;
        Button btndelete;
        CardView rootCardView;


        public MyViewHolder(View itemView) {
            super(itemView);


            bandnametxt = (TextView) itemView.findViewById(R.id.bandnametxt);
            longitudetxt = (TextView) itemView.findViewById(R.id.longitudetxt);
            latitudetxt = (TextView) itemView.findViewById(R.id.latitudetxt);
            txtdescription = (TextView) itemView.findViewById(R.id.txtdescription);
            txtnotification = (TextView) itemView.findViewById(R.id.txtnotification);

            imagalogo = (ImageView) itemView.findViewById(R.id.imagelogo);
            rootCardView = (CardView) itemView.findViewById(R.id.rootCardView);
            btndelete = (Button) itemView.findViewById(R.id.btndelete);


        }


        public void setData(BrandModel brandModel, int position) {

            Bitmap bitmap = BitmapConvert.getImage(brandModel.getBrand_image());
            imagalogo.setImageBitmap(bitmap);
            this.bandnametxt.setText(brandModel.getBrand_title());
            this.longitudetxt.setText("Long: "+brandModel.getLogitude());
            this.latitudetxt.setText("Lat: "+brandModel.getLatitude());
            this.txtdescription.setText(brandModel.getBrand_desc());
            this.txtnotification.setText(brandModel.getNotification_text());


        }
    }


    @Override
    public Filter getFilter() {
        if (valueFilter == null) {

            valueFilter = new ValueFilter();
        }

        return valueFilter;
    }

    private class ValueFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<BrandModel> filterList = new ArrayList<BrandModel>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getBrand_title().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        BrandModel brand = new BrandModel();
                        brand.setBrand_title(mStringFilterList.get(i).getBrand_title());
                        brand.setRetailerId(mStringFilterList.get(i).getRetailerId());
                        brand.setBrand_image(mStringFilterList.get(i).getBrand_image());
                        brand.setBrand_desc(mStringFilterList.get(i).getBrand_desc());
                        brand.setLatitude(mStringFilterList.get(i).getLatitude());
                        brand.setLogitude(mStringFilterList.get(i).getLogitude());
                        brand.setNotification_text(mStringFilterList.get(i).getNotification_text());

                        filterList.add(brand);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            mDataList = (ArrayList<BrandModel>) results.values;
            notifyDataSetChanged();
        }

    }
}



