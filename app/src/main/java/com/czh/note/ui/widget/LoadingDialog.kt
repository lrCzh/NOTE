package com.czh.note.ui.widget

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.czh.note.R

class LoadingDialog(
    private val msg: String? = null,
    private val cancelable: Boolean = true
) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.loadingDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView = view.findViewById<TextView>(R.id.tv_loading_hint)
        if (TextUtils.isEmpty(msg)) {
            textView.visibility = View.GONE
        } else {
            textView.text = msg
        }
        if (!cancelable) {
            isCancelable = false
        }
    }
}