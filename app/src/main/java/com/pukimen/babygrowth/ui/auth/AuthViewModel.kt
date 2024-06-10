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
    fun register(username:String,email:String,password:String) = authRepository.register(username,email, password)

    fun update(token:String,name:String,birthday:String,height:Int,weight:Int,gender:String) = authRepository.update(token,name,birthday,height,weight,gender)
    fun getSession(): LiveData<UserModel> {
        return authRepository.getSession().asLiveData()
    }
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

}