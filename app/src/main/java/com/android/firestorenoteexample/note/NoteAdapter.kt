package com.android.odevler.ayse_senses.note

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.firestorenoteexample.Note
import com.android.firestorenoteexample.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NoteAdapter(private val context: Context, private val noteList: ArrayList<Note>) :
    RecyclerView.Adapter<NoteAdapter.NoteHolder>() {

    inner class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView by lazy { itemView.findViewById(R.id.title_text) }
        private val description: TextView by lazy { itemView.findViewById(R.id.description_text) }
        private val priority: TextView by lazy { itemView.findViewById(R.id.priority_text) }
        private val date: TextView by lazy { itemView.findViewById(R.id.date_text) }

        fun bind(note: Note) {
            title.text = note.title
            description.text = note.description
            priority.text = note.priority.toString()

            val convertDate = note.date?.let { convertLongToTime(it) }
            date.text = convertDate
        }
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy hh:mm")
        return format.format(date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        return NoteHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.bind(noteList[position])
    }

    override fun getItemCount() = noteList.size
}