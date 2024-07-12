package kr.toru.lmwnassignment.util

import android.text.TextWatcher
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

class EditTextWatcher(
    private val postAction: ((String)-> Unit)? = null
): TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    override fun afterTextChanged(s: android.text.Editable?) {
        postAction?.invoke(s.toString())
    }
}

fun EditText.textChangesToFlow(): Flow<CharSequence?> {
    return callbackFlow {
        val listener = doOnTextChanged { text, start, before, count ->
            trySend(text)
        }
        awaitClose { removeTextChangedListener(listener) }
    }.onStart {
        emit(text)
    }
}