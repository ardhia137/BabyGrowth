package com.pukimen.babygrowth

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.pukimen.babygrowth.R

class EditTextPassword : AppCompatEditText {

    private val minLength = 8  // Panjang minimum password

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        initToggleListener()
        addTextChangedListener(passwordTextWatcher)
    }

    private fun initToggleListener() {
        Log.d("VisibilityToggleButton", "Init Toggle Listener dipanggil")
        val visibilityToggleOff: Drawable =
            ContextCompat.getDrawable(context, R.drawable.ic_invisibility)!!
        visibilityToggleOff.setBounds(
            0, 0, visibilityToggleOff.intrinsicWidth, visibilityToggleOff.intrinsicHeight
        )

        val visibilityToggleOn: Drawable =
            ContextCompat.getDrawable(context, R.drawable.ic_visibility)!!
        visibilityToggleOn.setBounds(
            0, 0, visibilityToggleOn.intrinsicWidth, visibilityToggleOn.intrinsicHeight
        )

        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableRight = compoundDrawablesRelative[2]
                Log.d("VisibilityToggleButton", "Sentuhan terdeteksi $drawableRight")
                if (drawableRight != null && event.rawX >= right - drawableRight.bounds.width()) {
                    Log.d("VisibilityToggleButton", "Tombol visibilitas ditekan")
                    toggleVisibility(visibilityToggleOff, visibilityToggleOn)
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun toggleVisibility(visibilityToggleOff: Drawable, visibilityToggleOn: Drawable) {
        if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, visibilityToggleOff, null)
        } else {
            inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, visibilityToggleOn, null)
        }
    }

    private val passwordTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let {
                if (it.length < minLength) {
                    error = "Password must be at least $minLength characters long"
                } else {
                    error = null
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }
}