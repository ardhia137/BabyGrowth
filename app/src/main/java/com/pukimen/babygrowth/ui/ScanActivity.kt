package com.pukimen.babygrowth.ui


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.pukimen.babygrowth.R
import com.pukimen.babygrowth.databinding.ActivityScanBinding
import com.pukimen.babygrowth.ui.auth.AuthViewModel
import com.pukimen.babygrowth.utils.Results
import com.pukimen.babygrowth.utils.reduceFileImage
import com.pukimen.babygrowth.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody


class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private var currentImageUri: Uri? = null

    private lateinit var scanViewModel: ScanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = ViewModelFactory.getInstance(this, application)
        scanViewModel = ViewModelProvider(this, viewModelFactory).get(ScanViewModel::class.java)
        val authViewModel: AuthViewModel by viewModels { viewModelFactory }
        val imageUriString = intent.getStringExtra("IMAGE_URI")
        if (imageUriString != null) {
            currentImageUri = Uri.parse(imageUriString)
            binding.ivScan.setImageURI(currentImageUri)
            uploadImage(authViewModel)
        } else {
            showToast(getString(R.string.empty_image_warning))
            finish()
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun uploadImage(authViewModel: AuthViewModel) {
        authViewModel.getSession().observe(this){
            var token = it.token
            currentImageUri?.let { it1 -> scanViewModel.uploadImage(token, it1, this).observe(this){ results ->
                when (results) {
                    is Results.Loading -> {
                        showLoading(true)
                        binding.svScan.visibility = View.GONE
                    }
                    is Results.Success -> {
                        val successResponse = results.data
                        with(successResponse.data) {
                            val resultText = "$label, $suggestion"
                            binding.resultText.text = resultText
                            showToast(successResponse.message)
                        }
                        binding.svScan.visibility = View.VISIBLE
                        showLoading(false)
                    }
                    is Results.Error -> {
                        showToast(results.error)
                        showLoading(false)
                        binding.svScan.visibility = View.VISIBLE
                        Log.e("ScanActivity", "Error: ${results.error}")
                    }
                }
            } }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}