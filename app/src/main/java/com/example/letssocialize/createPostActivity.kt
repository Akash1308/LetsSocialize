package com.example.letssocialize

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.letssocialize.daos.PostDao
import kotlinx.android.synthetic.main.activity_create_post.*

class createPostActivity : AppCompatActivity() {

    private lateinit var postDao: PostDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        postDao = PostDao()

        postButton.setOnClickListener{
            val input = postInput.text.toString().trim()
            if(input.isNotEmpty()){
                postDao.addpost(input)
                finish()
            }
        }
    }
}