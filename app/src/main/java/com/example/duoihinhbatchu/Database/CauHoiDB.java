package com.example.duoihinhbatchu.Database;

import androidx.annotation.NonNull;

import com.example.duoihinhbatchu.Module.CauHoi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CauHoiDB {
    private FirebaseDatabase database;
    private DatabaseReference cauHoiRef;
    private List<CauHoi> listCauHoi;

    public CauHoiDB() {
        database = FirebaseDatabase.getInstance();
        cauHoiRef = database.getReference("Cauhoi");
        listCauHoi = new ArrayList<>();
    }
    public List<CauHoi> getListCauHoi() {
        cauHoiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCauHoi.clear();
                for (DataSnapshot snap: snapshot.getChildren()){
                    CauHoi cauHoi = snap.getValue(CauHoi.class);
                    listCauHoi.add(cauHoi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return listCauHoi;
    }
}
