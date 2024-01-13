package com.chefapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.chefapp.sharedpreference.sharedpreference;

public class contentmain extends AppCompatActivity {
    ImageView myImageView;
    sharedpreference sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        myImageView = findViewById(R.id.myImageView);
        sharedPreferences = new sharedpreference(this);

        myImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoggedIn()) {
                    redirectToMainActivity();
                } else {
                    redirectToLoginPage();
                }
            }
        });
    }

    private boolean isLoggedIn() {
        return sharedPreferences.isFirstTimeLaunch();
    }

    private void redirectToLoginPage() {
        Intent intent = new Intent(contentmain.this, LoginActivity.class);
        startActivity(intent);
    }

    private void redirectToMainActivity() {
        Intent intent = new Intent(contentmain.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
