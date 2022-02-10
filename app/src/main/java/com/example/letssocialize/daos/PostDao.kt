package com.example.letssocialize.daos

import com.example.letssocialize.models.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {

    val db = FirebaseFirestore.getInstance()
    val postCollection = db.collection("posts")
    val auth = Firebase.auth

    fun addpost(text: String){
        val currentUserId = auth.currentUser!!.uid  // yaha exclaimaition marks lagane se current user bataega ki yaha pakka user hai aur agar current user null hua to app crash hojaega
        GlobalScope.launch {
            val userDao = UserDao()
            val user = userDao.getUserById(currentUserId).await().toObject(com.example.letssocialize.models.User::class.java)!!

            val currentTime = System.currentTimeMillis()
            val post = Post(text, user, currentTime)
            postCollection.document().set(post)
        }
    }

    fun getPostById(postId:String): Task<DocumentSnapshot>{
        return postCollection.document(postId).get()
    }

    fun updateLikes(postId: String){

        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid
            val post = getPostById(postId).await().toObject(Post::class.java)!!
            val isLiked = post.likedBy.contains(currentUserId)

            if(isLiked){
                post.likedBy.remove(currentUserId)
            } else{
                post.likedBy.add(currentUserId)
            }

            postCollection.document(postId).set(post)


        }
    }
}