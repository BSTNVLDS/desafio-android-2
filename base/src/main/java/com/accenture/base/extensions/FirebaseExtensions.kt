package com.accenture.base.extensions

import com.accenture.base.EXCEPTION_NO_FIND
import com.accenture.base.EXCEPTION_NO_FIND_ADVANCE
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private val fireInst = FirebaseAuth.getInstance()

fun initSession(
    email: String,
    pass: String,
    success: () -> Unit,
    failed: (exception: Exception?) -> Unit
) {
    fireInst.signInWithEmailAndPassword(email, pass)
        .completeListener(success = success, failed)
}

fun createUser(
    email: String,
    pass: String,
    success: (task: Task<AuthResult>) -> Unit,
    failed: (exception: Exception?) -> Unit
) {
    fireInst.createUserWithEmailAndPassword(email, pass)
        .completeListener({
            fireInst.currentUser?.sendEmailVerification()
            val task = fireInst.signInWithEmailAndPassword(email, pass)
            success(task)
        }, failed)
}

fun <T> String.registerFromKey(
    func: (String) -> T, success: () -> Unit,
    failed: (exception: Exception?) -> Unit
) {
    val fireData = FirebaseDatabase.getInstance().getReference(this)
    val key = fireData.push().key.toString()
    fireData.child(key).setValue(func(key)).completeListener(success, failed)
}

fun <T> Task<T>.completeListener(
    success: () -> Unit,
    failed: (exception: Exception?) -> Unit
) {
    this.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            success()
        } else {
            failed(task.exception)
        }
    }
}

fun exceptionLogout(exception: String): Exception {
    fireInst.signOut()
    return java.lang.Exception(exception)
}

inline fun <reified T> String.resultForQuery(
    parameter: String,
    value: String,
    crossinline failed: (exception: Exception?) -> Unit,
    crossinline success: (t: T) -> Unit
) {
    val fireData = FirebaseDatabase.getInstance().getReference(this)
    val query = fireData.orderByChild(parameter).equalTo(value)
    query.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                for (ds in dataSnapshot.children) {
                    val result = ds.getValue(T::class.java)
                    if (result != null) {
                        success(result)
                    } else {
                        failed(exceptionLogout(EXCEPTION_NO_FIND)) //TODO:possible parse error
                    }
                }
            } else {
                failed(exceptionLogout(EXCEPTION_NO_FIND))
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            failed(exceptionLogout(EXCEPTION_NO_FIND_ADVANCE))
        }
    })
}