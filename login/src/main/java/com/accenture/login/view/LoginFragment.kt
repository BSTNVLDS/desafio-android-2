package com.accenture.login.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.accenture.base.extensions.bottomSheet
import com.accenture.login.INFO_IN_WORKING
import com.accenture.login.INFO_UNAVAILABLE
import com.accenture.login.R
import com.accenture.login.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.singUp.setOnClickListener {
            childFragmentManager.bottomSheet(INFO_UNAVAILABLE, INFO_IN_WORKING)
        }
        binding.singIn.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_form)
        }
        binding.resetPassword.setOnClickListener {
            childFragmentManager.bottomSheet(INFO_UNAVAILABLE, INFO_IN_WORKING)
        }
    }
}