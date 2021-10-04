package com.android.firestorenoteexample

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.odevler.ayse_senses.note.NoteAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class NoteActivity : AppCompatActivity() {

    private val fab by lazy { findViewById<View>(R.id.fab) }
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recycler_view) }
    private var firestore: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_note_activity)

        initNote()
        firestore = FirebaseFirestore.getInstance()

        fab.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        bindNote()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.last_note -> {
                lastNotes()
                true
            }
            R.id.ordery_priority -> {
                bindNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initNote() {
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun lastNotes() {
        firestore?.collection("note")?.orderBy("date", Query.Direction.DESCENDING)
            ?.addSnapshotListener { value, error ->

                val noteList = arrayListOf<Note>()

                value?.forEach { queryDocumentSnapshot ->
                    queryDocumentSnapshot.toObject(Note::class.java).also { note ->
                        noteList.add(note)
                    }
                }
                recyclerView.adapter = NoteAdapter(this, noteList)
            }

    }

    private fun bindNote() {
        firestore?.collection("note")?.orderBy("priority", Query.Direction.DESCENDING)
            ?.addSnapshotListener { value, error ->

                val noteList = arrayListOf<Note>()

                value?.forEach { queryDocumentSnapshot ->
                    queryDocumentSnapshot.toObject(Note::class.java).also { note ->
                        noteList.add(note)
                    }
                }
                recyclerView.adapter = NoteAdapter(this, noteList)
            }
    }
}