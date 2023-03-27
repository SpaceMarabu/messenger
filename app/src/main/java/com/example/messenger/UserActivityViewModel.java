package com.example.messenger;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserActivityViewModel extends ViewModel {
    private final String TAG = "UserActivityViewModel";

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("users");

    public UserActivityViewModel() {

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                fUser.setValue(firebaseAuth.getCurrentUser());
            }
        });
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser curUser = mAuth.getCurrentUser();
                if (curUser == null) {
                    return;
                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    if (user == null) {
                        return;
                    }
                    if (!curUser.getUid().equals(user.getUid())) {
                        userList.add(user);
                    }
                }
                users.setValue(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void setUserOnline(boolean isOnline) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            return;
        }
        mRef.child(firebaseUser.getUid()).child("status").setValue(isOnline);
    }

    private final List<User> userList = new ArrayList<>();
    private final MutableLiveData<List<User>> users = new MutableLiveData<>();
    private final MutableLiveData<FirebaseUser> fUser = new MutableLiveData<>();

    public MutableLiveData<List<User>> getUsersList() {
        return users;
    }

    public MutableLiveData<FirebaseUser> getUser() {
        return fUser;
    }


    public void signOut() {
        mAuth.signOut();
        setUserOnline(false);
    }

}
