package com.codewithck.taskmint.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codewithck.taskmint.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddTaskBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.bottom_sheet_add_task,
            container,
            false
        )
    }
}