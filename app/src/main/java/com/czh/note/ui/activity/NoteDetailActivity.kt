package com.czh.note.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.czh.note.R
import com.czh.note.ui.base.BaseActivity
import com.czh.note.databinding.ActivityNoteDetailBinding
import com.czh.note.db.Note
import com.czh.note.util.getWeatherIcon
import com.czh.note.vm.NoteDetailViewModel

class NoteDetailActivity : BaseActivity() {

    companion object {

        private const val NOTE_ID = "note_id"
        private const val NO_ID = -1L

        fun actionStart(context: Context, noteId: Long) {
            val intent = Intent(context, NoteDetailActivity::class.java)
            intent.putExtra(NOTE_ID, noteId)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityNoteDetailBinding
    private val vm by viewModels<NoteDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVm()
        initData()
    }

    private fun initVm() {
        vm.noteLiveData.observe(this, {
            updateUI(it)
        })
        vm.deleteStatusLiveData.observe(this, { deleted ->
            if (deleted) {
                finish()
            }
        })
    }

    private fun initData() {
        val noteId = intent.getLongExtra(NOTE_ID, NO_ID)
        if (noteId == NO_ID) {
            finish()
        } else {
            vm.getNote(noteId)
        }
    }

    private fun updateUI(note: Note) {
        binding.toolbar.title = note.date
        binding.ivWeather.setImageResource(getWeatherIcon(note.weather))
        binding.tvMood.text = note.mood
        binding.tvTitle.text = note.title
        binding.tvDescription.text = note.description
        setToolbar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                vm.noteLiveData.value?.let {
                    EditNoteActivity.actionStart(this, it.uid)
                }
            }

            R.id.delete -> {
                vm.noteLiveData.value?.let { vm.deleteNote(it) }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}