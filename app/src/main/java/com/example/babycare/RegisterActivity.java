package com.example.babycare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.io.IOException;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_PARENT_IMAGE_REQUEST = 1;
    private static final String TAG = "RegisterActivity";
    private static final int PICK_DOCTOR_IMAGE_REQUEST = 2;

    private EditText baby_nameEditText, baby_ageEditText, placeOfBirthEditText, parentNameEditText,
            placeOfResidenceEditText, parent_phoneEditText, parent_emailEditText, parent_passwordEditText, parent_rePasswordEditText,
            doctor_nameEditText, doctor_emailEditText, doctor_passwordEditText, doctor_rePasswordEditText, specializationEditText, hospitalEditText, registrationNumberEditText;

    private RadioGroup registrationTypeRadioGroup;
    private RadioButton parentRadioButton, doctorRadioButton;
    private TextView parent_registerTextview, doctor_registerTextview, parent_alreadyHaveAccountTextView, doctor_alreadyHaveAccountTextView;

    private Button parent_uploadPictureButton, doctor_uploadPictureButton;
    private ImageView parent_profilePictureImageView, doctor_profilePictureImageView;

    private FirebaseAuth mAuth;
    private DatabaseReference parentDatabaseReference, doctorDatabaseReference;
    private StorageReference parentStorageReference, doctorStorageReference;

    private Uri parent_profilePictureUri, doctor_profilePictureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        parentDatabaseReference = FirebaseDatabase.getInstance().getReference("Parent");
        parentStorageReference = FirebaseStorage.getInstance().getReference("parent_profile_pictures");

        doctorDatabaseReference = FirebaseDatabase.getInstance().getReference("Doctor");
        doctorStorageReference = FirebaseStorage.getInstance().getReference("doctor_profile_pictures");

        baby_nameEditText = findViewById(R.id.baby_name);
        baby_ageEditText = findViewById(R.id.baby_age);
        placeOfBirthEditText = findViewById(R.id.baby_place_of_birth);
        parentNameEditText = findViewById(R.id.parent_name);
        placeOfResidenceEditText = findViewById(R.id.place_of_residence);
        parent_phoneEditText = findViewById(R.id.parent_phone);
        parent_emailEditText = findViewById(R.id.parent_email);
        parent_passwordEditText = findViewById(R.id.parent_password);
        parent_rePasswordEditText = findViewById(R.id.parent_rePassword);
        doctor_nameEditText = findViewById(R.id.doctor_name);
        doctor_emailEditText = findViewById(R.id.doctor_email);
        doctor_passwordEditText = findViewById(R.id.doctor_password);
        doctor_rePasswordEditText = findViewById(R.id.doctor_rePassword);
        specializationEditText = findViewById(R.id.Type_of_specialization);
        hospitalEditText = findViewById(R.id.Hospital);
        registrationNumberEditText = findViewById(R.id.Registration_Number);

        parent_alreadyHaveAccountTextView = findViewById(R.id.parent_alreadyHaveAccount);
        doctor_alreadyHaveAccountTextView = findViewById(R.id.doctor_alreadyHaveAccount);

        registrationTypeRadioGroup = findViewById(R.id.registrationTypeRadioGroup);
        parentRadioButton = findViewById(R.id.parentRadioButton);
        doctorRadioButton = findViewById(R.id.doctorRadioButton);

        parent_uploadPictureButton = findViewById(R.id.parent_uploadPictureButton);
        parent_registerTextview = findViewById(R.id.parent_register);

        parent_profilePictureImageView = findViewById(R.id.parent_profilePictureImageView);

        doctor_uploadPictureButton = findViewById(R.id.doctor_uploadPictureButton);
        doctor_registerTextview = findViewById(R.id.doctor_register);

        doctor_profilePictureImageView = findViewById(R.id.doctor_profilePictureImageView);

        parent_alreadyHaveAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the Login activity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        doctor_alreadyHaveAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the Login activity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        parent_uploadPictureButton.setOnClickListener(v -> chooseImageParent());

        doctor_uploadPictureButton.setOnClickListener(v -> chooseImageDoctor());

        parent_registerTextview.setOnClickListener(v -> registerParent());

        doctor_registerTextview.setOnClickListener(v -> registerDoctor());

        registrationTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.parentRadioButton) {
                // Show parent registration fields
                // Hide doctor registration fields
                showParentFields();
            } else if (checkedId == R.id.doctorRadioButton) {
                // Show doctor registration fields
                // Hide parent registration fields
                showDoctorFields();
            }
        });
    }

    private void chooseImageParent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PARENT_IMAGE_REQUEST);
    }

    private void chooseImageDoctor() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_DOCTOR_IMAGE_REQUEST);
    }

    private void showParentFields() {
        // Show parent registration fields
        // Hide doctor registration fields
        findViewById(R.id.parentLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.doctorLayout).setVisibility(View.GONE);
    }

    private void showDoctorFields() {
        // Show doctor registration fields
        // Hide parent registration fields
        findViewById(R.id.parentLayout).setVisibility(View.GONE);
        findViewById(R.id.doctorLayout).setVisibility(View.VISIBLE);
    }

    private void registerParent() {
        String email = parent_emailEditText.getText().toString().trim();
        String password = parent_passwordEditText.getText().toString().trim();
        String rePassword = parent_rePasswordEditText.getText().toString().trim();

        // Validate email and password fields
        if (email.isEmpty()) {
            parent_emailEditText.setError("Email is required");
            parent_emailEditText.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            parent_emailEditText.setError("Enter a valid email");
            parent_emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            parent_passwordEditText.setError("Password is required");
            parent_passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            parent_passwordEditText.setError("Password should be at least 6 characters long");
            parent_passwordEditText.requestFocus();
            return;
        }

        if (!password.equals(rePassword)) {
            parent_rePasswordEditText.setError("Passwords do not match");
            parent_rePasswordEditText.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userId = user.getUid();

                        DatabaseReference userRef = parentDatabaseReference.child(userId);

                        // Create a Parent object with the provided information
                        String babyName = baby_nameEditText.getText().toString().trim();
                        String babyAge = baby_ageEditText.getText().toString().trim();
                        String babyPlaceOfBirth = placeOfBirthEditText.getText().toString().trim();
                        String parentName = parentNameEditText.getText().toString().trim();
                        String placeOfResidence = placeOfResidenceEditText.getText().toString().trim();
                        String phone = parent_phoneEditText.getText().toString().trim();
                        Parent parent = new Parent(babyName, babyAge, babyPlaceOfBirth, parentName, placeOfResidence, phone);

                        // Save the Parent object in the database
                        userRef.setValue(parent);

                        // Upload profile picture if selected
                        if (parent_profilePictureUri != null) {
                            parent_uploadProfilePicture(userId);
                        }

                        // Start the ParentActivity
                        startActivity(new Intent(RegisterActivity.this, ParentActivity.class));

                        Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void registerDoctor() {
        String email = doctor_emailEditText.getText().toString().trim();
        String password = doctor_passwordEditText.getText().toString().trim();
        String rePassword = doctor_rePasswordEditText.getText().toString().trim();

        // Validate email and password fields
        if (email.isEmpty()) {
            doctor_emailEditText.setError("Email is required");
            doctor_emailEditText.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            doctor_emailEditText.setError("Enter a valid email");
            doctor_emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            doctor_passwordEditText.setError("Password is required");
            doctor_passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            doctor_passwordEditText.setError("Password should be at least 6 characters long");
            doctor_passwordEditText.requestFocus();
            return;
        }

        if (!password.equals(rePassword)) {
            doctor_rePasswordEditText.setError("Passwords do not match");
            doctor_rePasswordEditText.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userId = user.getUid();

                        DatabaseReference userRef = doctorDatabaseReference.child(userId);

                        // Create a Doctor object with the provided information
                        String doctorName = doctor_nameEditText.getText().toString().trim();
                        String specialization = specializationEditText.getText().toString().trim();
                        String hospital = hospitalEditText.getText().toString().trim();
                        String registrationNumber = registrationNumberEditText.getText().toString().trim();
                        Doctor doctor = new Doctor(doctorName, specialization, hospital, registrationNumber);

                        // Save the Doctor object in the database
                        userRef.setValue(doctor);

                        // Upload profile picture if selected
                        if (doctor_profilePictureUri != null) {
                            doctor_uploadProfilePicture(userId);
                        }

                        // Start the DoctorActivity
                        startActivity(new Intent(RegisterActivity.this, DoctorActivity.class));

                        Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void parent_uploadProfilePicture(String userId) {
        StorageReference profilePictureRef = parentStorageReference.child(userId + ".jpg");

        profilePictureRef.putFile(parent_profilePictureUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get download URL from Firebase Storage
                    profilePictureRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Save the download URL in the database
                        DatabaseReference userRef = parentDatabaseReference.child(userId);
                        userRef.child("profilePictureUrl").setValue(uri.toString());
                    });
                    Toast.makeText(RegisterActivity.this, "Profile picture uploaded successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(RegisterActivity.this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to upload profile picture", exception);
                });
    }

    private void doctor_uploadProfilePicture(String userId) {
        StorageReference profilePictureRef = doctorStorageReference.child(userId + ".jpg");

        profilePictureRef.putFile(doctor_profilePictureUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get download URL from Firebase Storage
                    profilePictureRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Save the download URL in the database
                        DatabaseReference userRef = doctorDatabaseReference.child(userId);
                        userRef.child("profilePictureUrl").setValue(uri.toString());
                    });
                    Toast.makeText(RegisterActivity.this, "Profile picture uploaded successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(RegisterActivity.this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to upload profile picture", exception);
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PARENT_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            parent_profilePictureUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), parent_profilePictureUri);
                parent_profilePictureImageView.setImageBitmap(bitmap);

                // Load the parent image using Glide with circular transformation
                Glide.with(this)
                        .load(parent_profilePictureUri)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(parent_profilePictureImageView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_DOCTOR_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            doctor_profilePictureUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), doctor_profilePictureUri);
                doctor_profilePictureImageView.setImageBitmap(bitmap);

                // Load the doctor image using Glide with circular transformation
                Glide.with(this)
                        .load(doctor_profilePictureUri)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(doctor_profilePictureImageView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
