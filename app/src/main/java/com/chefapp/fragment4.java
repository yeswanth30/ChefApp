package com.chefapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class fragment4 extends Fragment {

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4, container, false);

        showLogoutDialog();

        return view;
    }

    private void showLogoutDialog() {
        LogoutDialogFragment dialogFragment = new LogoutDialogFragment();
        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.show(getFragmentManager(), "logout_dialog");
    }
}
