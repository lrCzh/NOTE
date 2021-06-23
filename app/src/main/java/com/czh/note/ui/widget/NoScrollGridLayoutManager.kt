package com.czh.note.ui.widget

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class NoScrollGridLayoutManager(context: Context, spanCount: Int) :
    GridLayoutManager(context, spanCount) {
    override fun canScrollVertically(): Boolean {
        return false
    }

    override fun canScrollHorizontally(): Boolean {
        return false
    }
}