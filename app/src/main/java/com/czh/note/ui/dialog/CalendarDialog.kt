package com.czh.note.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.czh.note.databinding.DialogCalendarBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * @Description: 日期选择弹窗
 * @Author: czh
 * @CreateDate: 2021/7/10 18:35
 */
class CalendarDialog(
    private val date: Array<Int>,// [2021,07,10]
    private val saveCallback: (date: Array<Int>?) -> Unit
) : BottomSheetDialogFragment() {

    private var binding: DialogCalendarBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCalendarBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDialog()
        initView()
        jumpDate(date)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initDialog() {
        dialog?.apply {
            setOnShowListener {
                val bottomSheet =
                    findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                val behavior: BottomSheetBehavior<FrameLayout> =
                    BottomSheetBehavior.from(bottomSheet)
                behavior.skipCollapsed = true
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    private fun initView() {
        binding?.apply {
            tvCancel.setOnClickListener {
                dismiss()
            }
            tvConfirm.setOnClickListener {
                saveCallback.invoke(getSelectDate())
                dismiss()
            }
        }
    }

    private fun jumpDate(date: Array<Int>) {
        binding?.calendarView?.scrollToCalendar(date[0], date[1] + 1, date[2])
    }

    private fun getSelectDate(): Array<Int>? {
        binding?.apply {
            val year = calendarView.selectedCalendar.year
            val month = calendarView.selectedCalendar.month
            val day = calendarView.selectedCalendar.day
            return arrayOf(year, month - 1, day)
        }
        return null
    }
}