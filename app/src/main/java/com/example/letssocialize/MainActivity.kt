package com.example.letssocialize

import android.app.Dialog
import android.app.DownloadManager
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letssocialize.daos.PostDao
import com.example.letssocialize.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.internal.GoogleApiAvailabilityCache
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.core.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IpostAdapter {

    private lateinit var adapter:PostAdapter
    private lateinit var postDao: PostDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.my_toolbar))

        fab.setOnClickListener{
            val intent = Intent(this,createPostActivity::class.java)
            startActivity(intent)
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {

        postDao = PostDao()
        val postCollections = postDao.postCollection
        val query = postCollections.orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query,Post::class.java).build()


        adapter = PostAdapter(recyclerViewOptions, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    // we need to tell the adapter when to start listening the data of firestore and when to stop so we will implement it below

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeCliked(postId: String) {

        postDao.updateLikes(postId)
    }

    fun signOut(view: android.view.View) {

        val dialog = AlertDialog.Builder(this)
        dialog.setMessage("Do you want to logout?")
            .setPositiveButton("yes") { dialog, id ->

                FirebaseAuth.getInstance().signOut()

                val intent = Intent(this, SigninActivity::class.java)
                startActivity(intent)
                finish()

            }
            .setNegativeButton("No"){dialog,id ->
                dialog.dismiss()
            }
        dialog.create()
        dialog.show()




    }






}