package com.chefapp;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chefapp.Adapters.CompletedOrderAdapter;
import com.chefapp.Models.Order;
import com.chefapp.sharedpreference.sharedpreference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;




public class CompletedOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private CompletedOrderAdapter completedOrderAdapter;
    private List<Order> completedOrdersList;
    private TextView txtTotalSellingPrice;
    private double totalSellingPrice = 0.0;
    private sharedpreference sharedPreference;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_orders, container, false);

        sharedPreference = new sharedpreference(getActivity());

        completedOrdersList = new ArrayList<>();
        completedOrderAdapter = new CompletedOrderAdapter(completedOrdersList);

        txtTotalSellingPrice = view.findViewById(R.id.txtTotalSellingPrice);
        txtTotalSellingPrice.setText(String.format("Total Selling Price: ₹%.2f", totalSellingPrice));

        recyclerView = view.findViewById(R.id.completedOrdersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(completedOrderAdapter);

        loadCompletedOrders();

        return view;
    }

    private void loadCompletedOrders() {
        String loggedInChefUsername = sharedPreference.getSharedprefrences(getActivity(), "username");

        DatabaseReference completedOrdersRef = FirebaseDatabase.getInstance().getReference("order");

        completedOrdersRef.orderByChild("status").equalTo(0).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                completedOrdersList.clear();
                totalSellingPrice = 0.0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    if (order != null && order.getChefUsername() != null
                            && order.getChefUsername().equals(loggedInChefUsername)) {
                        order.setOrderId(snapshot.getKey());
                        completedOrdersList.add(order);

                        totalSellingPrice += Double.parseDouble(order.getSellingPrice().replaceAll("[^\\d.]", ""));
                        Log.d("Order", "Order added. Order chefUsername: " + order.getChefUsername());
                    }
                }

                Log.d("Order", "Completed Orders List Size: " + completedOrdersList.size());

                txtTotalSellingPrice.setText(String.format("Total Income : ₹%.2f", totalSellingPrice));

                if (completedOrderAdapter != null) {
                    completedOrderAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Order", "Error loading completed orders: " + databaseError.getMessage());
            }
        });
    }
}






