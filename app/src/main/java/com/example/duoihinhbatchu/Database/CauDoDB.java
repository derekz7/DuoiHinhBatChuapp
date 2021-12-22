package com.example.duoihinhbatchu.Database;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.duoihinhbatchu.Models.CauDo;
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

public class CauDoDB {
    private FirebaseDatabase database;
    private DatabaseReference cauHoiRef;
    private StorageReference storeRef;
    private List<CauDo> listCauDo;

    public CauDoDB() {
        database = FirebaseDatabase.getInstance();
        cauHoiRef = database.getReference("Cauhoi");
        storeRef = FirebaseStorage.getInstance().getReference();
        listCauDo = new ArrayList<>();
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
                        CauDo CH = new CauDo(id,uri.toString(),dapAn.toUpperCase());
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
    public List<CauDo> getListCauHoi() {

        cauHoiRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCauDo.clear();
                for (DataSnapshot snap: snapshot.getChildren()){
                    CauDo cauDo = snap.getValue(CauDo.class);
                    listCauDo.add(cauDo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return listCauDo;
    }
    private String getFileExtension(Uri mUri, Activity activity){
        ContentResolver cr = activity.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}
