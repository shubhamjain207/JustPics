package com.shubhamjain.justpics

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class MyAdapter(var context:Context,var list:List<String>,var userList:List<String>,var auth:FirebaseAuth) : BaseAdapter() {

    lateinit var imageView2:ImageView;
    lateinit var ratingSystem:SeekBar;
    lateinit var firebaseDatabase:DatabaseReference;
    lateinit var firebaseDatabase1:DatabaseReference;
    lateinit var ratingsBtn:TextView;

    override fun getCount(): Int {
      return list.size;
    }


    override fun getItem(p0: Int): Any {
       return list.get(p0);
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong();
    }

    @SuppressLint("ResourceType")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var convertView = p1;
        convertView = LayoutInflater.from(context).inflate(R.layout.image_card,p2,false);
        imageView2 = convertView.findViewById(R.id.imageView2);
//        ratingSystem = convertView.findViewById(R.id.ratingSystem);
//
//        ratingsBtn = convertView.findViewById(R.id.ratingsBtn);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userList.get(p0).substringAfter("username=").substringBefore(";")).child("Photos").child(userList.get(p0));


//        ratingsBtn.setOnClickListener {
//
//            val intent = Intent(context, RatersList::class.java);
//            intent.putExtra("User Other",userList.get(p0));
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//
//
//
//        }


//        ratingSystem.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
//            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                firebaseDatabase.child(auth.currentUser?.uid.toString()).setValue(p1);
//            }
//
//            override fun onStartTrackingTouch(p0: SeekBar?) {
//
//            }
//
//            override fun onStopTrackingTouch(p0: SeekBar?) {
//
//
//
//
//            }
//
//        })

            Glide.with(context).load(list.get(p0)).placeholder(Color.BLUE).into(imageView2);






        return convertView;

    }
}