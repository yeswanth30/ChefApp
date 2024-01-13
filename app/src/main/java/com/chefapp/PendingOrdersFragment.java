package com.chefapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chefapp.Adapters.OrderAdapter;
import com.chefapp.Adapters.PendingOrderAdapter;
import com.chefapp.Models.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class PendingOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private PendingOrderAdapter pendingOrderAdapter;
    private List<Order> pendingOrders;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_orders, container, false);

        pendingOrders = new ArrayList<>();
        pendingOrderAdapter = new PendingOrderAdapter(pendingOrders);

        recyclerView = view.findViewById(R.id.recyclerViewPending);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(pendingOrderAdapter);

        loadPendingOrders();

        return view;
    }

    private void loadPendingOrders() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("order");

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pendingOrders.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    if (order != null && order.getStatus() == 1) {
                        order.setOrderId(snapshot.getKey());

                        if (!"yes".equalsIgnoreCase(order.getStatusDenied())) {
                            pendingOrders.add(order);
                        }
                    }
                }

                if (pendingOrderAdapter != null) {
                    pendingOrderAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });
    }
}




