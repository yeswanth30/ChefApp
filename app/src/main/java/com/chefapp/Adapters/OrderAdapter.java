package com.chefapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chefapp.Models.Order;
import com.chefapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private static OnItemClickListener listener;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    public void setOnItemClickListener(OrderAdapter.OnItemClickListener listener) {
        this.listener = listener;

    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order, holder.itemView.getContext());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView txtOrderDetails;
        private Button btnStatus, btnDeny;
        private ImageView imgFoodItem;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderDetails = itemView.findViewById(R.id.txtOrderDetails);
            btnStatus = itemView.findViewById(R.id.btnStatus);
            btnDeny = itemView.findViewById(R.id.btnDeny);
            imgFoodItem = itemView.findViewById(R.id.imgFoodItem);

            btnStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onStatusClick(position);
                        }
                    }
                }
            });

            btnDeny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDenyClick(position);
                        }
                    }
                }
            });

        }

        public void bind(Order order, Context context) {
            String details = "Name: " + order.getName() +
                    "\nIngredients: " + order.getIngredients() +
                    "\nCost Price: " + order.getCostPrice() +
                    "\nSelling Price: " + order.getSellingPrice() +
                    "\nTime to Cook: " + order.getTimeToCook() +
                    "\nCategory: " + order.getCategory() +
                    "\nSubcategory: " + order.getSubcategory();

            txtOrderDetails.setText(details);
            btnStatus.setText(order.getStatus() == 0 ? "Completed" : "Pending");
            btnDeny.setText("yes".equalsIgnoreCase(order.getStatusDenied()) ? "Denied" : "Deny");

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
        void onStatusClick(int position);
        void onDenyClick(int position);
    }
}
