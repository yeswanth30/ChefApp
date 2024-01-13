package com.chefapp;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class LogoutDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Yes, handle the logout
                        logout();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked No, dismiss the dialog
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    private void logout() {
        // Implement your logout logic here
        // For example, clear SharedPreferences and notify the target fragment
        SharedPreferences preferences = getActivity().getSharedPreferences("clearPreferences", MODE_PRIVATE);
        preferences.edit().clear().apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();

        // Notify the target fragment (fragment4) that logout is successful
        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, null);
    }
}
