package com.chefapp.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.chefapp.CompletedOrdersFragment;
import com.chefapp.DeniedOrdersFragment;
import com.chefapp.PendingOrdersFragment;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    public TabAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }
    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PendingOrdersFragment();
            case 1:
                return new CompletedOrdersFragment();
            case 2:
                return new DeniedOrdersFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3; // Number of tabs
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Pending";
            case 1:
                return "Completed";
            case 2:
                return "Denied";
            default:
                return null;
        }
    }
}
