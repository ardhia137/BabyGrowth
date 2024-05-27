package com.pukimen.babygrowth.ui.customView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import java.util.regex.Pattern

class EditTextEmail : AppCompatEditText {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var errorMessage: String? = null

    private val emailPattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
    )



    private val emailTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                val text = it.toString()
                if (!isValidEmail(text)) {
                    setError(errorMessage ?: "Invalid email format")
                } else {
                    setError(null)
                }
            }
        }
    }
    init {
        addTextChangedListener(emailTextWatcher)
    }
    private fun isValidEmail(email: String): Boolean {
        return emailPattern.matcher(email).matches()
    }

    fun setErrorMessage(message: String) {
        errorMessage = message
    }
}
