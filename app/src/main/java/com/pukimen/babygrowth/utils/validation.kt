package com.pukimen.babygrowth.utils

import android.content.Context
import android.widget.Toast

fun validation(activity: String, email: String, password: String, context: Context, name: String?): Boolean {
    return if (activity == "LoginActivity") {
        if (email.isEmpty()) {
            val message = "Please enter your email"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            false
        } else if (password.isEmpty()) {
            val message = "Please enter your password"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    } else {
        if (email.isEmpty()) {
            val message = "Please enter your email"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            false
        } else if (password.isEmpty()) {
            val message = "Please enter your password"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            false
        } else if (name != null && name.isEmpty()) {
            val message = "Please enter your name"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }
}