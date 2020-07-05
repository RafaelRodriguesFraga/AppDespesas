package com.rafaelfraga.appdespesas.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConfig {

    private static FirebaseAuth mAuth;
    private static DatabaseReference mReference;

    public static FirebaseAuth getFirebaseAuth() {
        if(mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
        return mAuth;
    }

    public static DatabaseReference getFirebaseReference() {
        if(mReference == null) {
            mReference = FirebaseDatabase.getInstance().getReference();
        }

        return mReference;
    }
}
