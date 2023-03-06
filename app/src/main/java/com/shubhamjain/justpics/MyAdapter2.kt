package com.shubhamjain.justpics

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class MyAdapter2(var context: Context, var list:List<String>, var list1:List<String>){

//    lateinit var textView:TextView;
//    lateinit var textView1:TextView;
//
//    override fun getCount(): Int {
//        return list.size;
//    }
//
//    override fun getItem(p0: Int): Any {
//           return list.get(p0);
//
//    }
//
//
//
//    override fun getItemId(p0: Int): Long {
//        return p0.toLong();
//    }
//
//    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
//        var convertView = p1;
//        convertView = LayoutInflater.from(context).inflate(R.layout.list_item,p2,false);
//        textView = convertView.findViewById(R.id.textView3);
//        textView1 = convertView.findViewById(R.id.textView4);
//
//        textView.setText(list.get(p0));
//        textView1.setText(list1.get(p0));
//
//        return convertView;
//    }
}