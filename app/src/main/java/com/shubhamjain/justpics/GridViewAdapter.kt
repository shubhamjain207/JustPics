package com.shubhamjain.justpics

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide

class GridViewAdapter(var context: Context, var list:List<String>) : BaseAdapter() {

    lateinit var imageView2: ImageView;

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
        convertView = LayoutInflater.from(context).inflate(R.layout.griditems,p2,false);
        imageView2 = convertView.findViewById(R.id.imageView4);

        Glide.with(context).load(list.get(p0)).placeholder(Color.BLUE).into(imageView2);

        return convertView;

    }

}