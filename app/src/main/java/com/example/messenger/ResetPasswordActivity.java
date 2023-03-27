package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText editTextEmailReset;
    private Button buttonReset;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initViews();
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModelObserve();

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.userForgotPassword(getEmail());
            }
        });

    }

    public static Intent newIntent(Context context) {
        return new Intent(context, ResetPasswordActivity.class);
    }

    public String getEmail() {
        return editTextEmailReset.getText().toString();
    }

    private void viewModelObserve() {
        viewModel.getToastMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    Toast.makeText(ResetPasswordActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getResetPasswordComplete().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isComplete) {
                if (isComplete) {

                    Intent intent = MainActivity.newIntent(
                            ResetPasswordActivity.this,
                            getEmail()
                    );
                    startActivity(intent);
                }
            }
        });
    }

    private void initViews() {
        editTextEmailReset = findViewById(R.id.editTextEmailReset);
        buttonReset = findViewById(R.id.button_reset);
    }



}