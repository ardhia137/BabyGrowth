package com.pukimen.babygrowth.ui.customView

import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout

class EditTextRetype: AppCompatEditText {

    private var password: EditTextPassword? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        typeface = Typeface.DEFAULT



        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    fun setUserPasswordField(passwordField: EditTextPassword) {
        password = passwordField
        validatePassword()
    }

    private fun validatePassword() {
        val userPassword = password?.text.toString()
        val retypePassword = this.text.toString()

        val textInputLayout = parent.parent as? TextInputLayout
        if (retypePassword.isNotEmpty() && userPassword != retypePassword) {
            textInputLayout?.error = "Passwords don't match"
        } else {
            textInputLayout?.error = null
        }
    }
}