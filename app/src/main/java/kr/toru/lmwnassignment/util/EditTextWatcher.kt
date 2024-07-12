package kr.toru.lmwnassignment.util

import android.text.TextWatcher

class EditTextWatcher(
    private val postAction: ((String)-> Unit)? = null
): TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    override fun afterTextChanged(s: android.text.Editable?) {
        postAction?.invoke(s.toString())
    }
}