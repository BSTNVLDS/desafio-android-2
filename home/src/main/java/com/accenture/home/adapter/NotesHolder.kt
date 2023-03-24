package com.accenture.home.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.accenture.base.model.Note
import com.accenture.home.databinding.ItemNotesBinding

class NotesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemNotesBinding.bind(itemView)

    fun bind(note: Note) {
        binding.txtBody.text = note.body
        binding.txtTitle.text = note.title
        itemView.setOnClickListener {
            //todo: show full info
        }
    }
}