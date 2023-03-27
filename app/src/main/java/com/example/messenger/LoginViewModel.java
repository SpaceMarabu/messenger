package com.example.messenger;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginViewModel extends ViewModel {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dRef = database.getReference("users");

    public LoginViewModel() {
    }

    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private final MutableLiveData<FirebaseUser> userFB = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registrationComplete = new MutableLiveData<>();
    private final MutableLiveData<Boolean> resetPasswordComplete = new MutableLiveData<>();


    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }

    public MutableLiveData<FirebaseUser> getUserFB() {
        return userFB;
    }

    public MutableLiveData<Boolean> getRegistrationComplete() {
        return registrationComplete;
    }

    public MutableLiveData<Boolean> getResetPasswordComplete() {
        return resetPasswordComplete;
    }

    public void userForgotPassword(String email) {
        resetPasswordComplete.setValue(false);
        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        toastMessage.setValue("Email sent");
                        resetPasswordComplete.setValue(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        toastMessage.setValue(e.getMessage());
                    }
                });
    }

    public void userLogin(String email, String password) {
        if (password.equals("")) {
            toastMessage.setValue("Введите пароль");
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            userFB.setValue(authResult.getUser());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            toastMessage.setValue(e.getMessage());
                        }
                    });
        }
    }
    public void userCreate(String email, String password,
                           String name, String lastName, String age, Context context) {
        registrationComplete.setValue(false);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        toastMessage.setValue("Success registration");
                        registrationComplete.setValue(true);
                        userFB.setValue(authResult.getUser());
                        if (authResult.getUser() == null) {
                            return;
                        }
                        String uId = authResult.getUser().getUid();
                        User user = new User(uId, name, lastName, age, false);
                        dRef.child(uId).setValue(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        toastMessage.setValue(e.getMessage());
                    }
                });
    }

}
