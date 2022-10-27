package com.example.parstagram.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.parstagram.LoginActivity
import com.example.parstagram.MainActivity
import com.example.parstagram.Post
import com.example.parstagram.R
import com.parse.ParseFile
import com.parse.ParseUser
import java.io.File


class ComposeFragment : Fragment() {
    val photoFileName = "photo.jpg"
    var photoFile: File? = null
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    lateinit var ivPreview: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivPreview = view.findViewById(R.id.postPhoto)
        view.findViewById<Button>(R.id.postbutton).setOnClickListener{
            val description = view.findViewById<EditText>(R.id.photoDescription).text.toString()
            val user = ParseUser.getCurrentUser()
            if (photoFile != null){
                sendPost(description,user, photoFile!!)
            }
        }
        view.findViewById<Button>(R.id.takepicturebutton).setOnClickListener{
            onLaunchCamera()
        }
        //view.findViewById<Button>(R.id.logout).setOnClickListener{
            //ParseUser.logOut()
           // val intent = Intent(this@MainActivity, LoginActivity::class.java)
           // startActivity(intent)
       // }
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
                Toast.makeText(requireContext(), "Post created!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun onLaunchCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFileUri(photoFileName)

        if (photoFile != null){
            val fileProvider: Uri =
                FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            if (intent.resolveActivity(requireContext().packageManager) != null){
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }
    fun getPhotoFileUri(fileName: String): File{
        val mediaStorageDir = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            MainActivity.TAG
        )

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(MainActivity.TAG,"Failed to create Directory")
        }
        return File(mediaStorageDir.path + File.separator + fileName)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if (resultCode == AppCompatActivity.RESULT_OK){
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                ivPreview.setImageBitmap(takenImage)
            }else{
                Toast.makeText(requireContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT)
            }
        }
    }
}