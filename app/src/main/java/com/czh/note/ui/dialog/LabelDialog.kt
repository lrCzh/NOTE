package com.czh.note.ui.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.czh.note.R
import com.czh.note.databinding.DialogLabelsBinding
import com.czh.note.ui.adapter.LabelAdapter
import com.czh.note.ui.widget.NoScrollGridLayoutManager
import com.czh.note.util.getArray

class LabelDialog : DialogFragment() {

    companion object {
        private const val TYPE = "type"
        const val TYPE_WEATHER = 1
        const val TYPE_MOOD = 2

        fun newInstance(type: Int): LabelDialog {
            return LabelDialog().apply {
                arguments = Bundle().apply {
                    putInt(TYPE, type)
                }
            }
        }
    }

    private var binding: DialogLabelsBinding? = null
    private lateinit var mAdapter: LabelAdapter

    private var dismissCallback: ((label: String?) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogLabelsBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWindow()
        initView()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initWindow() {
        dialog?.window?.apply {
            setBackgroundDrawableResource(R.color.transparent)
            setWindowAnimations(R.style.ActionSheetDialogAnimation)
            attributes.gravity = Gravity.BOTTOM
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    private fun initView() {
        binding?.apply {
            mAdapter = LabelAdapter {
                dismissWithCallback(it)
            }
            rvLabel.layoutManager = NoScrollGridLayoutManager(requireContext(), 4)
            rvLabel.adapter = mAdapter
        }
    }

    private fun initData() {
        binding?.apply {
            val type = arguments?.getInt(TYPE, TYPE_WEATHER)
            when (type) {
                TYPE_WEATHER -> {
                    tvTitle.text = getString(R.string.en_select_weather_hint)
                    (rvLabel.adapter as LabelAdapter).setData(getArray(R.array.note_weather).toMutableList())
                }
                TYPE_MOOD -> {
                    tvTitle.text = getString(R.string.en_select_mood_hint)
                    (rvLabel.adapter as LabelAdapter).setData(getArray(R.array.note_mood).toMutableList())
                }
            }
        }
    }

    private fun dismissWithCallback(label: String?) {
        dismiss()
        dismissCallback?.invoke(label)
    }

    fun setDismissCallback(callback: (label: String?) -> Unit) {
        this.dismissCallback = callback
    }
}