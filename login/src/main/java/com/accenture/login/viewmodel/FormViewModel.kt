package com.accenture.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.accenture.base.extensions.completeListener
import com.accenture.base.extensions.createUser
import com.accenture.base.extensions.registerFromKey
import com.accenture.base.model.ApiState
import com.accenture.base.model.User
import com.accenture.login.PATH_USER

class FormViewModel : ViewModel() {

    val state = MutableLiveData<ApiState<Exception?>>()

    fun addUser(newUser: User) {
        createUser(newUser.email, newUser.pass,
            { taskResult ->
                taskResult.completeListener(
                    success = {
                        PATH_USER.registerFromKey({ key ->
                            newUser.copy(id = key)
                        }, success = {
                            state.postValue(ApiState.Success())
                        }, { exceptionDatabase ->
                            state.postValue(ApiState.Error(exceptionDatabase))
                        }
                        )
                    }, failed = { exceptionLogin ->
                        state.postValue(ApiState.Error(exceptionLogin))
                    }
                )
            }, failed = { exceptionAuth ->
                state.postValue(ApiState.Error(exceptionAuth))
            })
    }
}