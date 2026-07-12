package com.codewithck.taskmint.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.codewithck.taskmint.R
import com.codewithck.taskmint.utils.ThemeManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ThemeBottomSheet : BottomSheetDialogFragment() {

    private lateinit var rgTheme: RadioGroup
    private lateinit var rbLight: RadioButton
    private lateinit var rbDark: RadioButton
    private lateinit var rbSystem: RadioButton
    private lateinit var btnApply: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(
            R.layout.bottom_sheet_theme,
            container,
            false
        )

        rgTheme = view.findViewById(R.id.rgTheme)
        rbLight = view.findViewById(R.id.rbLight)
        rbDark = view.findViewById(R.id.rbDark)
        rbSystem = view.findViewById(R.id.rbSystem)
        btnApply = view.findViewById(R.id.btnApplyTheme)

        rbSystem.isChecked = true

        btnApply.setOnClickListener {

            val theme = when (rgTheme.checkedRadioButtonId) {

                R.id.rbLight -> ThemeManager.LIGHT

                R.id.rbDark -> ThemeManager.DARK

                else -> ThemeManager.SYSTEM
            }

            ThemeManager.saveTheme(
                requireContext(),
                theme
            )

            ThemeManager.applyTheme(
                requireContext()
            )

            requireActivity().recreate()

            dismiss()
        }

        return view
    }
}