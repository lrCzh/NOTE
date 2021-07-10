package com.czh.note.ui.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.ViewGroup
import com.czh.note.R
import com.czh.note.databinding.DialogAddNoteBinding
import com.czh.note.util.KeyBoardUtil
import com.czh.note.util.TimeUtils
import com.czh.note.util.toast.toast
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AddNoteDialog(context: Context) : Dialog(context) {

    private val binding: DialogAddNoteBinding = DialogAddNoteBinding.inflate(layoutInflater)
    private var saveCallback: ((title: String, date: String) -> Unit)? = null

    private val calendar = Calendar.getInstance()
    private var df: SimpleDateFormat = TimeUtils.getSafeDateFormat("yyyy-MM-dd")
    private var date: String? = null

    init {
        setContentView(binding.root)
        initView()
        initWindow()
        KeyBoardUtil.openKeyBord(binding.etEvent, context)
    }

    private fun initView() {
        binding.apply {
            tvCancel.setOnClickListener {
                dismiss()
            }
            tvSave.setOnClickListener {
                val title = etEvent.text.toString()
                if (TextUtils.isEmpty(title)) {
                    toast(R.string.and_add_event_hint)
                    return@setOnClickListener
                }
                if (date == null) {
                    toast(R.string.and_select_date_hint)
                    return@setOnClickListener
                }
                saveCallback?.invoke(title, date!!)
                dismiss()
            }
            llDate.setOnClickListener {
                showDatePicker()
            }
            date = df.format(calendar.time)
            tvDate.text = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)
        }
    }

    private fun initWindow() {
        window?.apply {
            setBackgroundDrawableResource(R.color.transparent)
            setWindowAnimations(R.style.ActionSheetDialogAnimation)
            attributes.gravity = Gravity.BOTTOM
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    override fun dismiss() {
        KeyBoardUtil.closeKeyBord(binding.etEvent, context)
        super.dismiss()
    }

    fun setSaveCallback(callback: ((title: String, date: String) -> Unit)) {
        this.saveCallback = callback
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            context, { _, year, month, dayOfMonth ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month
                calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                date = df.format(calendar.time) //2021-06-26
                binding.tvDate.text = DateFormat.getDateInstance(DateFormat.FULL)
                    .format(calendar.time) //2021年6月26日 周六
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}