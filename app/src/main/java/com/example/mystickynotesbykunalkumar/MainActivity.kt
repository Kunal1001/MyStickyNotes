package com.example.mystickynotesbykunalkumar

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback.Default
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var notesDao: NotesDao
    private lateinit var auth: FirebaseAuth
    private lateinit var rvAdaptor: RVAdaptor
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)
        auth = Firebase.auth
        findViewById<FloatingActionButton>(R.id.fab2).setOnClickListener {
            auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        setUpRecyclerView()

    }
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP //***Change Here***

            startActivity(intent)
            finish()
            System.exit(0)

        }
    }

    private fun setUpRecyclerView() {
        notesDao = NotesDao()
        recyclerView.layoutManager = LinearLayoutManager(this)
        var noteCollection = notesDao.noteCollection
        val currentUserId = auth.currentUser!!.uid
        val query = noteCollection.whereEqualTo("uid",currentUserId).orderBy("text",Query.Direction.ASCENDING)
        val recyclerOptions = FirestoreRecyclerOptions.Builder<Notes>().setQuery(query,Notes::class.java).build()
        rvAdaptor = RVAdaptor(recyclerOptions)
        recyclerView.adapter = rvAdaptor

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                rvAdaptor.deleteNote(position)
            }

        }

        ).attachToRecyclerView(
            recyclerView
        )

    }

    override fun onStart() {
        super.onStart()
        rvAdaptor.startListening()
    }

    override fun onStop() {
        super.onStop()
        rvAdaptor.stopListening()
    }
}