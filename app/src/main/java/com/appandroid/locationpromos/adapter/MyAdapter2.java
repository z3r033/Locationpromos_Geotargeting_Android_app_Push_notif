package com.appandroid.locationpromos.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.appandroid.locationpromos.R;
import com.appandroid.locationpromos.RetailerActivity;
import com.appandroid.locationpromos.UserActivity;
import com.appandroid.locationpromos.addCompany;
import com.appandroid.locationpromos.database.dbhelper;
import com.appandroid.locationpromos.models.BrandModel;
import com.appandroid.locationpromos.models.notification_user;
import com.appandroid.locationpromos.utils.BitmapConvert;
import com.appandroid.locationpromos.utils.SharedPref;

import java.util.ArrayList;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder> implements Filterable {


    ArrayList<BrandModel> mDataList;
    ArrayList<notification_user> mDataList2;
    LayoutInflater layoutInflater;
    Context context;
    private ValueFilter valueFilter;
    private ArrayList<BrandModel> mStringFilterList;
    dbhelper dbGest;

    public MyAdapter2(Context context, ArrayList<BrandModel> data, ArrayList<notification_user> data2, dbhelper db) {

        layoutInflater = LayoutInflater.from(context);
        this.mDataList = data;
        this.mStringFilterList = data;
        this.context = context;
        this.dbGest = db;
        this.mDataList2=data2;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.listitemuser, parent, false);

        MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final BrandModel brandModel = mDataList.get(position);
        int user_id = SharedPref.readSharedSettingint(context,"user_id",0);
        notification_user test = new notification_user(mDataList.get(position).getBrand_id(),user_id);

        if(mDataList2.contains(test)){
            holder.btnsubscribe.setText("unsubscribe");

        }
        holder.setData(brandModel, position);
        holder.btnsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.btnsubscribe.getText().equals("unsubscribe")){
                    dbhelper dbh = new dbhelper(context.getApplicationContext());
                    int user_id = SharedPref.readSharedSettingint(context, "user_id", 0);
                    dbh.deletenotification(brandModel.getBrand_id(), user_id);
                    mDataList2.remove(test);
                    holder.btnsubscribe.setText("Subscribe");

                }else {
                    ContentValues values = new ContentValues();
                    int user_id = SharedPref.readSharedSettingint(context, "user_id", 0);
                    values.put("brandId", brandModel.getBrand_id());
                    values.put("userId", user_id);
                    dbhelper dbh = new dbhelper(context.getApplicationContext());
                    dbh.Insert(values, "user_notification");
                    mDataList2.add(test);
                    holder.btnsubscribe.setText("unsubscribe");
                 //   Toast.makeText(context.getApplicationContext(), "subscribe", Toast.LENGTH_LONG).show();


                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nombranduser, longitudetxtuser, latitudetxtuser;
        ImageView imagalogo;
        Button btnsubscribe;
        CardView rootCardView;


        public MyViewHolder(View itemView) {
            super(itemView);


            nombranduser = (TextView) itemView.findViewById(R.id.nombranduser);
            longitudetxtuser = (TextView) itemView.findViewById(R.id.longitudetxtuser);
            latitudetxtuser = (TextView) itemView.findViewById(R.id.latitudetxtuser);

            imagalogo = (ImageView) itemView.findViewById(R.id.bandimageuser);
            rootCardView = (CardView) itemView.findViewById(R.id.rootCardView);
            btnsubscribe = (Button) itemView.findViewById(R.id.btnsubscribe);


        }


        public void setData(BrandModel brandModel, int position) {

            Bitmap bitmap = BitmapConvert.getImage(brandModel.getBrand_image());
            imagalogo.setImageBitmap(bitmap);
            this.nombranduser.setText(brandModel.getBrand_title());
            this.longitudetxtuser.setText("Long: "+brandModel.getLogitude());
            this.latitudetxtuser.setText("Lat: "+brandModel.getLatitude());

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



