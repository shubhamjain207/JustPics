package com.shubhamjain.justpics

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.reflect.typeOf

class ProfileView : AppCompatActivity() {

    lateinit var myRef:DatabaseReference;
    lateinit var ref:DatabaseReference;
    lateinit var ref2:DatabaseReference;
    lateinit var profileUserDp:ImageView;
    lateinit var profileUserName:TextView;
    lateinit var profilePhotoList: ListView;
    private var list: ArrayList<String> = arrayListOf();
    private var userList: ArrayList<String> = arrayListOf();
    lateinit var auth:FirebaseAuth;
    lateinit var addMateBtn:Button;
    lateinit var firebaseDatabase:DatabaseReference;
    lateinit var firebaseDatabase1:DatabaseReference;
    lateinit var dbRef:DatabaseReference;
     var reqUserName:String="";
     var buttonText:String="";



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)

        myRef = Firebase.database.getReference().child("Users");
        ref2 = Firebase.database.getReference().child("Usernames");
        ref = Firebase.database.getReference().child("Users");
        dbRef = Firebase.database.getReference().child("Users");
        var userUid = intent.getStringExtra("User Uid");
        var userName = intent.getStringExtra("User Name");
        //var buttonText = intent.getStringExtra("Button Text");

        profileUserDp = findViewById(R.id.profileUserDp);
        profileUserName = findViewById(R.id.profileUserName);
        profilePhotoList = findViewById(R.id.profilePhotoList);
        addMateBtn = findViewById(R.id.addMateBtn);


        ref2.child(Firebase.auth.currentUser?.uid.toString()).get().addOnCompleteListener(object :
            OnCompleteListener<DataSnapshot> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onComplete(p0: Task<DataSnapshot>) {
                if (p0.isSuccessful) {
                    if (p0.getResult().exists()) {

                        var datasnap = p0.getResult();

//                        var map = datasnap.value as Map<*,*>;
//
//                        profileUserName.text = map.get("Username") as CharSequence?;


                        reqUserName = datasnap.value.toString();






                    } else {

                    }
                } else {

                }
            }

        })






            checkUser(reqUserName,userUid.toString())





        addMateBtn.setOnClickListener {
            if(addMateBtn.text == "Accept Mate"){
                addMateBtn.text = "Mates";
                firebaseDatabase =
                    Firebase.database.getReference().child("Users").child(Firebase.auth.currentUser?.uid.toString())
                        .child("Mates").child(userName.toString());

                firebaseDatabase.setValue(true);

                firebaseDatabase1 =
                    Firebase.database.getReference().child("Users").child(userUid.toString()).child("Mates")
                        .child(reqUserName);

                firebaseDatabase1.setValue(true);
                return@setOnClickListener;
            }

            // if text (mates) unfollow popup .......
            // if text (Request Sent) Cancel Request Pops .......

            else if(addMateBtn.text == "Add Mate"){
                addMateBtn.text = "Sent Request";
                dbRef.child(userUid.toString()).child("Mates").child(reqUserName).setValue(false);
            }


        }


        auth = Firebase.auth;


        getUserData(userUid.toString());
        getPhotos(userUid.toString());


    }
    fun getUserData(userUid:String){
        myRef.child(userUid).get().addOnCompleteListener(object :
            OnCompleteListener<DataSnapshot> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onComplete(p0: Task<DataSnapshot>) {
                if (p0.isSuccessful) {
                    if (p0.getResult().exists()) {

                        var datasnap = p0.getResult();

                        var map = datasnap.value as Map<*,*>;

                        profileUserName.text = map.get("Username") as CharSequence?;



                    } else {

                    }
                } else {

                }
            }

        })


    }

    fun getPhotos(userUid: String) {



        ref.child(userUid).get().addOnCompleteListener(object :
            OnCompleteListener<DataSnapshot> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onComplete(p0: Task<DataSnapshot>) {
                if (p0.isSuccessful) {
                    if (p0.getResult().exists()) {

                        var datasnap = p0.getResult();

                        var map1 = datasnap.child("Photos").value as HashMap<*, *>;


                        map1.forEach { key, value ->

                            if (value != "") {
                                list.add(value.toString());
                                userList.add(key.toString());
                                var arrayAdapter = MyAdapter(applicationContext, list,userList,Firebase.auth);
                                profilePhotoList.adapter = arrayAdapter;
                            }

                        }


                    } else {

                    }
                } else {

                }
            }

        })


    }

    fun checkUser(userName:String,userUid:String) {
        var buttonText1 = ""

        firebaseDatabase =
            Firebase.database.getReference().child("Users").child(Firebase.auth.currentUser?.uid.toString())
                .child("Mates");

        firebaseDatabase.get().addOnCompleteListener(object :
            OnCompleteListener<DataSnapshot> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onComplete(p0: Task<DataSnapshot>) {
                if (p0.isSuccessful) {
                    if (p0.getResult().exists()) {

                        var datasnap = p0.getResult();

                        //var map = datasnap.value as Map<*, *>;



//                       for(i in map){
//
//                           if(i.value == false){
//                               buttonText = "Accept Request";
//                           }
//                       }

                        for(i in datasnap.children){
                            if(i.value.toString() == "false"){

                            buttonText1 = "Accept Mate";
                            addMateBtn.setText(buttonText1);

                        }
                            else if (i.value.toString() == "true"){
                                addMateBtn.setText("Mates")
                            }
                        }




                    } else {

                    }
                } else {

                }
            }

        })

        firebaseDatabase1 =
            Firebase.database.getReference().child("Users").child(userUid)
                .child("Mates");

        firebaseDatabase1.get().addOnCompleteListener(object :
            OnCompleteListener<DataSnapshot> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onComplete(p0: Task<DataSnapshot>) {
                if (p0.isSuccessful) {
                    if (p0.getResult().exists()) {

                        var datasnap = p0.getResult();

                        //var map = datasnap.value as Map<*, *>;



//                       for(i in map){
//
//                           if(i.value == false){
//                               buttonText = "Accept Request";
//                           }
//                       }

                        for(i in datasnap.children){
                            if(i.value.toString() == "false"){

                                buttonText1 = "Request Sent";
                                addMateBtn.setText(buttonText1);

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
