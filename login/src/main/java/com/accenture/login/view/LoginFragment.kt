package com.accenture.login.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.accenture.base.REQUIRED_VALUE
import com.accenture.base.extensions.*
import com.accenture.base.model.ApiState
import com.accenture.base.model.User
import com.accenture.base.model.ResultApiState
import com.accenture.login.*
import com.accenture.login.databinding.FragmentLoginBinding
import com.accenture.login.viewmodel.LoginViewModel

class LoginFragment : Fragment() {
    private val loginViewModel: LoginViewModel by activityViewModels()
    private val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservable()
    }

    private fun initView() {
        binding.singUp.setOnClickListener {
            if (BuildConfig.BUILD_TYPE == "debug") {
                findNavController().navigate(R.id.action_login_to_home)
            } else {
                singUp()
            }


        }
        binding.singIn.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_form)
        }
        binding.resetPassword.setOnClickListener {
            recoveryPassword()
        }
    }

    private fun initObservable() {
        with(loginViewModel) {
            observe(state, ::stateQuery)
            observe(recovery, ::stateRecoveryQuery)
        }
    }

    private fun stateRecoveryQuery(result: ApiState<Exception?>?) {
        when (result) {
            is ApiState.Error -> {
                proDialogStop()
                val exception = firebaseError(result.exception)
                childFragmentManager.bottomSheet(exception)
            }
            is ApiState.Success -> {
                proDialogStop()
                childFragmentManager.bottomSheet(INFO_EMAIL_CONCAT + binding.inputEmail)
            }
            else -> {
                proDialogStop()
            }
        }
    }

    private fun stateQuery(result: ResultApiState<User, Exception?>?) {
        when (result) {
            is ResultApiState.Error -> {
                proDialogStop()
                val exception = firebaseError(result.exception)
                childFragmentManager.bottomSheet(exception)
            }
            is ResultApiState.Success -> {
                proDialogStop()
                requireContext().userSave(result.value)
                findNavController().navigate(R.id.action_login_to_home)
            }
            else -> {
                proDialogStop()
            }
        }
    }

    private fun singUp() {

        if (binding.inputEmail.isValid() || binding.inputPassword.isValid()) {
            childFragmentManager.bottomSheet(ACTION_FIELDS)
        } else {
            requireContext().proDialogRun(ACTION_SING_IN)
            loginViewModel.initSession(
                binding.inputEmail.fullText(), binding.inputPassword.fullText()
            )
        }
    }

    private fun recoveryPassword() {
        if (binding.inputEmail.isValid()) {
            binding.inputEmail.error = REQUIRED_VALUE
            childFragmentManager.bottomSheet(INFO_EMAIL_IN)
        } else {
            requireContext().proDialogRun(INFO_VALID_TIME)
            loginViewModel.recoveryPassword(binding.inputEmail.fullText())
        }
    }
}