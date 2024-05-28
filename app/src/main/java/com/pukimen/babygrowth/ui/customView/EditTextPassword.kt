package com.pukimen.babygrowth.ui.customView

import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class EditTextPassword : AppCompatEditText {

    var isCharacterPasswordValid = false

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
        // Mengatur inputType untuk menampilkan teks sebagai titik-titik
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        typeface = Typeface.DEFAULT // Menetapkan jenis font default yang sama dengan EditText biasa

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    isCharacterPasswordValid = s.toString().length > 7
                    error = if (isCharacterPasswordValid) null else "Minimum 8 Characters"
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}
