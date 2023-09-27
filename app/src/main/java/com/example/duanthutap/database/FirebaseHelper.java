package com.example.duanthutap.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    private DatabaseReference databaseReference;

    public FirebaseHelper(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getUsersRef(){
        return databaseReference.child("user");
    }
}
