package com.shubhamjain.justpics

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PendingMates : AppCompatActivity() {

    lateinit var ref:DatabaseReference;
    lateinit var ref1:DatabaseReference;
    private var list: ArrayList<String> = arrayListOf();
    lateinit var pendingMatesList:ListView;


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending_mates)

        ref = Firebase.database.getReference().child("Users");
        ref1 = Firebase.database.getReference().child("Usernames");
        pendingMatesList = findViewById(R.id.pendingMatesList);


        getPhotos();

        pendingMatesList.setOnItemClickListener { adapterView, view, i, l ->

           var userName = view as TextView;

            ref1.get().addOnCompleteListener(object :
                OnCompleteListener<DataSnapshot> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onComplete(p0: Task<DataSnapshot>) {
                    if (p0.isSuccessful) {
                        if (p0.getResult().exists()) {

                            var datasnap = p0.getResult();



                            for(i in datasnap.children){

                                Log.i("Hello ====>",i.toString())
                                if(i.value.toString() == userName.text){
                                    var intent = Intent(applicationContext,ProfileView::class.java);
                                    intent.putExtra("User Uid",i.key.toString());
                                    intent.putExtra("User Name",i.value.toString());
                                    intent.putExtra("Button Text","Accept Request");
                                    startActivity(intent);

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

    fun getPhotos() {


        ref.child(Firebase.auth.currentUser?.uid.toString()).child("Mates").get().addOnCompleteListener(object :
            OnCompleteListener<DataSnapshot> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onComplete(p0: Task<DataSnapshot>) {
                if (p0.isSuccessful) {
                    if (p0.getResult().exists()) {

                        var datasnap = p0.getResult();




                        for(i in datasnap.children){

                            if(i.value == false){
                                list.add(i.key.toString());
                            }



                        }

                        var adapter = ArrayAdapter(applicationContext,android.R.layout.simple_list_item_1,list);
                        pendingMatesList.adapter = adapter;


                    } else {

                    }
                } else {

                }
            }

        })


    }

}