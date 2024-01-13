package com.chefapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chefapp.Models.Order;
import com.chefapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder> {

    private List<Order> pendingOrders;
    private static OnItemClickListener listener;

    public PendingOrderAdapter(List<Order> pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    @NonNull
    @Override
    public PendingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wraperfororders, parent, false);
        return new PendingOrderViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull PendingOrderViewHolder holder, int position) {
        Order order = pendingOrders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return pendingOrders.size();
    }

    public static class PendingOrderViewHolder extends RecyclerView.ViewHolder {
        private TextView txtOrderDetails;
        private ImageView imgFoodItem;

        public PendingOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderDetails = itemView.findViewById(R.id.txtOrderDetails);
            imgFoodItem = itemView.findViewById(R.id.imgFoodItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

        public void bind(Order order) {
            String details = "Name: " + order.getName() +
                    "\nIngredients: " + order.getIngredients() +
                    "\nCost Price: " + order.getCostPrice() +
                    "\nSelling Price: " + order.getSellingPrice() +
                    "\nTime to Cook: " + order.getTimeToCook() +
                    "\nCategory: " + order.getCategory() +
                    "\nSubcategory: " + order.getSubcategory();

            txtOrderDetails.setText(details);

            if (order.getImageUrl() != null && !order.getImageUrl().isEmpty()) {
                imgFoodItem.setVisibility(View.VISIBLE);

                // Clear previous image (important for RecyclerView)
                imgFoodItem.setImageDrawable(null);

                // Load image from Firebase Storage using Picasso
                Picasso.get().load(order.getImageUrl())
                        .into(imgFoodItem, new Callback() {
                            @Override
                            public void onSuccess() {
                                // Image loaded successfully
                            }

                            @Override
                            public void onError(Exception e) {
                                // Error loading image
                            }
                        });

            } else {
                imgFoodItem.setVisibility(View.GONE);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
