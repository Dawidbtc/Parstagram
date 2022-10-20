package com.example.parstagram

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.parse.*
import java.io.File

class MainActivity : AppCompatActivity() {

    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.logout).setOnClickListener{
            ParseUser.logOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.postbutton).setOnClickListener{
            val description = findViewById<EditText>(R.id.photoDescription).text.toString()
            val user = ParseUser.getCurrentUser()
            if (photoFile != null){
                sendPost(description,user, photoFile!!)
            }
        }
        findViewById<Button>(R.id.takepicturebutton).setOnClickListener{
            onLaunchCamera()
        }
        queryPost()
    }
    fun sendPost(description: String, user: ParseUser, file: File){
        val post = Post()
        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(file))
        post.saveInBackground{ exception ->
            if (exception!=null){
                Log.e("Post:","Error while saving post")
            }else{
                Log.i("Post:","Successfully saved post")
                Toast.makeText(this, "Post created!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                val ivPreview: ImageView = findViewById(R.id.postPhoto)
                ivPreview.setImageBitmap(takenImage)
            }else{
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT)
            }
        }
    }
    fun onLaunchCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFileUri(photoFileName)

        if (photoFile != null){
            val fileProvider: Uri =
                FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            if (intent.resolveActivity(packageManager) != null){
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }
    fun getPhotoFileUri(fileName: String): File{
        val mediaStorageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG,"Failed to create Directory")
        }
        return File(mediaStorageDir.path + File.separator + fileName)
    }
    fun queryPost(){
        val query : ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER)
        query.findInBackground(object: FindCallback<Post>{
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null){
                    Log.e("queryPost: ","error")
                }else{
                    if (posts!=null){
                        Log.e("Posts:","Found Posts")
                    }
                }
            }
        })
    }
    companion object{
        const val TAG = "MainActivity"
    }
}