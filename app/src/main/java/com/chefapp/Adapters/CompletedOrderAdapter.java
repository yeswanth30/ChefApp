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

public class CompletedOrderAdapter extends RecyclerView.Adapter<CompletedOrderAdapter.CompletedOrderViewHolder> {

    private List<Order> completedOrdersList;

    public CompletedOrderAdapter(List<Order> completedOrdersList) {
        this.completedOrdersList = completedOrdersList;
    }

    @NonNull
    @Override
    public CompletedOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wraperfororders, parent, false);
        return new CompletedOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedOrderViewHolder holder, int position) {
        Order order = completedOrdersList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return completedOrdersList.size();
    }

    static class CompletedOrderViewHolder extends RecyclerView.ViewHolder {
        private TextView txtCompletedOrderDetails;
        private ImageView imgFoodItem;

        CompletedOrderViewHolder(View itemView) {
            super(itemView);
            txtCompletedOrderDetails = itemView.findViewById(R.id.txtOrderDetails);
            imgFoodItem = itemView.findViewById(R.id.imgFoodItem);
        }

        void bind(Order order) {
            String details = "Name: " + order.getName() +
                    "\nIngredients: " + order.getIngredients() +
                    "\nCost Price: " + order.getCostPrice() +
                    "\nSelling Price: " + order.getSellingPrice() +
                    "\nTime to Cook: " + order.getTimeToCook() +
                    "\nCategory: " + order.getCategory() +
                    "\nSubcategory: " + order.getSubcategory();

            txtCompletedOrderDetails.setText(details);

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
}
