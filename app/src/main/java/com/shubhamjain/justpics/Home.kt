package com.shubhamjain.justpics

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class Home : AppCompatActivity() {

    private lateinit var profilePageButton: Button;
    private lateinit var homePageButton: Button;
    private lateinit var searchPageButton: Button;
    private lateinit var uploadPhotoBtn: FloatingActionButton;
    private lateinit var imageUri: Uri;
    private lateinit var ref: DatabaseReference;
    private lateinit var ref2: DatabaseReference;
    private lateinit var ref3: DatabaseReference;
    private lateinit var ref4: DatabaseReference;
    private lateinit var photoRatingList: DatabaseReference;
    private lateinit var auth: FirebaseAuth;
    private lateinit var photoView: ListView;
    private var list: ArrayList<String> = arrayListOf();
    private var userList: ArrayList<String> = arrayListOf();
    lateinit var firebaseStorage: FirebaseStorage;
    var reqUserName:String = "";
    var userUid:String="";

lateinit var view: View;


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val bar = actionBar;
        bar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#6071CF")));


        var user = Firebase.auth.currentUser;

        ref2 = Firebase.database.getReference().child("Usernames");

        ref3 = Firebase.database.getReference().child("Users").child(Firebase.auth.currentUser?.uid.toString());

        ref = Firebase.database.getReference().child("Users");


        uploadPhotoBtn = findViewById(R.id.uploadPhotoBtn);

        uploadPhotoBtn.setImageResource(R.drawable.action_button_back);



        



        ref4 = Firebase.database.getReference().child("AllPhotos");
        auth = Firebase.auth;

        ref2.child(Firebase.auth.currentUser?.uid.toString()).get().addOnCompleteListener(object :
            OnCompleteListener<DataSnapshot> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onComplete(p0: Task<DataSnapshot>) {
                if (p0.isSuccessful) {
                    if (p0.getResult().exists()) {

                        var datasnap = p0.getResult();


                        reqUserName = datasnap.value.toString();




                    } else {

                    }
                } else {

                }
            }

        })

        profilePageButton = findViewById(R.id.profilePageButton);
        homePageButton = findViewById(R.id.homePageButton);
        searchPageButton = findViewById(R.id.searchPageButton);
        photoView = findViewById(R.id.photoView);

        firebaseStorage = FirebaseStorage.getInstance();

        getPhotos();


        uploadPhotoBtn = findViewById(R.id.uploadPhotoBtn);

        searchPageButton.setOnClickListener {
            val intent = Intent(this,SearchActivity::class.java);
            startActivity(intent)
        }

        uploadPhotoBtn.setOnClickListener {
            selectPhotoToUpload();

        }

        profilePageButton.setOnClickListener {
            val intent = Intent(this, ProfilePageActivity::class.java);
            startActivity(intent);

        }

        homePageButton.setOnClickListener {
            // does nothing!
        }

        photoView.setOnItemClickListener { adapterView, view, i, l ->


            var builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Notification")
                .setContentText("Hello")
                .setStyle(NotificationCompat.BigTextStyle().bigText("Much longer text"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);



            var drawable = view.findViewById<ImageView>(R.id.imageView2);

           Log.i("User number ==== > ",userList.get(i));

            var intent = Intent(applicationContext,FullSizePic::class.java);
            intent.putExtra("image drawable",list.get(list.size-1-i));
            startActivity(intent)

        }

    }

    fun selectPhotoToUpload() {

        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        intent.type = "image/*";
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, 100);
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == 100) {
            imageUri = data?.data!!;

            var intent = Intent(this, UploadPhoto::class.java);
            intent.putExtra("ImageUri", imageUri.toString());

            startActivity(intent);
        }
    }

    fun getPhotos() {



        ref3.child("Mates").get().addOnCompleteListener(object :
            OnCompleteListener<DataSnapshot> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onComplete(p0: Task<DataSnapshot>) {
                if (p0.isSuccessful) {
                    if (p0.getResult().exists()) {

                        var datasnap = p0.getResult();



                        for(i in datasnap.children){
                            if(i.value.toString() == "true"){
                                getPhotos1(i.key.toString());

                            }
                        }

                        getPhotos2(Firebase.auth.currentUser?.uid.toString());



                    } else {

                    }
                } else {

                }
            }

        })


    }

    fun getPhotos1(userName:String) {

        ref2.get().addOnCompleteListener(object :
            OnCompleteListener<DataSnapshot> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onComplete(p0: Task<DataSnapshot>) {
                if (p0.isSuccessful) {
                    if (p0.getResult().exists()) {

                        var datasnap = p0.getResult();

                        for(i in datasnap.children){
                            if(i.value.toString() == userName){



                             getPhotos2(i.key.toString());


                            }
                        }







                    } else {

                    }
                } else {

                }
            }

        })




    }




    fun getPhotos2(userUid:String) {







        ref4.get().addOnCompleteListener(object :
            OnCompleteListener<DataSnapshot> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onComplete(p0: Task<DataSnapshot>) {
                if (p0.isSuccessful) {
                    if (p0.getResult().exists()) {

                        var datasnap = p0.getResult();


                       for(i in datasnap.children){


                           Log.i("String sString====>",i.key.toString())



//
                           if (i.key.toString().contains("username="+userUid)) {
                               list.add(i.value.toString());
                               userList.add(i.key.toString())
                               var arrayAdapter = MyAdapter(applicationContext, list.reversed(),userList.reversed(),Firebase.auth);
                               photoView.adapter = arrayAdapter;
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