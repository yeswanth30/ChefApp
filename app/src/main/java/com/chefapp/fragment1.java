package com.chefapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chefapp.Adapters.FoodItemAdapter;
import com.chefapp.Models.FoodItem;
import com.chefapp.sharedpreference.sharedpreference;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class fragment1 extends Fragment {

    private RecyclerView recyclerView;
    private FoodItemAdapter foodItemAdapter;
    private List<FoodItem> foodItemList;
    TextView anotherTextView;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);

        ImageView btnInsertFoodItem = view.findViewById(R.id.btnInsertFoodItem);
        anotherTextView = view.findViewById(R.id.anotherTextView);
        btnInsertFoodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the activity to insert a new food item
                // Adjust the activity class accordingly
                startActivity(new Intent(getActivity(), AddFoodItemActivity.class));
            }
        });

        String userName = sharedpreference.getLoggedInName(getActivity());
        if (!userName.isEmpty()) {
            anotherTextView.setText( userName);
        }

        foodItemList = new ArrayList<>();
        foodItemAdapter = new FoodItemAdapter(foodItemList);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(foodItemAdapter);

        loadFirebaseData();

        foodItemAdapter.setOnItemClickListener(new FoodItemAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                // Handle the delete button click
                deleteFoodItem(position);
            }

            @Override
            public void onStatusClick(int position) {
                // Handle the status button click
                updateFoodItemStatus(position);
            }
        });

        return view;
    }

    private void loadFirebaseData() {
        DatabaseReference foodItemsRef = FirebaseDatabase.getInstance().getReference("foodItems");

        foodItemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodItemList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FoodItem foodItem = snapshot.getValue(FoodItem.class);
                    if (foodItem != null) {
                        // Set the Firebase key for each item
                        foodItem.setFoodItemId(snapshot.getKey());
                        foodItemList.add(foodItem);
                    }
                }

                // Notify the adapter that the data has changed
                if (foodItemAdapter != null) {
                    foodItemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to load data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteFoodItem(int position) {
        if (position >= 0 && position < foodItemList.size()) {
            FoodItem deletedItem = foodItemList.get(position);

            foodItemList.remove(position);

            if (foodItemAdapter != null) {
                foodItemAdapter.notifyItemRemoved(position);
            }

            DatabaseReference foodItemsRef = FirebaseDatabase.getInstance().getReference("foodItems");
            foodItemsRef.child(deletedItem.getFoodItemId()).removeValue();

            Toast.makeText(getActivity(), "Item deleted successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateFoodItemStatus(int position) {
        if (position >= 0 && position < foodItemList.size()) {
            FoodItem selectedFoodItem = foodItemList.get(position);

            // Update the status for the selected item
            selectedFoodItem.setStatus((selectedFoodItem.getStatus() == 0) ? 1 : 0);

            if (foodItemAdapter != null) {
                foodItemAdapter.notifyItemChanged(position);
            }

            DatabaseReference foodItemsRef = FirebaseDatabase.getInstance().getReference("foodItems");
            foodItemsRef.child(selectedFoodItem.getFoodItemId()).child("status")
                    .setValue(selectedFoodItem.getStatus());
        }
    }


}



