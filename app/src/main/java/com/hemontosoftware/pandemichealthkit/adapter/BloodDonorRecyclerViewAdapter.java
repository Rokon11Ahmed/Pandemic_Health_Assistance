package com.hemontosoftware.pandemichealthkit.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hemontosoftware.pandemichealthkit.R;
import com.hemontosoftware.pandemichealthkit.model.BloodDonorModel;
import com.hemontosoftware.pandemichealthkit.model.ContactTracingModel;
import com.hemontosoftware.pandemichealthkit.model.MedicineShopModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class BloodDonorRecyclerViewAdapter extends RecyclerView.Adapter<BloodDonorRecyclerViewAdapter.ViewHolder> implements Filterable {

    Context context;
    ArrayList<BloodDonorModel> bloodDonorList;
    ArrayList<BloodDonorModel> bloodDonorListFull;

    public BloodDonorRecyclerViewAdapter(Context context, ArrayList<BloodDonorModel> bloodDonorList) {
        this.context = context;
        this.bloodDonorList = bloodDonorList;
        bloodDonorListFull = new ArrayList<>(bloodDonorList);
    }

    @NonNull
    @Override
    public BloodDonorRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.m_blood_donor_layout_single_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodDonorRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.name.setText(bloodDonorList.get(position).getDonorName());
        holder.age.setText("Age: "+bloodDonorList.get(position).getAge());
        holder.address.setText(bloodDonorList.get(position).getAddress());
        holder.bloodGroup.setText(bloodDonorList.get(position).getBloodGroup());

        boolean isExpanded = bloodDonorList.get(position).isExpanded();
        holder.expandable.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dexter.withActivity((Activity) context)
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                // permission is granted, open the camera
                                String phone = bloodDonorList.get(position).getPhoneNumber();
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                context.startActivity(intent);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                // check for permanent denial of permission
                                if (response.isPermanentlyDenied()) {
                                    // navigate user to app settings
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bloodDonorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, age, address, bloodGroup;
        Button phoneNumber;
        CardView cardLayout;
        LinearLayout expandable;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.blood_donor_name);
            age = itemView.findViewById(R.id.blood_donor_age);
            address = itemView.findViewById(R.id.blood_donor_address);
            bloodGroup = itemView.findViewById(R.id.blood_group);
            phoneNumber = itemView.findViewById(R.id.call_button);
            cardLayout = itemView.findViewById(R.id.cardView_layout);
            expandable = itemView.findViewById(R.id.expandable);


            cardLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BloodDonorModel donorModel = bloodDonorList.get(getAdapterPosition());
                    donorModel.setExpanded(!donorModel.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
    @Override
    public Filter getFilter() {
        return bloodDonorFilter;
    }

    private Filter bloodDonorFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<BloodDonorModel> bloodDonorList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                bloodDonorList.addAll(bloodDonorListFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (BloodDonorModel item : bloodDonorListFull){
                    if (item.getDonorName().toLowerCase().contains(filterPattern) || item.getAge().toLowerCase().contains(filterPattern)
                            || item.getAddress().toLowerCase().contains(filterPattern) || item.getBloodGroup().toLowerCase().contains(filterPattern)){
                        bloodDonorList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = bloodDonorList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            bloodDonorList.clear();
            bloodDonorList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
