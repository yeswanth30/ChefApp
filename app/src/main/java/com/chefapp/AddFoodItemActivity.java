package com.chefapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chefapp.Models.FoodItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class AddFoodItemActivity extends AppCompatActivity {

    private EditText edtName, edtIngredients, edtCostPrice, edtSellingPrice, edtTimeToCook;
    private Spinner spinnerCategory, spinnerSubcategory;
    private Button btnAddFoodItem, btnSelectImage;
    private ImageView imgFoodItem;
    private Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    FirebaseDatabase database;
    DatabaseReference reference;

    private static final int PICK_IMAGE_REQUEST = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_item);

        FirebaseApp.initializeApp(this);

        edtName = findViewById(R.id.edtName);
        edtIngredients = findViewById(R.id.edtIngredients);
        edtCostPrice = findViewById(R.id.edtCostPrice);
        edtSellingPrice = findViewById(R.id.edtSellingPrice);
        edtTimeToCook = findViewById(R.id.edtTimeToCook);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSubcategory = findViewById(R.id.spinnerSubcategory);
        btnAddFoodItem = findViewById(R.id.btnAddFoodItem);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        imgFoodItem = findViewById(R.id.imgFoodItem);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        database = FirebaseDatabase.getInstance();
//        reference = database.getReference("Chef");
        btnAddFoodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath != null) {
                    uploadImage();
                } else {
                    showToast("Please select an image");
                }
            }
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        if (savedInstanceState != null) {
            String imageUriString = savedInstanceState.getString("selectedImageUri");
            if (imageUriString != null) {
                filePath = Uri.parse(imageUriString);
                imgFoodItem.setImageURI(filePath);
                imgFoodItem.setVisibility(View.VISIBLE);
            }
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            imgFoodItem.setImageURI(filePath);
            imgFoodItem.setVisibility(View.VISIBLE);
        }
    }

    private void uploadImage() {
        if (filePath != null) {
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Fixed path in Firebase Storage - replace with your desired path
            String name = getFileNameFromUri(filePath);
            StorageReference ref = storageReference.child("fooditems").child(name);
            Log.e("filepath", "File path" + filePath);
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully
                        progressDialog.dismiss();
                        Toast.makeText(AddFoodItemActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();

                        // Retrieve the download URL after successful upload
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();

                            addFoodItemWithImage(downloadUrl);
                        }).addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Log.e("FirebaseStorage", "Failed to get download URL", e);
                            Toast.makeText(AddFoodItemActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Log.e("FirebaseStorage", "Failed " + e.getMessage(), e);
                        Toast.makeText(AddFoodItemActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(snapshot -> {
                        // Progress Listener for loading percentage on the dialog box
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    });
        } else {
            Log.e("FirebaseStorage", "File path is null");
            Toast.makeText(this, "File path is null", Toast.LENGTH_SHORT).show();
        }
    }


    private void addFoodItemWithImage(String downloadUrl) {
        String name = edtName.getText().toString();
        String ingredients = edtIngredients.getText().toString();
        String costPriceText = "₹" + edtCostPrice.getText().toString();
        String sellingPriceText = "₹" + edtSellingPrice.getText().toString();
        String timeToCookText = edtTimeToCook.getText().toString();
        String category = spinnerCategory.getSelectedItem().toString();
        String subcategory = spinnerSubcategory.getSelectedItem().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(ingredients) || TextUtils.isEmpty(costPriceText) ||
                TextUtils.isEmpty(sellingPriceText) || TextUtils.isEmpty(timeToCookText)) {
            showToast("Please fill in all fields");
            return;
        }

        FoodItem foodItem = new FoodItem(name, ingredients, costPriceText, sellingPriceText,
                timeToCookText, category, subcategory, downloadUrl, 1);
        reference = database.getReference("foodItems");

        reference.child(name).setValue(foodItem);


        // Your database insertion logic here
        // Use the 'foodItem' object to save data to your Firebase Realtime Database or Firestore
        showToast("Food item added successfully");
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (filePath != null) {
            outState.putString("selectedImageUri", filePath.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String imageUriString = savedInstanceState.getString("selectedImageUri");
        if (imageUriString != null) {
            filePath = Uri.parse(imageUriString);
            imgFoodItem.setImageURI(filePath);
            imgFoodItem.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("Range")
    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }
}
