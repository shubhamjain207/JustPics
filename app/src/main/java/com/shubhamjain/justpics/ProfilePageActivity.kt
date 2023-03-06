package com.shubhamjain.justpics

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.shubhamjain.justpics.databinding.ActivityHomeBinding
import com.shubhamjain.justpics.databinding.ActivityProfilePageBinding

class ProfilePageActivity : AppCompatActivity() {

    lateinit var settingsButton: TextView;
    private lateinit var databse: FirebaseDatabase;
    private lateinit var myRef: DatabaseReference;
   // lateinit var pendingMatesBtn:TextView;
    lateinit var photoListProfile:ListView;
    lateinit var photoThumbnail:GridView;
    lateinit var userName:TextView;
    lateinit var currentMatesBtn:TextView;
    lateinit var currentMatesNumber:TextView;
   // lateinit var pendingMatesNumber:TextView;
    lateinit var profilePicture:ImageView;
    private var list: ArrayList<String> = arrayListOf();
    lateinit var firebaseStorage: FirebaseStorage;
    lateinit var storageReference: StorageReference;
    lateinit var databaseReference: DatabaseReference;
    var currentCount:Int=0;
    var pendingCount:Int=0;
    lateinit var activityMainBindin:ActivityProfilePageBinding;






    class staticClass{
        companion object{
            lateinit var profilePicture: ImageView;
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBindin = ActivityProfilePageBinding.inflate(layoutInflater);

        setContentView(activityMainBindin.root)

        supportActionBar?.hide();


        profilePicture = findViewById(R.id.profilePicture);
        userName = findViewById(R.id.userName);
        currentMatesNumber = findViewById(R.id.currentMatesNumber);
        //pendingMatesNumber = findViewById(R.id.pendingMatesNumber);
        //photoListProfile = findViewById(R.id.photoListProfile);

        photoThumbnail = findViewById(R.id.photoThumbnail);


        getPhotos2();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        var ref = storageReference.child("images").child(Firebase.auth.currentUser?.uid.toString());
        var refnew = FirebaseDatabase.getInstance().getReference().child("Users").child(Firebase.auth.currentUser?.uid.toString());

        var databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(Firebase.auth.currentUser?.uid.toString());


//        ref.downloadUrl.addOnSuccessListener(object:OnSuccessListener<Uri>{
//            override fun onSuccess(p0: Uri?) {
//                Glide.with(applicationContext).load(p0).into(profilePicture);
//            }
//
//        })

        photoThumbnail.setOnItemClickListener { adapterView, view, i, l ->
            var drawable = view.findViewById<ImageView>(R.id.imageView2);


            var intent = Intent(applicationContext,FullSizePic::class.java);
            intent.putExtra("image drawable",list.get(list.size-1-i));
            startActivity(intent)
        }


        refnew.child("Image").get().addOnCompleteListener(object:OnCompleteListener<DataSnapshot>{
            override fun onComplete(p0: Task<DataSnapshot>) {
                if (p0.isSuccessful){

                    if (p0.getResult().exists()){
                        var datasnap = p0.getResult();
                        Glide.with(applicationContext).load(datasnap.value.toString() ).into(profilePicture);
                    }
                }
                else{

                }
            }

        })

        databaseReference.child("Username").get().addOnCompleteListener(object :
            OnCompleteListener<DataSnapshot> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onComplete(p0: Task<DataSnapshot>) {
                if (p0.isSuccessful) {
                    if (p0.getResult().exists()) {

                        var datasnap = p0.getResult();

                            userName.setText(datasnap.value.toString());

                    } else {

                    }
                } else {

                }
            }

        })

        databaseReference.child("Mates").get().addOnCompleteListener(object :
            OnCompleteListener<DataSnapshot> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onComplete(p0: Task<DataSnapshot>) {
                if (p0.isSuccessful) {
                    if (p0.getResult().exists()) {

                        var datasnap = p0.getResult();
                        for(i in datasnap.children){
                            if(i.value == true){
                                currentCount++;
                            }

                        }

                        currentMatesNumber.setText("Mates   " + currentCount.toString());
                       // pendingMatesNumber.setText(pendingCount.toString());

                    } else {

                    }
                } else {

                }
            }

        })




        settingsButton = findViewById(R.id.settingsButton);
        staticClass.profilePicture = findViewById(R.id.profilePicture);

        //pendingMatesBtn = findViewById(R.id.pendingMatesBtn);


        databse = Firebase.database;
        myRef = databse.getReference().child("Users");


        var map = HashMap<String,String>();




        settingsButton.setOnClickListener {
            val intent = Intent(this,Settings::class.java);
            startActivity(intent);
        }

//        pendingMatesBtn.setOnClickListener {
//            var intent = Intent(applicationContext,PendingMates::class.java);
//            startActivity(intent);
//
//        }

        currentMatesNumber.setOnClickListener {
            var intent = Intent(applicationContext,CurrentMates::class.java);
            startActivity(intent);

        }




    }

    fun getPhotos2() {

        //var ref = FirebaseDatabase.getInstance().getReference("Users").child(Firebase.auth.currentUser?.uid.toString());
        var ref = FirebaseDatabase.getInstance().getReference("AllPhotos");


        ref.get().addOnCompleteListener(object :
            OnCompleteListener<DataSnapshot> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onComplete(p0: Task<DataSnapshot>) {
                if (p0.isSuccessful) {
                    if (p0.getResult().exists()) {

                        var datasnap = p0.getResult();


                        for(i in datasnap.children){
                            if (i.key.toString().contains("username="+Firebase.auth.currentUser?.uid.toString())) {
                                list.add(i.value.toString());
                                var arrayAdapter = GridViewAdapter(applicationContext, list.reversed());

                                 activityMainBindin.photoThumbnail.adapter =  arrayAdapter;

                            }
                        }




                    } else {

                    }
                } else {

                }
            }

        })


    }

}