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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hemontosoftware.pandemichealthkit.R;
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

public class MedicalShopRecyclerViewAdapter extends RecyclerView.Adapter<MedicalShopRecyclerViewAdapter.ViewHolder> implements Filterable {

    Context context;
    ArrayList<MedicineShopModel> medicineShopList;
    ArrayList<MedicineShopModel> medicineShopListFull;

    public MedicalShopRecyclerViewAdapter(Context context, ArrayList<MedicineShopModel> medicineShopList) {
        this.context = context;
        this.medicineShopList = medicineShopList;
        medicineShopListFull = new ArrayList<>(medicineShopList);
    }

    @NonNull
    @Override
    public MedicalShopRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_shop_layout_single_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalShopRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.shopName.setText(medicineShopList.get(position).getName());
        holder.shopAddress.setText(medicineShopList.get(position).getAddress());

        boolean isExpanded = medicineShopList.get(position).isExpanded();
        holder.expandable.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dexter.withActivity((Activity) context)
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                // permission is granted, open the camera
                                String phone = medicineShopList.get(position).getPhoneNumber();
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
        return medicineShopList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopName, shopAddress;
        Button callButton, website;
        LinearLayout expandable;
        CardView card_layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.medicine_shop_title);
            shopAddress = itemView.findViewById(R.id.medicine_shop_address);
            callButton = itemView.findViewById(R.id.call_button);
            expandable = itemView.findViewById(R.id.expandable);
            card_layout = itemView.findViewById(R.id.cardView_layout);
            website = itemView.findViewById(R.id.website_button);

            card_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MedicineShopModel shopModel = medicineShopList.get(getAdapterPosition());
                    shopModel.setExpanded(!shopModel.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
            website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Website under construction", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    @Override
    public Filter getFilter() {
        return medicalShopFilter;
    }

    private Filter medicalShopFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MedicineShopModel> medicineShopList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                medicineShopList.addAll(medicineShopListFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (MedicineShopModel item : medicineShopListFull){
                    if (item.getName().toLowerCase().contains(filterPattern) || item.getAddress().toLowerCase().contains(filterPattern)){
                        medicineShopList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = medicineShopList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            medicineShopList.clear();
            medicineShopList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
