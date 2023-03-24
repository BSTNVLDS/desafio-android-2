package com.accenture.home.service

import com.accenture.base.model.Note
import java.util.ArrayList

object NotesService {
    var notes: MutableList<Note> = ArrayList()
    fun addNote(note: Note) {
        notes.add(note)
    }

    fun removeNote(note: Note) {
        notes.remove(note)
    }

    fun updateNote(note: Note) {
        notes[notes.indexOf(note)] = note
    }
}