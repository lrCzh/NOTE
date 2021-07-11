package com.czh.note.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.czh.note.R
import com.czh.note.ui.base.BaseActivity
import com.czh.note.databinding.ActivityNoteDetailBinding
import com.czh.note.db.Note
import com.czh.note.util.TimeUtils
import com.czh.note.vm.NoteDetailViewModel
import com.gyf.immersionbar.ImmersionBar
import java.text.DateFormat
import com.czh.note.util.TimeConstants

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
    private lateinit var mNote: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initData()
    }

    private fun initView() {
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .transparentStatusBar()
            .init()
        Glide.with(this).load(R.drawable.pic_2).into(binding.ivScrollingTop)
        setToolbar(binding.toolbar)
    }

    private fun initData() {
        val noteId = intent.getLongExtra(NOTE_ID, NO_ID)
        if (noteId == NO_ID) {
            finish()
        } else {
            vm.getNote(noteId).observe(this, {
                it?.also {
                    mNote = it
                    updateUI(it)
                } ?: finish()
            })
        }
    }

    private fun updateUI(note: Note) {
        val span = TimeUtils.getTimeSpan(System.currentTimeMillis(), note.date, TimeConstants.DAY)
        val week = TimeUtils.getChineseWeek(note.date)
        val dateStr = DateFormat.getDateInstance(DateFormat.DEFAULT).format(note.date)
        binding.tvDay.text = "$span "
        binding.tvWeek.text = week
        binding.tvYearMonth.text = dateStr
        binding.tvTitle.text = note.title
        if (TextUtils.isEmpty(note.description)) {
            binding.tvDescription.visibility = View.GONE
        } else {
            binding.tvDescription.visibility = View.VISIBLE
            binding.tvDescription.text = note.description
        }
        if (TextUtils.isEmpty(note.location)) {
            binding.tvLocation.visibility = View.GONE
        } else {
            binding.tvLocation.visibility = View.VISIBLE
            binding.tvLocation.text = note.location
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                EditNoteActivity.actionStart(this, mNote.uid)
            }

            R.id.delete -> {
                vm.deleteNote(mNote)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}