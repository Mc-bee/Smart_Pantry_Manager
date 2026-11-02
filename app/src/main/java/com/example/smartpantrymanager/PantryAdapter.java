package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.VH> {

    public interface Listener {
        void onEdit(PantryItem item);
        void onDelete(PantryItem item);
    }

    private List<PantryItem> items;
    private final Listener listener;

    public PantryAdapter(List<PantryItem> items, Listener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void setItems(List<PantryItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView name, details;
        Button edit, delete;

        VH(View v) {
            super(v);
            name = v.findViewById(R.id.tvName);
            details = v.findViewById(R.id.tvDetails);
            edit = v.findViewById(R.id.btnEdit);
            delete = v.findViewById(R.id.btnDelete);
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH h, int position) {
        PantryItem i = items.get(position);
        h.name.setText(i.getName());
        String exp = (i.getExpiry() == null || i.getExpiry().isEmpty())
                ? "" : "  |  Expires " + i.getExpiry();
        h.details.setText(i.getQuantity() + " " + i.getUnit() + exp);
        h.edit.setOnClickListener(v -> listener.onEdit(i));
        h.delete.setOnClickListener(v -> listener.onDelete(i));
    }

    @Override
    public int getItemCount() { return items.size(); }
}
