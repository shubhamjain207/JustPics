package com.shubhamjain.justpics

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

class ProfileSetUp : AppCompatActivity() {

    lateinit var imageUri: String;
    lateinit var userNameBox:EditText;
    private lateinit var auth: FirebaseAuth;

    private lateinit var emailBox: EditText;
    private lateinit var passwordBox: EditText;

    private lateinit var signInButton: TextView;


    lateinit var imageView:ImageView;
    lateinit var button: Button;
    lateinit var storageReference: StorageReference;
    lateinit var firebaseDatabase: DatabaseReference;
    private lateinit var myRef: DatabaseReference;
    lateinit var firebaseStorage: FirebaseStorage;
    private lateinit var myRef1: DatabaseReference;
    private lateinit var myRef2: DatabaseReference;
    var photoList:ArrayList<String> = arrayListOf();
     var isThere2:Boolean = true;
     var imageUri2:String="";
      lateinit var email:String;
    lateinit var password:String;
    lateinit var userUid:String;
    lateinit var progressBar:ProgressBar;
     var doRun:Boolean=true;

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_set_up)


        FirebaseApp.initializeApp(applicationContext);

        auth = Firebase.auth;
        userUid = auth.currentUser?.uid.toString();

        if (auth.currentUser != null) {
            val intent = Intent(this, Home::class.java);
            startActivity(intent)
        }

        progressBar = findViewById(R.id.progressBar);


        emailBox = findViewById<EditText>(R.id.emailBox);
        passwordBox = findViewById<EditText>(R.id.passwordBox);
       // registerUserButton = findViewById<Button>(R.id.signInUserButton);
        signInButton = findViewById<TextView>(R.id.signInButton);



        imageUri = "";


        userNameBox = findViewById(R.id.userNameBox);
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);

        myRef2 = Firebase.database.getReference().child("Usernames");


        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        firebaseDatabase =
            Firebase.database.getReference().child("Users").child(Firebase.auth.currentUser?.uid.toString())
                .child("Image");

        imageView.setOnClickListener {
            if(progressBar.visibility == View.INVISIBLE)
             changeProfilePicture();
        }

        signInButton.setOnClickListener {
            if(progressBar.visibility == View.INVISIBLE){
                val intent = Intent(this, SignInActivity::class.java);
                startActivity(intent)
            }

        }



        button.setOnClickListener {



            isThere2 = true;


            if(userNameBox.text.toString().length<5){
                userNameBox.setError("UserName should be minimum 5 characters!");
                return@setOnClickListener;
            }

            if(hasSpecials(userNameBox.text.toString())){
                userNameBox.setError("UserName can have only _ as special character");
                return@setOnClickListener;
            }

            if(hasCapitals(userNameBox.text.toString())){
                userNameBox.setError("UserName should not contain capitals!");
                return@setOnClickListener;
            }

            email = emailBox.text.toString();

            password = passwordBox.text.toString();

            if (email == "" || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailBox.setError("Can't be empty or Invalid!")
                return@setOnClickListener;
            }
            if (password == "" || password.length < 8) {
                passwordBox.setError("Can't be empty! or have less than 8 characters")
                return@setOnClickListener;
            }


            progressBar.visibility = View.VISIBLE;
                doRun = false;

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val user = auth.currentUser;
                  userUid = user?.uid.toString()

//                    val intent = Intent(this, ProfileSetUp::class.java);
//
//
//                    intent.putExtra("Email", email);
//                    intent.putExtra("Password", password);
//                    //intent.putExtra("UserUid", user?.uid);
//                    startActivity(intent)

                } else {
                    emailBox.setError(task.exception?.message.toString());
                }
            }



            myRef2.get().addOnCompleteListener(object :
                OnCompleteListener<DataSnapshot> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onComplete(p0: Task<DataSnapshot>) {
                    if (p0.isSuccessful) {
                        if (p0.getResult().exists()) {

                            var datasnap = p0.getResult();


                            for(i in datasnap.children){


                                if(i.value.toString() == userNameBox.text.toString()){
                                    userNameBox.setError("UserName already taken!");
                                    isThere2 = false;

                                }
                            }







                        } else {

                        }
                    } else {

                    }

                    if(isThere2){

                        if(imageUri == ""){

//                            var storageref = FirebaseStorage.getInstance().getReference().child("images/sampledp.png");
//                            storageref.downloadUrl.addOnSuccessListener(object:OnSuccessListener<Uri>{
//                                override fun onSuccess(p0: Uri?) {
//                                    uploadPhoto(p0.toString());
//                                }
//
//                            }).addOnFailureListener(object:OnFailureListener{
//                                override fun onFailure(p0: java.lang.Exception) {
//
//                                }
//
//                            })

                            var refnew = FirebaseDatabase.getInstance().getReference().child("sampleimage");
                            refnew.get().addOnCompleteListener(object :
                                OnCompleteListener<DataSnapshot> {
                                @RequiresApi(Build.VERSION_CODES.O)
                                override fun onComplete(p0: Task<DataSnapshot>) {
                                    if (p0.isSuccessful) {
                                        if (p0.getResult().exists()) {

                                            var datasnap = p0.getResult();

                                            var userObj = UserClass();
                                            userObj.setUsername(userNameBox.text.toString());
                                            userObj.setPassword(password.toString());
                                            userObj.setEmail(email.toString());
                                            userObj.setImageUri(datasnap.value.toString());



                                            myRef = Firebase.database.getReference().child("Users").child(userUid.toString());
                                            myRef2 = Firebase.database.getReference().child("Usernames");



                                            var map = HashMap<String,String>();
                                            map.put("Email",userObj.getEmail());
                                            map.put("Password",userObj.getPassword());
                                            map.put("Username",userObj.getUserName());
                                            map.put("Image",userObj.getImageUri());
                                            map.put("Photos",photoList.toString());
                                            map.put("Mates",photoList.toString());
                                            myRef.setValue(map);

                                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                            val current = LocalDateTime.now().format(formatter);


                                            myRef1 = Firebase.database.getReference().child("Users").child(Firebase.auth.currentUser?.uid.toString()).child("Photos");
                                            myRef1.child(current).setValue("")
                                            myRef2.child(Firebase.auth.currentUser?.uid.toString()).setValue(userNameBox.text.toString());



                                            Log.i("User ------ >>>> ",userObj.getEmail() + " " + userObj.getUserName() + " " + userObj.getImageUri() + " " + userObj.getPassword());

                                            var intent = Intent(applicationContext,Home::class.java);
                                            startActivity(intent);


                                        } else {

                                        }
                                    } else {

                                    }
                                }

                            })

                        }

                        else{
                            uploadPhoto(imageUri);
                        }
//                        if(imageUri != ""){
//                            uploadPhoto(imageUri);
//                        }
//                        else{
//                            uploadPhoto(imageUri,false)
//                        }




                    }

                }



            })












           // else{
           //     userNameBox.setError("UserName Already Taken!")
           // }



      //  }









        }
        }




    fun changeProfilePicture(){
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        intent.type = "image/*";
        if(intent.resolveActivity(packageManager)!=null){
            startActivityForResult(intent,100);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == 100){
            imageUri = data?.data.toString()!!;
            imageView.setImageURI(Uri.parse(imageUri));
        }
    }

    fun uploadPhoto(imageUri:String) {
        var ref = storageReference.child("images").child(Firebase.auth.currentUser?.uid.toString());
        ref.putFile(Uri.parse(imageUri)).addOnSuccessListener(object :
            OnSuccessListener<UploadTask.TaskSnapshot> {
            override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
                downloadUrl1(ref);
            }

        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(p0: Exception) {

            }

        })


}

    fun downloadUrl1(ref: StorageReference) {
        ref.downloadUrl.addOnSuccessListener(object :
            OnSuccessListener<Uri> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onSuccess(p0: Uri?) {
                var model = Model(p0.toString());


             //   firebaseDatabase.setValue(model.imageUrl)

                         imageUri2 = model.imageUrl;
                            Log.i("Image uiri of == >",imageUri2);

                        var userObj = UserClass();
                        userObj.setUsername(userNameBox.text.toString());
                        userObj.setPassword(password.toString());
                        userObj.setEmail(email.toString());
                        userObj.setImageUri(imageUri2);



                        myRef = Firebase.database.getReference().child("Users").child(userUid.toString());
                        myRef2 = Firebase.database.getReference().child("Usernames");



                        var map = HashMap<String,String>();
                        map.put("Email",userObj.getEmail());
                        map.put("Password",userObj.getPassword());
                        map.put("Username",userObj.getUserName());
                        map.put("Image",userObj.getImageUri());
                        map.put("Photos",photoList.toString());
                        map.put("Mates",photoList.toString());
                        myRef.setValue(map);

                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        val current = LocalDateTime.now().format(formatter);


                        myRef1 = Firebase.database.getReference().child("Users").child(Firebase.auth.currentUser?.uid.toString()).child("Photos");
                        myRef1.child(current).setValue("")
                        myRef2.child(Firebase.auth.currentUser?.uid.toString()).setValue(userNameBox.text.toString());



                        Log.i("User ------ >>>> ",userObj.getEmail() + " " + userObj.getUserName() + " " + userObj.getImageUri() + " " + userObj.getPassword());

                        var intent = Intent(applicationContext,Home::class.java);
                        startActivity(intent);


            }

        }).addOnFailureListener(object : OnFailureListener {
            override fun onFailure(p0: java.lang.Exception) {

            }

        })
    }

    fun hasSpecials(username:String):Boolean{
            var p = Pattern.compile(
                "[^a-z0-9_]",Pattern.CASE_INSENSITIVE
            );

          var match = p.matcher(username);

          return match.find();

    }

    fun hasCapitals(username: String):Boolean{
            for(i in username){
                if(i > 'A' && i < 'Z'){
                    return true;
                }
            }

        return false;
    }




}

