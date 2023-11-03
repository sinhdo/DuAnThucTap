package com.example.duanthutap.adapter;

import com.example.duanthutap.model.Oder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StaticalAdapter {
    private DatabaseReference databaseReference;
    public StaticalAdapter() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("list_oder");
    }
    public interface CalculationCallback {
        void onCalculated(double revenue);
        void onCalculationError(String errorMessage);
    }
    public void calculateRevenue(String startDate, String endDate, final CalculationCallback callback) {
        Query query = databaseReference.orderByChild("date")
                .startAt(startDate)
                .endAt(endDate);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalRevenue = 0.0;
                for (DataSnapshot transactionSnapshot : dataSnapshot.getChildren()) {
                    if (transactionSnapshot.child("status").getValue().equals("done")){
                        double transactionRevenue = transactionSnapshot.child("total").getValue(Double.class);
                        totalRevenue += transactionRevenue;
                    }
                }
                callback.onCalculated(totalRevenue);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCalculationError(databaseError.getMessage());
            }
        });
    }
}
