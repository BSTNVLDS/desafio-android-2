package com.accenture.base.extensions

import android.content.Context
import android.content.SharedPreferences
import com.accenture.base.model.User
import com.google.firebase.auth.FirebaseAuth

val userAuth = FirebaseAuth.getInstance()

fun Context.sharedPref(): SharedPreferences =
    getSharedPreferences("BD", Context.MODE_PRIVATE)

fun Context.sharedPrefShow(key: String): String =
    sharedPref().getString(key, "Error").toString()

fun Context.sharedPrefAdd(key: String, text: String) =
    sharedPref().edit().putString(key, text).apply()

fun Context.sharedPrefClear() =
    sharedPref().edit().clear().apply()

fun Context.userSave(u: User) {
    sharedPrefAdd("user_id", u.id)
    sharedPrefAdd("user_userName", u.userName)
    sharedPrefAdd("user_email", u.email)
}

fun Context.userLoad() =
    User(
        id = sharedPrefShow("user_id"),
        userName = sharedPrefShow("user_userName"),
        email = sharedPrefShow("user_email"),
    )


fun userExistFirebase(): Boolean {
    return userAuth.currentUser != null
}

fun Context.userExistShared(): Boolean {
    return sharedPrefShow("user_id") != "Error"
}

fun Context.userExist(): Boolean {
    return userExistShared() && userExistFirebase()
}

fun Context.logOut() {
    sharedPrefClear()
    userAuth.signOut()
}
