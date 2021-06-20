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
import com.czh.note.ui.adapter.NoteAdapter
import com.czh.note.ui.adapter.NoteComparator
import com.czh.note.ui.base.BaseActivity
import com.czh.note.databinding.ActivityMainBinding
import com.czh.note.db.AppDatabase
import com.czh.note.db.Note
import com.czh.note.vm.MainViewModel
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

        setSupportActionBar(binding.toolbar)
        mAdapter = NoteAdapter(NoteComparator) {
            NoteDetailActivity.actionStart(this, it.uid)
        }
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
            vm.addNote(
                Note(
                    title = "qwer",
                    date = "2003/03/03",
                    description = "HSDDFHFDFDSFUASSUFDSABFDUFDUFDFNDUJFHHSDDFHFDFDSFUASSUFDSABFDUFDUFDFNDUJFHDUFHDFUDSFGFYAGYSGFYUSGFUYAHSDDFHFDFDSFUASSUFDSABFDUFDUFDFNDUJFHDUFHDFUDSFGFYAGYSGFYUSGFUYADUFHDFUDSFGFYAGYSGFYUSGFUYA",
                    weather = "多云",
                    mood = "开心"
                )
            )
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

            R.id.about -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
}