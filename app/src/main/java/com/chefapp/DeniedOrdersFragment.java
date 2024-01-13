package com.chefapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chefapp.Adapters.DeniedOrderAdapter;
import com.chefapp.Models.Order;
import com.chefapp.sharedpreference.sharedpreference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeniedOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private DeniedOrderAdapter deniedOrderAdapter;
    private List<Order> deniedOrdersList;
    private sharedpreference sharedPreference;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_denied_orders, container, false);

        sharedPreference = new sharedpreference(getActivity());

        deniedOrdersList = new ArrayList<>();
        deniedOrderAdapter = new DeniedOrderAdapter(deniedOrdersList);

        recyclerView = view.findViewById(R.id.deniedOrdersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(deniedOrderAdapter);

        loadDeniedOrders();

        return view;
    }

    private void loadDeniedOrders() {
        String loggedInChefUsername = sharedPreference.getSharedprefrences(getActivity(), "username");
        Log.d("DeniedOrdersFragment", "loggedInChefUsername: " + loggedInChefUsername);

        DatabaseReference deniedOrdersRef = FirebaseDatabase.getInstance().getReference("order");

        deniedOrdersRef.orderByChild("statusDenied").equalTo("yes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deniedOrdersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    Log.d("DeniedOrdersFragment", "Order: " + order);  // Log order details

                    if (order != null && order.getDeniedChefUsername() != null && order.getDeniedChefUsername().equals(loggedInChefUsername)) {
                        order.setOrderId(snapshot.getKey());
                        deniedOrdersList.add(order);
                    }
                }

                if (deniedOrderAdapter != null) {
                    deniedOrderAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DeniedOrdersFragment", "onCancelled: " + databaseError.getMessage());
                // Handle error
            }
        });
    }
}
