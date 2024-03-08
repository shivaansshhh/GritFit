package com.example.gritfitfitnessstudio;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText emailEt, passEt, confirmPassEt;
    Button registerBtn;
    TextView login;
    CheckBox checkBox;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();


//       checkbox to show password

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirmPassEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirmPassEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


//        Register button functionality and to register user using firebase (email and pass)
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString();
                String pass = passEt.getText().toString();
                String confirm_password = confirmPassEt.getText().toString();

//                checking if it is empty or not!
                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(pass) || !TextUtils.isEmpty(confirm_password)) {
//                    checking if password and confirm pass same
                    if (pass.equals(confirm_password)) {
                        progressBar.setVisibility(View.VISIBLE);
//                        registering user
                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
//                                    if user is created successfully send to main activity
                                    sendToCreateProfile();
                                    progressBar.setVisibility(View.INVISIBLE);
                                } else {
//                                    if user is not created successfully
//                                    storing error in a variable to display it
                                    progressBar.setVisibility(View.INVISIBLE);
                                    String error = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, "Error" + error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
//                        if password and confirm password does not match
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(RegisterActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    }
                }else{
//                    if field are empty
                    Toast.makeText(RegisterActivity.this, "Please fill all field", Toast.LENGTH_SHORT).show();
                }
            }

        });


//        intent function to go to the login activity when clicked on login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });



    }

    private void sendToCreateProfile(){
        Intent intent= new Intent(RegisterActivity.this, CreateProfileActivity.class);
        startActivity(intent);
        finish();

    }

//    function for intent to go to Main Activity
    private void sendToMain(){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void init(){
        emailEt = findViewById(R.id.register_email_et);
        passEt = findViewById(R.id.register_pass_et);
        confirmPassEt = findViewById(R.id.register_confirm_pass_et);
        registerBtn = findViewById(R.id.register_btn);
        login = findViewById(R.id.goToLogin);
        checkBox = findViewById(R.id.register_checkbox);
        progressBar = findViewById(R.id.progressBar_register);
        mAuth = FirebaseAuth.getInstance();
    }

//    to check if user is already logged in or not
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            sendToMain();
        }
    }
}