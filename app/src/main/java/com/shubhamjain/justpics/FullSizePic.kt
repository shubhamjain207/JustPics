package com.shubhamjain.justpics

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FullSizePic : AppCompatActivity() {

    lateinit var imageView3:ImageView;
    lateinit var ref:DatabaseReference;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_size_pic)


        imageView3 = findViewById(R.id.imageView3);

        var intent = intent;
        var imageUrl = intent.getStringExtra("image drawable");
        Glide.with(applicationContext).load(imageUrl).into(imageView3);



    }
}