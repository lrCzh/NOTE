package com.czh.note.ui.activity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.czh.note.R
import com.czh.note.config.AppConfig
import com.czh.note.databinding.ActivityEditNoteBinding
import com.czh.note.db.NOTE_TYPE_MARK
import com.czh.note.db.Note
import com.czh.note.ui.base.BaseActivity
import com.czh.note.util.TimeUtils
import com.czh.note.util.VibratorUtils
import com.czh.note.util.toast.toast
import com.czh.note.vm.EditNoteViewModel
import com.gyf.immersionbar.ImmersionBar
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class EditNoteActivity : BaseActivity() {

    companion object {
        private const val NOTE_ID = "note_id"
        private const val NO_ID = -1L

        fun actionStart(context: Context, noteId: Long? = null) {
            val intent = Intent(context, EditNoteActivity::class.java)
            if (noteId != null) {
                intent.putExtra(NOTE_ID, noteId)
            }
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityEditNoteBinding
    private val vm by viewModels<EditNoteViewModel>()

    private var mNote: Note? = null
    private val calendar = Calendar.getInstance()
    private var df: SimpleDateFormat = TimeUtils.getSafeDateFormat("yyyy-MM-dd")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar(binding.toolbar)
        initView()
        initVm()
        initData()
    }

    private fun initView() {
        ImmersionBar.with(this)
            .statusBarColor(R.color.white)
            .statusBarDarkFont(true)
            .init()
        binding.llDate.setOnClickListener {
            VibratorUtils.shortVibrate(AppConfig.mContext)
            showDatePicker()
        }
        updateDate(calendar.timeInMillis)
    }

    private fun initVm() {
        vm.saveStatusLiveData.observe(this, { saved ->
            if (saved) {
                finish()
            }
        })
    }

    private fun initData() {
        val noteId = intent.getLongExtra(NOTE_ID, NO_ID)
        if (noteId != NO_ID) {
            vm.getNote(noteId).observe(this, {
                it?.let {
                    this.mNote = it
                    updateUI(it)
                }
            })
        }
    }

    private fun updateUI(note: Note) {
        binding.etTitle.setText(note.title)
        binding.etDescription.setText(note.description)
        updateDate(note.date)
        binding.etLocation.setText(note.location)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                if (TextUtils.isEmpty(binding.etTitle.text.toString())) {
                    toast(R.string.en_enter_title_hint)
                    return true
                }
                if (TextUtils.isEmpty(binding.tvDate.text.toString())) {
                    toast(R.string.en_select_date_hint)
                    return true
                }

                mNote?.apply {
                    title = binding.etTitle.text.toString()
                    date = TimeUtils.string2Millis(df.format(calendar.time), "yyyy-MM-dd")
                    type = NOTE_TYPE_MARK
                    description = binding.etDescription.text.toString()
                    location = binding.etLocation.text.toString()
                    vm.updateNote(this)
                    return true
                }

                vm.saveNote(
                    Note(
                        title = binding.etTitle.text.toString(),
                        date = TimeUtils.string2Millis(df.format(calendar.time), "yyyy-MM-dd"),
                        type = NOTE_TYPE_MARK,
                        description = binding.etDescription.text.toString(),
                        location = binding.etLocation.text.toString()
                    )
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateDate(millis: Long) {
        calendar.time = Date(millis)
        binding.tvDate.text = DateFormat.getDateInstance(DateFormat.FULL).format(millis)
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this, { _, year, month, dayOfMonth ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month
                calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                binding.tvDate.text = DateFormat.getDateInstance(DateFormat.FULL)
                    .format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}