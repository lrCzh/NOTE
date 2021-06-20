package com.czh.note.ui.base

import androidx.fragment.app.Fragment
import com.czh.note.ui.widget.LoadingDialog

class BaseFragment : Fragment() {

    private var loadingDialog: LoadingDialog? = null

    fun showLoadingDialog(msg: String? = null, cancelable: Boolean = true) {
        loadingDialog?.dismiss()
        loadingDialog = LoadingDialog(msg, cancelable).also {
            it.show(childFragmentManager, "loading")
        }
    }

    fun hideLoadingDialog() {
        loadingDialog?.dismiss()
    }
}