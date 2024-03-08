package com.example.gritfitfitnessstudio;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateProfileActivity extends AppCompatActivity {
    CircleImageView profilePic;
    EditText etName;
    EditText etUsername;
    EditText etBio;
    EditText etGender;
    EditText etWeight;
    EditText etGoal;
    Button btnSave;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference userRef;
    DatabaseReference userRefUid;
    boolean doubleBackToExitPressedOnce = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

//        Initialize ui elements and firebase components
        init();

//        set on click listener for save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               get user input
                String Email = firebaseUser.getEmail();
                String Username = etUsername.getText().toString();
                String Bio = etBio.getText().toString();
                String Name = etName.getText().toString();
                String Gender = etGender.getText().toString();
                String Goal = etGoal.getText().toString();
                String Weight = etWeight.getText().toString();

//                validate input fields
                if (!TextUtils.isEmpty(Name) || !TextUtils.isEmpty(Username) || !TextUtils.isEmpty(Gender) || !TextUtils.isEmpty(Goal) || !TextUtils.isEmpty(Weight)) {
//                    check if the username is already taken
                    userRef.child(Username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
//                                Username is not taken

//                                user model object with user details

                                UserModel userModel = new UserModel(Name, Username, firebaseUser.getUid(), Bio, Email, Gender, Weight, R.drawable.profile);


//                                update user display name
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(Name)
                                        .build();
                                firebaseUser.updateProfile(profileUpdates);

//                                save the user details under usernames node
                                userRef.child(Username).setValue(userModel);


//                                for storing details of users using UID
                                userRefUid.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists()) {

//                                save the user details under uid node
                                            userRefUid.child(firebaseUser.getUid()).setValue(userModel);

                                        }


//                                intent function to navigate to main activity

                                        Intent intent = new Intent(CreateProfileActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();  // Close the current activity to prevent going back to it

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle database error
                                    }
                                });
                            } else {
//                                username is already taken
                                Toast.makeText(CreateProfileActivity.this, "username already taken", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
//                            to handle the error

                        }
                    });

                } else {
//                   for incomplete input
                    Toast.makeText(CreateProfileActivity.this, "Enter Valid Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    finishAffinity();
                } else {
                    doubleBackToExitPressedOnce = true;
                    Toast.makeText(CreateProfileActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
                }

            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }



    private void init(){
        etUsername = findViewById(R.id.etUsername);
        etName = findViewById(R.id.etName);
        etBio = findViewById(R.id.etBio);
        etGoal = findViewById(R.id.etGoal);
        etGender = findViewById(R.id.etGender);
        etWeight = findViewById(R.id.etWeight);
        btnSave = findViewById(R.id.btnSave);
        profilePic = findViewById(R.id.profilePic);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRefUid = FirebaseDatabase.getInstance().getReference("Users_UID");
    }
}