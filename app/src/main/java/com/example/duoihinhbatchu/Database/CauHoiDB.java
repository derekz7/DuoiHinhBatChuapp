package com.example.duoihinhbatchu.Database;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.duoihinhbatchu.Models.CauHoi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class CauHoiDB {
    private FirebaseDatabase database;
    private DatabaseReference cauHoiRef;
    private StorageReference storeRef;
    private List<CauHoi> listCauHoi;

    public CauHoiDB() {
        database = FirebaseDatabase.getInstance();
        cauHoiRef = database.getReference("Cauhoi");
        storeRef = FirebaseStorage.getInstance().getReference();
        listCauHoi = new ArrayList<>();
    }

    public void upLoadCauHoi(Activity activity,int id, Uri uri, String dapAn) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        StorageReference fileRef = storeRef.child(dapAn.toLowerCase() + "." +getFileExtension(uri,activity));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        CauHoi CH = new CauHoi(id,uri.toString(),dapAn.toUpperCase());
                        cauHoiRef.child(String.valueOf(CH.getId())).setValue(CH);
                        progressDialog.dismiss();
                        Toast.makeText(activity, "Thêm câu hỏi thành công!", Toast.LENGTH_SHORT).show();
                        activity.finish();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressDialog.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(activity, "Upload Failed!", Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        });
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
    private String getFileExtension(Uri mUri, Activity activity){
        ContentResolver cr = activity.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}
