package com.rv1den.facetertest.presentation.features.camera.view.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rv1den.facetertest.R
import com.rv1den.facetertest.domain.models.values.Resolution
import kotlinx.android.synthetic.main.fragment_bottom_sheet_settings.*


class BottomSheetSettings : BottomSheetDialogFragment() {
    lateinit var widthChangeListener: (String) -> Unit
    lateinit var heightChangeListener: (String) -> Unit
    lateinit var onApplyClickListener: () -> Unit
    lateinit var resolution: Resolution

    private val inputManager by lazy {
        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showKeyboard()

        width_edit_text.setText(resolution.width.toString())
        height_edit_text.setText(resolution.height.toString())


        width_edit_text.addTextChangedListener {
            val width = it.toString()
            widthChangeListener(width)
        }
        height_edit_text.addTextChangedListener {
            heightChangeListener(it.toString())
        }
        setting_apply_button.setOnClickListener {
            onApplyClickListener()
            hideKeyboard()
            dismiss()
        }
    }

    private fun showKeyboard() {
        inputManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun hideKeyboard() {
        inputManager?.hideSoftInputFromWindow(width_edit_text.windowToken, 0)
    }

}