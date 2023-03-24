package com.accenture.note.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.accenture.base.PATH_NOTE
import com.accenture.base.extensions.registerFromKey
import com.accenture.base.model.ApiState
import com.accenture.base.model.Note

class NotesViewModel : ViewModel() {

    val state = MutableLiveData<ApiState<Exception?>>()

    fun addNote(newNote: Note) {
        PATH_NOTE.registerFromKey({ key ->
            newNote.copy(id = key)
        }, success = {
            state.postValue(ApiState.Success())
        }, { exceptionDatabase ->
            state.postValue(ApiState.Error(exceptionDatabase))
        }
        )
    }
}