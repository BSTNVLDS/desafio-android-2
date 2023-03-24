package com.accenture.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.accenture.base.EXCEPTION_VERIFY_EMAIL
import com.accenture.base.extensions.completeListener
import com.accenture.base.extensions.initSession
import com.accenture.base.extensions.resultForQuery
import com.accenture.base.extensions.exceptionLogout
import com.accenture.base.model.ApiState
import com.accenture.base.model.ResultApiState
import com.accenture.base.model.User
import com.accenture.login.EXCEPTION_NO_VALID
import com.accenture.login.PATH_USER
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    val state = MutableLiveData<ResultApiState<User, Exception?>>()
    val recovery = MutableLiveData<ApiState<Exception?>>()
    private val fireInst = FirebaseAuth.getInstance()

    fun initSession(email: String, password: String) {
        initSession(email, password,
            success = {
                val user = FirebaseAuth.getInstance().currentUser
                if (user?.isEmailVerified == true) {
                    PATH_USER.resultForQuery<User>("email", email,
                        success = {
                            state.postValue(ResultApiState.Success(it))
                        },
                        failed = {
                            state.postValue(ResultApiState.Error(it))
                        }
                    )
                } else {
                   val exception= exceptionLogout(EXCEPTION_VERIFY_EMAIL)
                    state.postValue(ResultApiState.Error(exception))
                }
            },
            failed = {
                state.postValue(ResultApiState.Error(it))
            }
        )
    }

    fun recoveryPassword(email: String) {
        fireInst.sendPasswordResetEmail(email).completeListener(
            success = {
                recovery.postValue(ApiState.Success())
            },
            failed = {
                fireInst.signOut()
                val exception = java.lang.Exception(EXCEPTION_NO_VALID)
                recovery.postValue(ApiState.Error(exception))
            }
        )
    }

}