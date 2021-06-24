package com.czh.note.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.czh.note.R
import com.czh.note.config.AppConfig
import com.czh.note.databinding.ActivityEditNoteBinding
import com.czh.note.db.Note
import com.czh.note.ui.base.BaseActivity
import com.czh.note.ui.dialog.LabelDialog
import com.czh.note.util.VibratorUtils
import com.czh.note.util.toast.toast
import com.czh.note.vm.EditNoteViewModel

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
        binding.tvWeather.setOnClickListener {
            VibratorUtils.shortVibrate(AppConfig.mContext)
            LabelDialog.newInstance(LabelDialog.TYPE_WEATHER).apply {
                setDismissCallback {
                    binding.tvWeather.text = it
                }
            }.show(supportFragmentManager, "labelDialog")
        }

        binding.tvMood.setOnClickListener {
            VibratorUtils.shortVibrate(AppConfig.mContext)
            LabelDialog.newInstance(LabelDialog.TYPE_MOOD).apply {
                setDismissCallback {
                    binding.tvMood.text = it
                }
            }.show(supportFragmentManager, "labelDialog")
        }
    }

    private fun initVm() {
        vm.noteLiveData.observe(this, {
            updateUI(it)
        })
        vm.saveStatusLiveData.observe(this, { saved ->
            if (saved) {
                finish()
            }
        })
    }

    private fun initData() {
        val noteId = intent.getLongExtra(NOTE_ID, NO_ID)
        if (noteId != NO_ID) {
            vm.getNote(noteId)
        }
    }

    private fun updateUI(note: Note) {
        binding.etTitle.setText(note.title)
        binding.etDescription.setText(note.description)
        binding.tvDate.text = note.date
        binding.tvWeather.text = note.weather
        binding.tvMood.text = note.mood
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
}