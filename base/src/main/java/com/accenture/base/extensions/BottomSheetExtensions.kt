package com.accenture.base.extensions

import androidx.fragment.app.FragmentManager
import com.accenture.base.widget.BottomSheet

fun FragmentManager.bottomSheet(message: String) {
    BottomSheet(body = message, stateT = true).show(this, "BottomSheet")
}

fun FragmentManager.bottomSheet(title: String, message: String) {
    BottomSheet(body = message, title = title).show(this, "BottomSheet")
}