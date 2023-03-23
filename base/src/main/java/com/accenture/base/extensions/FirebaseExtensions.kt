package com.accenture.base.extensions

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

private val fireInst = FirebaseAuth.getInstance()

fun initSession(
    email: String,
    pass: String,
    success: () -> Unit,
    failed: (exception: Exception?) -> Unit
) {
    fireInst.signInWithEmailAndPassword(email, pass)
        .completeListener(success, failed)
}

fun <T> Task<T>.completeListener(success: () -> Unit, failed: (exception: Exception?) -> Unit) {
    this.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            success()
        } else {
            failed(task.exception)
        }
    }
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
