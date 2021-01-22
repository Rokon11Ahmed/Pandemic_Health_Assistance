package com.hemontosoftware.pandemichealthkit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hemontosoftware.pandemichealthkit.R;
import com.hemontosoftware.pandemichealthkit.model.ContactTracingModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContactTracingRecyclerViewAdapter extends RecyclerView.Adapter<ContactTracingRecyclerViewAdapter.ViewHolder> implements Filterable {
    Context context;
    ArrayList<ContactTracingModel> contactTracingList;
    ArrayList<ContactTracingModel> contactTracingListFull;

    public ContactTracingRecyclerViewAdapter(Context context, ArrayList<ContactTracingModel> contactTracingList) {
        this.context = context;
        this.contactTracingList = contactTracingList;
        contactTracingListFull = new ArrayList<>(contactTracingList);
    }

    @NonNull
    @Override
    public ContactTracingRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.m_contact_tracing_layout_single_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactTracingRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.name.setText(contactTracingList.get(position).getName());
        holder.age.setText("Age: "+contactTracingList.get(position).getAge());
        holder.address.setText(contactTracingList.get(position).getAddress());

        String oldstring = contactTracingList.get(position).getTime();
        Date olddate = null;
        try {
            olddate = new SimpleDateFormat("dd-MM-yyyy").parse(oldstring);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String newDate = new SimpleDateFormat("dd").format(olddate);
        String newMonthYear = new SimpleDateFormat("MMM yyyy").format(olddate);
        holder.day.setText(newDate);
        holder.month.setText(newMonthYear);
    }

    @Override
    public int getItemCount() {
        return contactTracingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, age, address, day, month;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.blood_donor_name);
            age = itemView.findViewById(R.id.blood_donor_age);
            address = itemView.findViewById(R.id.blood_donor_address);
            day = itemView.findViewById(R.id.day_textview);
            month = itemView.findViewById(R.id.month_year_textview);
        }
    }

    @Override
    public Filter getFilter() {
        return contactTracingFilter;
    }

    private Filter contactTracingFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ContactTracingModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(contactTracingListFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ContactTracingModel item : contactTracingListFull){
                    if (item.getName().toLowerCase().contains(filterPattern) || item.getAge().toLowerCase().contains(filterPattern)
                    || item.getAddress().toLowerCase().contains(filterPattern) || item.getTime().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contactTracingList.clear();
            contactTracingList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
