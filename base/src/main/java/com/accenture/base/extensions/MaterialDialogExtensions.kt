package com.accenture.base.extensions

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.accenture.base.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

var dialog: AlertDialog? = null

fun Context.proDialogRun(message: String) {
    val builder = MaterialAlertDialogBuilder(this)
    builder.setTitle(message)
    builder.background = ContextCompat.getDrawable(this, R.drawable.alert_round)
    builder.setView(R.layout.progress_indicator)
    builder.setCancelable(false)
    dialog = builder.create()
    dialog?.show()
}

fun proDialogStop() {
    dialog?.dismiss()
}
fun Context.materialDialog(title: String, message: String): MaterialAlertDialogBuilder {
    val builder = MaterialAlertDialogBuilder(this)
    builder.setCancelable(false).setTitle(title)
    builder.background = ContextCompat.getDrawable(this, R.drawable.alert_round)
    builder.setMessage(message)
    return builder
}