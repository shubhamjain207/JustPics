package com.shubhamjain.justpics

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Settings : AppCompatActivity() {

    lateinit var settingsList:ListView;
    lateinit var settingsListData:List<String>;
    lateinit var firebaseDatabase: DatabaseReference;
    lateinit var storageReference: StorageReference;
    lateinit var firebaseStorage: FirebaseStorage;
    lateinit var adapter:ArrayAdapter<String>;
    var imageUri:Uri?=null;


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        firebaseDatabase =
            Firebase.database.getReference().child("Users").child(Firebase.auth.currentUser?.uid.toString())
                .child("Image");

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();




        settingsList = findViewById<ListView>(R.id.settingsList);
        settingsListData = mutableListOf<String>("Change profile pic","Pending Requests","Log out");
        adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,settingsListData);
        settingsList.adapter = adapter;


        settingsList.setOnItemClickListener { adapterView, view, i, l ->

            if(i == 0){
                changeProfilePicture();
            }

            else if (i == 1){
                displayPendingRequests();
            }

            else if (i == 2){
                Firebase.auth.signOut();
                val intent = Intent(this,SignInActivity::class.java);
                startActivity(intent)
            }



        }







    }

    fun changeProfilePicture(){
        var intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        intent.type = "image/*";
        if(intent.resolveActivity(packageManager)!=null){
            startActivityForResult(intent,100);
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == 100){
            imageUri = data?.data;

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            val current = LocalDateTime.now().format(formatter);

            uploadPhoto(imageUri!!,current);


            ProfilePageActivity.staticClass.profilePicture.setImageURI(imageUri);
        }
    }

    fun uploadPhoto(imageUri:Uri,current: String) {
        var ref = storageReference.child("images").child(Firebase.auth.currentUser?.uid.toString());
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


                firebaseDatabase.setValue(model.imageUrl)




            }

        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(p0: java.lang.Exception) {

            }

        })
    }

    fun displayPendingRequests(){
           var intent = Intent(applicationContext,PendingMates::class.java);
           startActivity(intent);
    }
}