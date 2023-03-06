package com.shubhamjain.justpics

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SearchActivity : AppCompatActivity() {

    lateinit var searchUser:EditText;
    lateinit var searchUserList:ListView;
    private lateinit var ref: DatabaseReference;
    private lateinit var myRef: DatabaseReference;
    private lateinit var ref2: DatabaseReference;
    private lateinit var auth: FirebaseAuth;
    lateinit var adapter:ArrayAdapter<*>;
    var userList:ArrayList<String> = arrayListOf();
    var reqUserName:String="";

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchUserList = findViewById(R.id.searchUserList);
        searchUser = findViewById(R.id.searchUser);

        ref2 = Firebase.database.getReference().child("Usernames");

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

        searchUser.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    getUserFunc(p0)
                };
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        ref = Firebase.database.getReference().child("Usernames");
        myRef = Firebase.database.getReference().child("Users");

        auth = Firebase.auth;

        searchUserList.setOnItemClickListener { adapterView, view, i, l ->

                var textView = view as TextView;

                ref.get().addOnCompleteListener(object :
                    OnCompleteListener<DataSnapshot> {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onComplete(p0: Task<DataSnapshot>) {
                        if (p0.isSuccessful) {
                            if (p0.getResult().exists()) {

                                var datasnap = p0.getResult();



                                for(i in datasnap.children){

                                    if(i.value.toString() == textView.text){
                                        var intent = Intent(applicationContext,ProfileView::class.java);
                                        intent.putExtra("User Uid",i.key.toString());
                                        intent.putExtra("User Name",i.value.toString());
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

    fun getUserFunc(userChar:CharSequence){
        if(userChar.length<=0){
            userList.clear();
            adapter = ArrayAdapter(applicationContext,android.R.layout.simple_list_item_1,userList);
            searchUserList.adapter = adapter;
            return;
        }

        ref.get().addOnCompleteListener(object :
            OnCompleteListener<DataSnapshot> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onComplete(p0: Task<DataSnapshot>) {
                if (p0.isSuccessful) {
                    if (p0.getResult().exists()) {

                        var datasnap = p0.getResult();



                        for(i in datasnap.children){
                            if(i.value.toString() == reqUserName){
                                continue;
                            }
                            if(i.value.toString().contains(userChar)){
                                if(!userList.contains(i.value.toString())){
                                    userList.add(i.value.toString());
                                }


                            }
                            else{
                                userList.remove(i.value.toString())
                            }



                        }

                        adapter = ArrayAdapter(applicationContext,android.R.layout.simple_list_item_1,userList);
                        searchUserList.adapter = adapter;


                    } else {

                    }
                } else {

                }
            }

        })


    }




}