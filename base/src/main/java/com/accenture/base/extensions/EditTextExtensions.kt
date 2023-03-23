package com.accenture.base.extensions

import android.text.Editable
import android.text.TextWatcher
import com.accenture.base.REQUIRED_VALUE
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.isValid(): Boolean {
    this.setWatcher()
    return this.textIsEmpty()
}

fun TextInputLayout.setWatcher() {
    this.editText?.addTextChangedListener(initWatcher(this))
}

fun TextInputLayout.textIsEmpty(): Boolean {
    return this.editText?.text.toString().isEmpty()
}

fun TextInputLayout.fullText(): String {
    return this.editText?.text.toString()
}

fun TextInputLayout.compareTo(text: TextInputLayout): Boolean {
    return this.fullText() != text.fullText()
}

fun TextInputLayout.isEmail(): Boolean {
    return this.fullText().endsWith(".cl") || this.fullText().endsWith(".com")
}

fun initWatcher(text: TextInputLayout): TextWatcher {
    if (text.textIsEmpty()) text.error = REQUIRED_VALUE
    return object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) =
            if (text.textIsEmpty()) {
                text.error = REQUIRED_VALUE
            } else {
                text.error = ""
            }

        override fun afterTextChanged(editable: Editable) {}
    }
}