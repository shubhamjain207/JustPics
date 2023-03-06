package com.shubhamjain.justpics

class UserClass () {

       private lateinit var email1:String;

      private lateinit var password1:String;
      private lateinit var username1:String;
     private lateinit var imageUri1:String;

   fun setEmail(email:String){
       email1 = email;
   }

   fun getEmail(): String {
       return email1;
   }

    fun setPassword(password:String){
        password1 = password;
    }

    fun getPassword(): String {
        return password1;
    }

    fun setUsername(username:String){
        username1 = username;
    }

    fun getUserName(): String {
        return username1;
    }

    fun setImageUri(imageUri:String){
        imageUri1 = imageUri;
    }

    fun getImageUri(): String {
        return imageUri1;
    }



}