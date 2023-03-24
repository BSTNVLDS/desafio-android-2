package com.accenture.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.accenture.base.extensions.proDialogRun
import com.accenture.base.extensions.logOut
import com.accenture.base.extensions.bottomSheet
import com.accenture.base.extensions.firebaseError
import com.accenture.base.extensions.observe
import com.accenture.base.extensions.proDialogStop
import com.accenture.base.model.ApiState
import com.accenture.home.adapter.NotesAdapter
import com.accenture.home.databinding.FragmentHomeBinding
import com.accenture.home.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val adapter = NotesAdapter()
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        initObservable()
    }

    private fun initObservable() {
        with(homeViewModel) {
            observe(state, ::stateQuery)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun stateQuery(result: ApiState<Exception?>?) {
        when (result) {
            is ApiState.Error -> {
                proDialogStop()
                val exception = firebaseError(result.exception)
                childFragmentManager.bottomSheet(exception)
            }
            is ApiState.Success -> {
                binding.rvNotes.adapter?.notifyDataSetChanged()
                proDialogStop()
            }
            else -> {
                proDialogStop()
            }
        }
    }

    private fun initComponents() {
        binding.btnToolbar.setOnClickListener {
            requireContext().logOut()
        }
        binding.viewBtnAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_note)
        }
        binding.rvNotes.adapter = adapter
        requireContext().proDialogRun(LOADING)
        homeViewModel.load()
    }

}