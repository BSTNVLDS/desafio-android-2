package com.accenture.base.extensions

import android.view.View
import androidx.navigation.findNavController

fun View.onClickNavigationUp() {
    this.setOnClickListener {
        findNavController().navigateUp()
    }
}