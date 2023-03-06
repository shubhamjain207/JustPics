package com.shubhamjain.justpics

class Image (url:String) : java.io.Serializable {
    lateinit var downloadUrl:String;

    init {

        this.downloadUrl = url;

    }
}