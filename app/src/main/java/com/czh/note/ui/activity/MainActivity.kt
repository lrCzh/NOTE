package com.czh.note.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.recyclerview.widget.RecyclerView
import com.czh.note.R
import com.czh.note.config.AppConfig
import com.czh.note.ui.adapter.NoteAdapter
import com.czh.note.ui.adapter.NoteComparator
import com.czh.note.ui.base.BaseActivity
import com.czh.note.databinding.ActivityMainBinding
import com.czh.note.db.AppDatabase
import com.czh.note.util.VibratorUtils
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

    private fun initView() {
        ImmersionBar.with(this)
            .statusBarColor(R.color.color_fff6f6f6)
            .statusBarDarkFont(true)
            .init()
        setSupportActionBar(binding.toolbar)
        mAdapter = NoteAdapter(NoteComparator, onClick = {
            NoteDetailActivity.actionStart(this, it.uid)
        }, onLongClick = {
            VibratorUtils.shortVibrate(AppConfig.mContext)
        })
        binding.rv.adapter = mAdapter
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
}