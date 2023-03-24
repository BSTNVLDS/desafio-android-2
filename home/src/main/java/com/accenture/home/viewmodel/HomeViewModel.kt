package com.accenture.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.accenture.base.PATH_NOTE
import com.accenture.base.model.ApiState
import com.accenture.base.model.Note
import com.accenture.home.service.NotesService
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class HomeViewModel : ViewModel() {

    val state = MutableLiveData<ApiState<Exception?>>()

    fun load(userId:String) {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference(PATH_NOTE)
        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val note: Note? = dataSnapshot.getValue(Note::class.java)
                if (!NotesService.notes.contains(note)) {
                    if (note?.user == userId) {
                        NotesService.addNote(note)
                    }
                }
                state.postValue(ApiState.Success())
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                val note: Note? = dataSnapshot.getValue(Note::class.java)
                if (!NotesService.notes.contains(note)) {
                    if (note?.user == userId) {
                        NotesService.updateNote(note)
                    }
                }
                state.postValue(ApiState.Success())

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val note: Note? = dataSnapshot.getValue(Note::class.java)
                if (NotesService.notes.contains(note)) {
                    if (note?.user == userId) {
                        NotesService.removeNote(note)
                    }
                }
                state.postValue(ApiState.Success())
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                //todo:future changes
            }
            override fun onCancelled(databaseError: DatabaseError) {
                state.postValue(ApiState.Error(databaseError.toException()))

            }
        })
    }
}