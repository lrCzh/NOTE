package com.czh.note.ui.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class NoteItemDecoration(private val size: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.right = size
        outRect.bottom = size
        if (parent.getChildAdapterPosition(view) <= 1) {
            outRect.top = size
        }
        if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.left = size
        }
    }
}