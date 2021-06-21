package com.czh.note.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.czh.note.R

class LabelAdapter(private val selectedListener: ((label: String) -> Unit)) :
    RecyclerView.Adapter<LabelAdapter.ViewHolder>() {

    private val mLabels = ArrayList<String>()
    private var mSelected: Int = -1 // 选中的item

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_label, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mLabels.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv.text = mLabels[position]
        holder.tv.isSelected = position == mSelected
        holder.itemView.setOnClickListener {
            select(position)
        }
    }

    fun setData(data: List<String>) {
        mSelected = -1
        mLabels.clear()
        mLabels.addAll(data)
        notifyDataSetChanged()
    }

    fun select(position: Int) {
        if (position in 0 until itemCount) {
            mSelected = position
            notifyDataSetChanged() //数据量少，直接刷新整个列表
            selectedListener.invoke(mLabels[position])
        }
    }

    fun getSelected(): String? {
        return if (mSelected == -1) {
            null
        } else {
            mLabels[mSelected]
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv: TextView = itemView.findViewById(R.id.tv)
    }
}