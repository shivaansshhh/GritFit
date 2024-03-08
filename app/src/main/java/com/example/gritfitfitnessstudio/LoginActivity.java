package com.example.gritfitfitnessstudio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText emailEt, passEt;
    Button loginBtn;
    CheckBox checkBox;
    ProgressBar progressBar;
    TextView register;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

//       checkbox to show password

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    passEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    passEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

//       intent function to go to register activity when clicked on register

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


//        login button functionality and for authentication

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                storing value to variable
                String email = emailEt.getText().toString();
                String pass = passEt.getText().toString();

//                checking if values are empty or not
                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(pass)){  //if values are not empty
//                    set visibility of progress bar to visible
                    progressBar.setVisibility(View.VISIBLE);
//                    checking user login details
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
//                                if details match, go to main activity
                                sendToMain();
//                                set visibility to invisible after login successful
                                progressBar.setVisibility(View.INVISIBLE);
                            }else{
//                                storing error message in a variable
                                String error = task.getException().getMessage();
                                progressBar.setVisibility(View.INVISIBLE);
//                                displaying error message
                                Toast.makeText(LoginActivity.this, "Error"+error, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }else {
//                    values are empty
//                    if details are not filled display a message
                    Toast.makeText(LoginActivity.this, "Enter Details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //    function for Intent to go to Main activity
    private void sendToMain(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //    function to initialize values
    private void init(){
        emailEt = findViewById(R.id.login_email_et);
        passEt = findViewById(R.id.login_pass_et);
        loginBtn = findViewById(R.id.btn_to_login);
        register = findViewById(R.id.registerYourself);
        checkBox = findViewById(R.id.login_checkbox);
        progressBar = findViewById(R.id.progressBar_login);
        mAuth = FirebaseAuth.getInstance();

    }


    //    to check if user is already logged in or not, if already logged in send him to main activity
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!= null){
            sendToMain();
        }
    }
}