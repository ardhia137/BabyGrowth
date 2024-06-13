package com.pukimen.babygrowth.ui


import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private var currentImageUri: Uri? = null

    private lateinit var scanViewModel: ScanViewModel
    private var umur = 0

    @RequiresApi(Build.VERSION_CODES.O)
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
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadImage(authViewModel: AuthViewModel) {
        authViewModel.getSession().observe(this) { session ->
            var token = session.token
            umur = calculateAgeInMonths(session.birthDay).toInt()
            currentImageUri?.let { uri ->
                scanViewModel.uploadImage(token, uri, this).observe(this) { results ->
                    when (results) {
                        is Results.Loading -> {
                            showLoading(true)
                            binding.svScan.visibility = View.GONE
                        }
                        is Results.Success -> {
                            val successResponse = results.data

                            var kategoriUsia = when {
                                umur <= 6 -> 1
                                umur <= 11 -> 2
                                else -> 3
                            }

                            with(successResponse.data) {
                                val (cardBackgroundColor, suitabilityText) = when {
                                    label.equals("Lumat", ignoreCase = true) && kategoriUsia == 1 -> Pair(ContextCompat.getColor(this@ScanActivity, R.color.green), "Suitable for your baby")
                                    label.equals("Lunak", ignoreCase = true) && kategoriUsia == 2 -> Pair(ContextCompat.getColor(this@ScanActivity, R.color.green), "Suitable for your baby")
                                    label.equals("Kasar", ignoreCase = true) && kategoriUsia == 3 -> Pair(ContextCompat.getColor(this@ScanActivity, R.color.green), "Suitable for your baby")
                                    label.equals("Bukan Makanan", ignoreCase = true) -> Pair(ContextCompat.getColor(this@ScanActivity, R.color.red), "Please enter the correct food image")
                                    else -> Pair(ContextCompat.getColor(this@ScanActivity, R.color.yellow), "Not Suitable for your baby")
                                }
                                val resultText = "$label, $suitabilityText"
                                binding.resultText.text = resultText
                                binding.cvAnalyze.setCardBackgroundColor(cardBackgroundColor)
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
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateAgeInMonths(birthday: String?): Long {
        return if (!birthday.isNullOrEmpty()) {
            try {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val birthDate = LocalDate.parse(birthday, formatter)
                val currentDate = LocalDate.now()
                ChronoUnit.MONTHS.between(birthDate, currentDate)
            } catch (e: Exception) {
                // Log the error if needed
                0
            }
        } else {
            0
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}