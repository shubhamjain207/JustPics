package com.shubhamjain.justpics

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Display.Mode
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class UploadPhoto : AppCompatActivity() {

    lateinit var uploadPhotoView: ImageView;
    lateinit var imageUri: Uri;
    lateinit var uploadBtn: Button;
    lateinit var firebaseDatabase: DatabaseReference;
    lateinit var firebaseDatabase1: DatabaseReference;
    lateinit var firebaseStorage: FirebaseStorage;
    lateinit var storageReference: StorageReference;
    lateinit var bitmap: Bitmap;
    lateinit var auth: FirebaseAuth;
    lateinit var progressBar2:ProgressBar;


    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_photo)

        uploadBtn = findViewById(R.id.uploadBtn);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        progressBar2 = findViewById(R.id.progressBar2);



        auth = Firebase.auth;

        uploadPhotoView = findViewById(R.id.uploadPhotoView);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users");


        firebaseDatabase =
            Firebase.database.getReference().child("Users").child(auth.currentUser?.uid.toString())
                .child("Photos");



        firebaseDatabase1 =
            Firebase.database.getReference().child("AllPhotos");



        var intent = getIntent();

        imageUri = Uri.parse(intent.getStringExtra("ImageUri").toString());

        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri);


        uploadPhotoView.setImageURI(imageUri);


        uploadBtn.setOnClickListener {

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            val current = LocalDateTime.now().format(formatter);


                uploadPhoto(current);
                progressBar2.visibility = View.VISIBLE;



        }


    }

    fun uploadPhoto(current: String) {
        var ref = storageReference.child("images").child("useruid="+auth.currentUser?.uid + current);
        ref.putFile(imageUri).addOnSuccessListener(object :
            OnSuccessListener<UploadTask.TaskSnapshot> {
            override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
                downloadUrl1(current, ref);
            }

        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(p0: Exception) {

            }

        })


    }



    fun downloadUrl1(current: String, ref: StorageReference) {
        ref.downloadUrl.addOnSuccessListener(object :
            OnSuccessListener<Uri> {
            override fun onSuccess(p0: Uri?) {
                var model = Model(p0.toString());

                Log.i("Heloooo+>>>>",model.imageUrl)

                firebaseDatabase1.child("username="+Firebase.auth.currentUser?.uid.toString()+ ";" + current).setValue(model.imageUrl)
                firebaseDatabase.child("username="+Firebase.auth.currentUser?.uid.toString() + ";" +current).child("Photo").setValue(model.imageUrl)
                var intent = Intent(applicationContext, Home::class.java);

                startActivity(intent);


            }

        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(p0: java.lang.Exception) {

            }

        })
    }

}