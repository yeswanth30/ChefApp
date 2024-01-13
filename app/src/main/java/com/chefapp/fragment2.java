package com.chefapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chefapp.Adapters.OrderAdapter;
import com.chefapp.Models.Order;
import com.chefapp.sharedpreference.sharedpreference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class fragment2 extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private sharedpreference sharedPreference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);

        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList);

        recyclerView = view.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(orderAdapter);

        loadFirebaseOrders();

        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onStatusClick(int position) {
                toggleOrderStatus(position);
            }

            @Override
            public void onDenyClick(int position) {
                denyOrder(position);
            }
        });

        return view;
    }

    private void loadFirebaseOrders() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("order");

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    if (order != null) {
                        order.setOrderId(snapshot.getKey());

                        // Check if the order is not completed (status is 1) and not denied
                        if (order.getStatus() != 0 && !"yes".equalsIgnoreCase(order.getStatusDenied())) {
                            orderList.add(order);
                        }
                    }
                }

                if (orderAdapter != null) {
                    orderAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to load orders: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void toggleOrderStatus(int position) {
        if (position >= 0 && position < orderList.size()) {
            Order selectedOrder = orderList.get(position);

            // Toggle the status (0 to 1 or 1 to 0)
            selectedOrder.setStatus(selectedOrder.getStatus() == 0 ? 1 : 0);

            if (orderAdapter != null) {
                orderAdapter.notifyItemChanged(position);
            }

            // Update the status, completed time, and chef username in Firebase
            DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("order");

            if (selectedOrder.getStatus() == 0) { // Order completed
                // Set the completed time in the "hh:mm:ss" format
                String completedTime = getCurrentTimeInHHmmss();
                selectedOrder.setCompletedTime(completedTime);

                // Set the chef username
                String loggedInChefUsername = sharedPreference.getSharedprefrences(getActivity(), "username");
                selectedOrder.setChefUsername(loggedInChefUsername);

                // Update the completed time and chef username in Firebase
                ordersRef.child(selectedOrder.getOrderId()).child("completedTime")
                        .setValue(completedTime);
                ordersRef.child(selectedOrder.getOrderId()).child("chefUsername")
                        .setValue(loggedInChefUsername);
            }

            // Update the status in Firebase
            ordersRef.child(selectedOrder.getOrderId()).child("status")
                    .setValue(selectedOrder.getStatus());
        }
    }


    // Helper method to get the current time in "hh:mm:ss" format
    private String getCurrentTimeInHHmmss() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }

    private void denyOrder(int position) {
        if (position >= 0 && position < orderList.size()) {
            Order selectedOrder = orderList.get(position);

            // Check if statusDenied is not null before comparing
            if (selectedOrder.getStatusDenied() != null) {
                // Toggle the statusDenied (yes to no or no to yes)
                selectedOrder.setStatusDenied(selectedOrder.getStatusDenied().equalsIgnoreCase("no") ? "yes" : "no");

                if (orderAdapter != null) {
                    orderAdapter.notifyItemChanged(position);
                }

                // Update the denied chef username in Firebase
                String loggedInChefUsername = sharedPreference.getSharedprefrences(getActivity(), "username");
                DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("order");
                ordersRef.child(selectedOrder.getOrderId()).child("statusDenied")
                        .setValue(selectedOrder.getStatusDenied());
                ordersRef.child(selectedOrder.getOrderId()).child("deniedChefUsername")
                        .setValue(loggedInChefUsername);
            } else {
                selectedOrder.setStatusDenied("no");
                denyOrder(position);
            }
        }
    }

}
