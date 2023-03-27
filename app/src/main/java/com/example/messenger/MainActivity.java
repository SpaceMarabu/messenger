package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private String email;
    private String password;

    private EditText editTextPassword;
    private EditText editTextEmailAddress;
    private Button buttonLogin;
    private TextView registerTextView;
    private TextView forgotPasswordTextView;

    private final String TAG =  "MainActivity";

    private LoginViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_views();

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModelObserve();
        setOnClickListeners();

        if (getIntent().getStringExtra("email") != null) {
            editTextEmailAddress.setText(getIntent().getStringExtra("email"));
        }
    }

    private void setOnClickListeners() {
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = RegistrationActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ResetPasswordActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = editTextEmailAddress.getText().toString().trim();
                password = editTextPassword.getText().toString();
                viewModel.userLogin(email, password);
            }
        });
    }
    private void viewModelObserve() {
        viewModel.getToastMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getUserFB().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Intent intent = UserActivity.newIntent(
                            MainActivity.this,
                            firebaseUser.getUid()
                    );
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void init_views() {
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        buttonLogin = findViewById(R.id.buttonLogin);
        registerTextView = findViewById(R.id.registerTextView);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
    }

    public static Intent newIntent(Context context, String email) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("email", email);
        return intent;
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }
}