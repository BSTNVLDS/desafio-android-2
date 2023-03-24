package com.accenture.base.extensions

import com.accenture.base.ERROR_AUTH_GENERIC
import com.accenture.base.ERROR_AUTH_NO_RECENT
import com.accenture.base.ERROR_AUTH_PASSWORD
import com.accenture.base.ERROR_AUTH_TWO_FACTOR
import com.accenture.base.ERROR_AUTH_EXIST
import com.accenture.base.ERROR_AUTH_INVALID
import com.accenture.base.ERROR_AUTH_INVALID_CODE
import com.accenture.base.ERROR_AUTH_INVALID_CREDENTIALS
import com.accenture.base.ERROR_INTERNET
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthMultiFactorException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import java.lang.Exception

fun firebaseError(exception: Exception?): String { //TODO: explore more cases...
    return when (exception) {
        is FirebaseAuthUserCollisionException -> ERROR_AUTH_EXIST
        is FirebaseAuthEmailException -> ERROR_AUTH_INVALID
        is FirebaseAuthActionCodeException -> ERROR_AUTH_INVALID_CODE
        is FirebaseAuthInvalidCredentialsException -> ERROR_AUTH_INVALID_CREDENTIALS
        is FirebaseAuthMultiFactorException -> ERROR_AUTH_TWO_FACTOR
        is FirebaseAuthWeakPasswordException -> ERROR_AUTH_PASSWORD
        is FirebaseAuthRecentLoginRequiredException -> ERROR_AUTH_NO_RECENT
        is FirebaseAuthException -> ERROR_AUTH_GENERIC
        is FirebaseNetworkException -> ERROR_INTERNET
        else -> exception.toString()
    }
}