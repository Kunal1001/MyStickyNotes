package com.example.mystickynotesbykunalkumar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback

class AddNoteActivity : AppCompatActivity() {
    private lateinit var editText: EditText
    private lateinit var addButton: Button
    private lateinit var notesDao: NotesDao
    private lateinit var intent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        editText = findViewById(R.id.inputText)
        addButton = findViewById(R.id.postButton)
        notesDao = NotesDao()
        supportActionBar!!.setTitle("Add Note")
        intent = Intent(this, MainActivity::class.java)
        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)
        addButton.setOnClickListener {
            val note = editText.text.toString()
            if(note.isNotEmpty()){
                notesDao.addNote(note)
                startActivity(intent)
            }

        }



    }
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            startActivity(intent)
        }
    }
}