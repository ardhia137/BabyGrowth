package com.pukimen.babygrowth.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pukimen.babygrowth.data.repository.AuthRepository
import com.pukimen.babygrowth.di.Injection
import com.pukimen.babygrowth.ui.auth.AuthViewModel

class ViewModelFactory private constructor(private val loginRepository: AuthRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(loginRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }


    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                val authRepository = Injection.provideAuthRepository(context)
//                val stroyRepository = Injection.provideStroyRepository(context)
                instance ?: ViewModelFactory(authRepository
                )
            }.also { instance = it }
    }
}