package com.chefapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.chefapp.Adapters.TabAdapter;
import com.google.android.material.tabs.TabLayout;

public class fragment3 extends Fragment {

    private TabAdapter tabAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        tabAdapter = new TabAdapter(getChildFragmentManager());

        tabAdapter.addFragment(new PendingOrdersFragment(), "Pending Orders");
        tabAdapter.addFragment(new CompletedOrdersFragment(), "Completed Orders");
        tabAdapter.addFragment(new DeniedOrdersFragment(), "Denied Orders");

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
