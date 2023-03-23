package com.accenture.base.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.accenture.base.databinding.ViewBottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheet(
    val stateT: Boolean = false,
    val stateB: Boolean = false,
    val title: String = "Información",
    val body: String = "Body"
) : BottomSheetDialogFragment() {

    private val bindingView by lazy { ViewBottomSheetDialogBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindingView.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        invisibilityBody(stateB)
        invisibilityTitle(stateT)
        setTitle(title)
        setBody(body)

    }

    private fun setTitle(title: String) {
        bindingView.bottomSheetTitle.text = title
    }

    private fun setBody(body: String) {
        bindingView.bottomSheetBody.text = body
    }

    private fun invisibilityTitle(state: Boolean) {
        if (state) bindingView.bottomSheetTitle.visibility = View.GONE
    }

    private fun invisibilityBody(state: Boolean) {
        if (state) bindingView.bottomSheetBody.visibility = View.GONE
    }

}