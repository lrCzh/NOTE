package com.czh.note.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.czh.note.R
import com.czh.note.config.AppConfig
import com.czh.note.ui.adapter.NoteAdapter
import com.czh.note.ui.adapter.NoteComparator
import com.czh.note.ui.base.BaseActivity
import com.czh.note.databinding.ActivityMainBinding
import com.czh.note.db.AppDatabase
import com.czh.note.db.Note
import com.czh.note.ui.adapter.NoteItemDecoration
import com.czh.note.util.VibratorUtils
import com.czh.note.util.dp2px
import com.czh.note.vm.MainViewModel
import com.gyf.immersionbar.ImmersionBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val vm by viewModels<MainViewModel>()
    private lateinit var mAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    override fun onResume() {
        super.onResume()
        binding.efab.show()
    }

    override fun onPause() {
        super.onPause()
        binding.efab.hide()
    }

    private fun initView() {
        ImmersionBar.with(this)
//            .statusBarColor(R.color.color_fff6f6f6)
            .statusBarDarkFont(true)
            .init()
        setSupportActionBar(binding.toolbar)
        Glide.with(this).load(R.drawable.pic_1).into(binding.ivScrollingTop)

        mAdapter = NoteAdapter(NoteComparator, onClick = {
            NoteDetailActivity.actionStart(this, it.uid)
        }, onLongClick = {
            VibratorUtils.shortVibrate(AppConfig.mContext)
            showDeleteNoteHint(it)
        })
        binding.rv.adapter = mAdapter
        binding.rv.addItemDecoration(NoteItemDecoration(dp2px(8)))
        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    binding.efab.shrink()
                } else {
                    binding.efab.extend()
                }
            }
        })

        binding.efab.setOnClickListener {
            EditNoteActivity.actionStart(this)
        }

        lifecycleScope.launch(Dispatchers.IO) {
            Pager(config = PagingConfig(pageSize = 10, initialLoadSize = 50)) {
                AppDatabase.getInstance().noteDao().getNotes()
            }.flow.cachedIn(this).collectLatest {
                mAdapter.submitData(it)
            }
        }
    }


    private fun showDeleteNoteHint(note: Note) {
        AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.main_dialog_hint))
            .setMessage(resources.getString(R.string.main_delete_hint))
            .setPositiveButton(resources.getString(R.string.confirm)) { _, _ -> vm.deleteNote(note) }
            .setNegativeButton(resources.getString(R.string.cancel)) { _, _ -> }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                startActivity(Intent(this,SettingActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}