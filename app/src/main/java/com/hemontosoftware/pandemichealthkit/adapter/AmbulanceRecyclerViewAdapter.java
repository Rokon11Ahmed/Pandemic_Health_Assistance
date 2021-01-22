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
import com.hemontosoftware.pandemichealthkit.model.AmbulanceModel;
import com.hemontosoftware.pandemichealthkit.model.ContactTracingModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class AmbulanceRecyclerViewAdapter extends RecyclerView.Adapter<AmbulanceRecyclerViewAdapter.ViewHolder> implements Filterable {
    Context context;
    ArrayList<AmbulanceModel> ambulanceList;
    ArrayList<AmbulanceModel> ambulanceListFull;

    public AmbulanceRecyclerViewAdapter(Context context, ArrayList<AmbulanceModel> ambulanceList) {
        this.context = context;
        this.ambulanceList = ambulanceList;
        ambulanceListFull = new ArrayList<>(ambulanceList);
    }

    @NonNull
    @Override
    public AmbulanceRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.m_ambulance_layout_single_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmbulanceRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.shopName.setText(ambulanceList.get(position).getName());
        holder.shopAddress.setText(ambulanceList.get(position).getAddress());

        boolean isExpanded = ambulanceList.get(position).isExpanded();
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
                                String phone = ambulanceList.get(position).getPhoneNumber();
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
        return ambulanceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopName, shopAddress;
        Button callButton;
        LinearLayout expandable;
        CardView card_layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.ambulance_title);
            shopAddress = itemView.findViewById(R.id.ambulance_address);
            callButton = itemView.findViewById(R.id.call_button);
            expandable = itemView.findViewById(R.id.expandable);
            card_layout = itemView.findViewById(R.id.cardView_layout);

            card_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AmbulanceModel ambulanceModel = ambulanceList.get(getAdapterPosition());
                    ambulanceModel.setExpanded(!ambulanceModel.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return ambulanceFilter;
    }

    private Filter ambulanceFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<AmbulanceModel> ambulanceList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                ambulanceList.addAll(ambulanceListFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (AmbulanceModel item : ambulanceListFull){
                    if (item.getName().toLowerCase().contains(filterPattern) || item.getAddress().toLowerCase().contains(filterPattern)){
                        ambulanceList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = ambulanceList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ambulanceList.clear();
            ambulanceList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
