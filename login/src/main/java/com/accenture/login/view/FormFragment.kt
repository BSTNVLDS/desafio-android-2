package com.accenture.login.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.accenture.base.extensions.isEmail
import com.accenture.base.extensions.isValid
import com.accenture.base.extensions.firebaseError
import com.accenture.base.extensions.observe
import com.accenture.base.extensions.onClickNavigationUp
import com.accenture.base.extensions.proDialogStop
import com.accenture.base.extensions.fullText
import com.accenture.base.extensions.materialDialog
import com.accenture.base.extensions.proDialogRun
import com.accenture.base.extensions.bottomSheet
import com.accenture.base.extensions.initSession
import com.accenture.base.extensions.compareTo
import com.accenture.base.model.ApiState
import com.accenture.base.model.User
import com.accenture.login.EXCEPTION_NO_VALID
import com.accenture.login.INFO_REGISTER_USER
import com.accenture.login.ACTION_OK
import com.accenture.login.EMPTY_STRING
import com.accenture.login.INFO_PROFILE_TIME
import com.accenture.login.INFO_PRE_EXISTENCE_USER
import com.accenture.login.ERROR_AUTH_INVALID
import com.accenture.login.INFO_FORMAT_EMAIL
import com.accenture.login.ACTION_FIELDS
import com.accenture.login.ACTION_MIN_CHARACTERS
import com.accenture.login.ACTION_PASS_EQUALS
import com.accenture.login.databinding.FragmentFormBinding
import com.accenture.login.viewmodel.FormViewModel

class FormFragment : Fragment() {
    private val registerViewModel: FormViewModel by activityViewModels()
    private val binding by lazy { FragmentFormBinding.inflate(layoutInflater) }

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
        binding.btnToolbar.onClickNavigationUp()
        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    private fun initObservable() {
        with(registerViewModel) {
            observe(state, ::stateQuery)
        }
    }

    private fun stateQuery(result: ApiState<Exception?>?) {
        when (result) {
            is ApiState.Error -> {
                proDialogStop()
                val exception = firebaseError(result.error)
                childFragmentManager.bottomSheet(exception)
            }
            is ApiState.Success -> {
                proDialogStop()
                childFragmentManager.bottomSheet(INFO_REGISTER_USER)
            }
            else -> {
                proDialogStop()
            }
        }
    }

    private fun register() {
        if (binding.inputUserName.isValid()
            || binding.inputEmail.isValid()
            || binding.inputPassword.isValid()
            || binding.inputPasswordConfirm.isValid()
        ) {
            childFragmentManager.bottomSheet(ACTION_FIELDS)
        } else if (binding.inputPassword.fullText().length < 6) {
            childFragmentManager.bottomSheet(ACTION_MIN_CHARACTERS)
        } else if (binding.inputPassword.compareTo(binding.inputPasswordConfirm)) {
            childFragmentManager.bottomSheet(ACTION_PASS_EQUALS)
        } else if (!binding.inputEmail.isEmail()) {
            showDialogEmail()
        } else {
            validateExist()
        }
    }

    private fun validateExist() {
        requireContext().proDialogRun(INFO_PROFILE_TIME)
        initSession(
            email = binding.inputEmail.fullText(),
            pass = binding.inputPassword.fullText(),
            success = {
                proDialogStop()
                childFragmentManager.bottomSheet(INFO_PRE_EXISTENCE_USER)
            },
            failed = {
                val user = User(
                    id = EMPTY_STRING,
                    userName = binding.inputUserName.fullText(),
                    email = binding.inputEmail.fullText(),
                    pass = binding.inputPassword.fullText(),
                )
                registerViewModel.addUser(user)
            })
    }

    private fun showDialogEmail() {
        binding.inputEmail.error = EXCEPTION_NO_VALID
        val builder = requireContext().materialDialog(ERROR_AUTH_INVALID, INFO_FORMAT_EMAIL)
        builder.setPositiveButton(ACTION_OK) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        builder.show()
    }

}