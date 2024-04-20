package com.example.babycare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private RadioGroup registrationTypeRadioGroup;
    private RadioButton parentRadioButton, doctorRadioButton;
    private TextView loginButton;
    private TextView createAccountTextView, forgetPasswordTextView;
    ProgressDialog progressDialog;
    EditText emailEditText, passwordEditText;

    private DatabaseReference parentDatabaseReference, doctorDatabaseReference;
    private StorageReference parentStorageReference, doctorStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        registrationTypeRadioGroup = findViewById(R.id.registrationTypeRadioGroup);
        parentRadioButton = findViewById(R.id.parentRadioButton);
        doctorRadioButton = findViewById(R.id.doctorRadioButton);
        loginButton = findViewById(R.id.btn_login);
        createAccountTextView = findViewById(R.id.btn_createAccount);
        forgetPasswordTextView = findViewById(R.id.btn_forgetPassword);
        progressDialog = new ProgressDialog(this);
        emailEditText = findViewById(R.id.email);
        TextInputLayout passwordTextInputLayout = findViewById(R.id.password2);
        passwordEditText = passwordTextInputLayout.getEditText();

        progressDialog.setMessage("Logging in...");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = registrationTypeRadioGroup.getCheckedRadioButtonId();
                if (selectedId == parentRadioButton.getId()) {
                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    loginUser(email, password);
                } else if (selectedId == doctorRadioButton.getId()) {
                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    loginUser(email, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Please select a user type", Toast.LENGTH_SHORT).show();
                }
            }
        });

        createAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the registration activity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgetPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "Forgot Password" feature
                forgotPassword();
            }
        });

        // Initialize Firebase references
        parentDatabaseReference = FirebaseDatabase.getInstance().getReference("Parent");
        parentStorageReference = FirebaseStorage.getInstance().getReference("parent_profile_pictures");

        doctorDatabaseReference = FirebaseDatabase.getInstance().getReference("Doctor");
        doctorStorageReference = FirebaseStorage.getInstance().getReference("doctor_profile_pictures");
    }

    private void loginUser(String email, String password) {

        progressDialog.show();

        // Implement Firebase authentication here
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Authentication successful
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                saveUserUidToSession(user.getUid());
                                retrieveUserDetails();
                            } else {
                                // Handle if FirebaseUser is null
                            }
                        } else {
                            // Authentication failed
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void forgotPassword() {
        // Implement the "Forgot Password" feature
        String email = emailEditText.getText().toString();

        if (email.isEmpty()) {
            emailEditText.setError("Enter your Email");
        } else {
            progressDialog.setMessage("Please wait while resetting password...");
            progressDialog.setTitle("Reset Password");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Password reset email sent. Check your email.", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Failed to send password reset email. " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void saveUserUidToSession(String uid) {
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_uid", uid);
        editor.apply();
    }

    private void retrieveUserDetails() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef;
            if (parentRadioButton.isChecked()) {
                userRef = parentDatabaseReference.child(userId);
            } else {
                userRef = doctorDatabaseReference.child(userId);
            }
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve and use user details as needed
                        // Example: String userType = dataSnapshot.child("userType").getValue(String.class);
                        if (parentRadioButton.isChecked()) {
                            startActivity(new Intent(LoginActivity.this, ParentActivity.class));
                        } else {
                            startActivity(new Intent(LoginActivity.this, DoctorActivity.class));
                        }
                        finish(); // Close LoginActivity
                    } else {
                        // Handle if the user's data is not found in the database
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        }
    }
}
