package com.accenture.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.accenture.base.ACTION_FIELDS
import com.accenture.base.extensions.bottomSheet
import com.accenture.base.extensions.proDialogRun
import com.accenture.base.extensions.fullText
import com.accenture.base.extensions.firebaseError
import com.accenture.base.extensions.isValid
import com.accenture.base.extensions.observe
import com.accenture.base.extensions.onClickNavigationUp
import com.accenture.base.extensions.proDialogStop
import com.accenture.base.model.ApiState
import com.accenture.base.model.Note
import com.accenture.note.databinding.FragmentNoteBinding
import com.accenture.note.viewmodel.NotesViewModel

class NoteFragment : Fragment() {
    private val notesViewModel: NotesViewModel by activityViewModels()
    private val binding by lazy { FragmentNoteBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initObservable()
    }

    private fun initView() {
        binding.btnToolbar.onClickNavigationUp()
        binding.btnSave.setOnClickListener {
            validateLabels()
        }
    }

    private fun initObservable() {
        with(notesViewModel) {
            observe(state, ::stateQuery)
        }
    }

    private fun stateQuery(result: ApiState<Exception?>?) {
        when (result) {
            is ApiState.Error -> {
                proDialogStop()
                val exception = firebaseError(result.exception)
                childFragmentManager.bottomSheet(exception)
            }
            is ApiState.Success -> {
                proDialogStop()
              findNavController().navigateUp()
            }
            else -> {
                proDialogStop()
            }
        }
    }

    private fun validateLabels() {
        if (binding.inputTitle.isValid()
            || binding.inputNote.isValid()
        ) {
            childFragmentManager.bottomSheet(ACTION_FIELDS)
        } else {
            saveNote()
        }
    }

    private fun saveNote() {
        requireContext().proDialogRun(INFO_NOTE_SAVING)
        val note = Note(
            title = binding.inputTitle.fullText(),
            body = binding.inputNote.fullText(),
            user = "null" //todo:temporal
        )
        notesViewModel.addNote(note)
    }
}