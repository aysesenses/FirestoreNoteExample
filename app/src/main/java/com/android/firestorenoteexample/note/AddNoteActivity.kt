package com.android.firestorenoteexample

import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AddNoteActivity : AppCompatActivity() {

    private val write_title by lazy { findViewById<EditText>(R.id.write_title) }
    private val write_description by lazy { findViewById<EditText>(R.id.write_description) }
    private val numberPicker by lazy { findViewById<NumberPicker>(R.id.numberPicker) }

    private val textInputLayoutTitle by lazy { findViewById<TextInputLayout>(R.id.text_input_layout_title) }
    private val textInputLayoutDescription by lazy { findViewById<TextInputLayout>(R.id.text_input_layout_description) }

    private var firestore: FirebaseFirestore? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_new_note)

        numberPicker.minValue = 0
        numberPicker.maxValue = 10
        numberPicker.wrapSelectorWheel = true

        firestore = FirebaseFirestore.getInstance()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_add_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_note -> {
                if (valid()) {
                    saveNote(
                        title = write_title.text.toString(),
                        priority = numberPicker.value,
                        description = write_description.text.toString(),
                        date = Calendar.getInstance().time.time
                    )
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun saveNote(title: String, priority: Int, description: String, date: Long) {
        val note = Note(title, priority, description, date)

        firestore?.collection("note")?.add(note)?.addOnCompleteListener { task ->
            when (task.isSuccessful) {
                true -> finish()
                false -> Toast.makeText(this, "Note not added to Firebase", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun valid(): Boolean {

        val title = write_title.text
        val description = write_description.text

        val titleCheck: Boolean = checkIsNullOrEmpty(
            title,
            write_title,
            textInputLayoutTitle,
            "title cannot emty")

        val descriptionCheck: Boolean = checkIsNullOrEmpty(
            description,
            write_description,
            textInputLayoutDescription,
            "description cannot emty"
        )

        write_description.doOnTextChanged { text, start, before, count ->
            checkIsNullOrEmpty(
                description,
                write_description,
                textInputLayoutDescription,
                "description cannot emty"
            )

        }
        write_title.doOnTextChanged { text, start, before, count ->
            checkIsNullOrEmpty(title, write_title, textInputLayoutTitle, "title cannot emty")
        }

        if (titleCheck && descriptionCheck) {
            return true
        } else {
            when {
                !titleCheck && !descriptionCheck -> write_description.clearFocus()
                !titleCheck -> write_title.requestFocus()
                else -> write_description.requestFocus()
            }
            return false
        }
    }

    fun checkIsNullOrEmpty(
        text: Editable,
        focus: EditText,
        textInputLayout: TextInputLayout,
        error_message: String
    ): Boolean {
        when {
            text.isEmpty() -> {
                focus.requestFocus()
                textInputLayout.error = error_message
                return false
            }
            else -> {
                textInputLayout.isErrorEnabled = false
                return true
            }
        }
    }

}