package com.codewithck.taskmint.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import com.codewithck.taskmint.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterBottomSheet : BottomSheetDialogFragment() {

    interface OnFilterAppliedListener {
        fun onFilterApplied(priority: String, status: String)
    }

    private var listener: OnFilterAppliedListener? = null

    fun setOnFilterAppliedListener(listener: OnFilterAppliedListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(
            R.layout.bottom_sheet_filter,
            container,
            false
        )

        val rgPriority = view.findViewById<RadioGroup>(R.id.rgPriorityFilter)
        val rgStatus = view.findViewById<RadioGroup>(R.id.rgStatusFilter)

        val btnCancel = view.findViewById<Button>(R.id.btnCancelFilter)
        val btnApply = view.findViewById<Button>(R.id.btnApplyFilter)

        btnCancel.setOnClickListener {
            dismiss()
        }

        btnApply.setOnClickListener {

            val priority = when (rgPriority.checkedRadioButtonId) {
                R.id.rbHigh -> "High"
                R.id.rbMedium -> "Medium"
                R.id.rbLow -> "Low"
                else -> "All"
            }

            val status = when (rgStatus.checkedRadioButtonId) {
                R.id.rbPending -> "Pending"
                R.id.rbCompleted -> "Completed"
                else -> "All"
            }

            listener?.onFilterApplied(priority, status)
            dismiss()
        }

        return view
    }
}