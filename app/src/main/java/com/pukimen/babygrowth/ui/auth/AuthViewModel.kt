package com.pukimen.babygrowth.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pukimen.babygrowth.data.model.UserModel
import com.pukimen.babygrowth.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun login(email:String,password:String) = authRepository.login(email, password)
    fun register(name:String,email:String,password:String) = authRepository.register(name,email, password)
    fun getSession(): LiveData<UserModel> {
        return authRepository.getSession().asLiveData()
    }
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

}