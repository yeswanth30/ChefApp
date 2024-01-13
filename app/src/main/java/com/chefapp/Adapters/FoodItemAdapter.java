package com.chefapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chefapp.Models.FoodItem;
import com.chefapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.FoodItemDetailsViewHolder> {
    private List<FoodItem> foodItemList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onStatusClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public FoodItemAdapter(List<FoodItem> foodItemList) {
        this.foodItemList = foodItemList;
    }

    @NonNull
    @Override
    public FoodItemDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_item, parent, false);
        return new FoodItemDetailsViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemDetailsViewHolder holder, int position) {
        FoodItem foodItem = foodItemList.get(position);
        holder.bind(foodItem, holder.itemView.getContext());
    }

    @Override
    public int getItemCount() {
        return foodItemList.size();
    }

    public static class FoodItemDetailsViewHolder extends RecyclerView.ViewHolder {
        private TextView txtFoodItemDetails;
        private ImageView imgFoodItem;
        private ImageView btnDelete;
        private Button btnStatus;

        public FoodItemDetailsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            txtFoodItemDetails = itemView.findViewById(R.id.txtFoodItemName);
            imgFoodItem = itemView.findViewById(R.id.imgFoodItem);
            btnDelete = itemView.findViewById(R.id.deletebutton);
            btnStatus = itemView.findViewById(R.id.statusButton);

            // Set up click listeners
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDeleteConfirmationDialog(listener);
                }
            });

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
        }

        public void bind(FoodItem foodItem, Context context) {
            String details = "Name: " + foodItem.getName() +
                    "\nIngredients: " + foodItem.getIngredients() +
                    "\nCost Price: " + foodItem.getCostPrice() +
                    "\nSelling Price: " + foodItem.getSellingPrice() +
                    "\nTime to Cook: " + foodItem.getTimeToCook() +
                    "\nCategory: " + foodItem.getCategory() +
                    "\nSubcategory: " + foodItem.getSubcategory();

            txtFoodItemDetails.setText(details);

            btnStatus.setText((foodItem.getStatus() == 0) ? "Out of Stock" : "Available");
            int textColor = (foodItem.getStatus() == 0) ? context.getResources().getColor(R.color.out_of_stock_color) : context.getResources().getColor(R.color.available_color);
            btnStatus.setTextColor(textColor);

            if (foodItem.getImageUrl() != null && !foodItem.getImageUrl().isEmpty()) {
                imgFoodItem.setVisibility(View.VISIBLE);

                // Clear previous image (important for RecyclerView)
                imgFoodItem.setImageDrawable(null);

                // Load image from Firebase Storage using Picasso
                Picasso.get().load(foodItem.getImageUrl())
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
        private void showDeleteConfirmationDialog(final OnItemClickListener listener) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Confirmation")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (listener != null) {
                                int position = getAdapterPosition();
                                if (position != RecyclerView.NO_POSITION) {
                                    listener.onDeleteClick(position);
                                }
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Do nothing, dismiss the dialog
                        }
                    })
                    .show();
        }
    }
}