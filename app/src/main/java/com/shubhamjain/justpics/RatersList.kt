package com.shubhamjain.justpics

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RatersList : AppCompatActivity() {

   lateinit var firebaseDatabase1:DatabaseReference;
   var userList:ArrayList<String> = arrayListOf();
    var userList1:ArrayList<String> = arrayListOf();

    lateinit var userListRatings:ListView;



    private lateinit var ref2: DatabaseReference;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_raters_list)

//        var intent = intent;
//        var userOther = intent.getStringExtra("User Other");
//        var userOtherRating = intent.getStringExtra("Rating Other");
//
//        userListRatings = findViewById(R.id.userListRatings);
//
//        ref2 = Firebase.database.getReference().child("Usernames");
//
//
//
//
//
//        firebaseDatabase1 = FirebaseDatabase.getInstance().getReference().child("Users").child(
//            userOther?.substringAfter("username=")!!.substringBefore(";")).child("Photos");
//
//                    firebaseDatabase1.get().addOnCompleteListener(object:
//                        OnCompleteListener<DataSnapshot> {
//                override fun onComplete(p0: Task<DataSnapshot>) {
//                    if(p0.isSuccessful){
//                        if(p0.result.exists()){
//                            var data = p0.getResult();
//
//
//
//                            for(i in data.children){
//                                if(i.toString().contains("username")){
//
//
//                                            for(j in i.children){
//
//
//
//
//                                                if(j.key.toString()!="Photo"){
//
//                                                   userList1.add(j.value.toString());
//
//
//                                                    ref2.child(j.key.toString()).get().addOnCompleteListener(object :
//                                                        OnCompleteListener<DataSnapshot> {
//                                                        @RequiresApi(Build.VERSION_CODES.O)
//                                                        override fun onComplete(p0: Task<DataSnapshot>) {
//                                                            if (p0.isSuccessful) {
//                                                                if (p0.getResult().exists()) {
//
//                                                                    var datasnap = p0.getResult();
//
//                                                                       Log.i("user rateer==== .>",datasnap.value.toString())
//
//                                                                        userList.add(datasnap.value.toString())
//
//                                                                    var arrayAdapter = MyAdapter2(applicationContext, userList,userList1);
//                                                                    userListRatings.adapter = arrayAdapter;
//
//
//                                                                } else {
//
//                                                                }
//                                                            } else {
//
//                                                            }
//                                                        }
//
//                                                    })
//
//
//
//
//
//                                                }
//                                            }
//
//
//
//                                }
//                            }
//                        }
//                    }
//                }
//
//            })
        }
    }
