package com.example.duanthutap.database;

import com.google.firebase.database.DataSnapshot;

public class FirebaseRole {
    public static boolean isUserAdmin(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            Boolean isAdmin = dataSnapshot.child("role").getValue(Boolean.class);
            return isAdmin != null && isAdmin;
        }
        return false;
    }
}
