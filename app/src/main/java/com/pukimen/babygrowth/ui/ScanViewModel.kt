package com.pukimen.babygrowth.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pukimen.babygrowth.data.remote.response.PredictResponse
import com.pukimen.babygrowth.data.repository.ScanRepository
import com.pukimen.babygrowth.utils.Results
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ScanViewModel(private val repository: ScanRepository) : ViewModel() {

    private val _uploadResult = MutableLiveData<Results<PredictResponse>>()
    val uploadResult: LiveData<Results<PredictResponse>> get() = _uploadResult

//    fun uploadImage(file: MultipartBody.Part) {
//        viewModelScope.launch {
//            _uploadResult.value = Results.Loading
//            _uploadResult.value = repository.uploadImage(file)
//        }
//    }

    fun uploadImage(token:String,image: Uri,context: Context) = repository.uploadImage(token,image,context)

}