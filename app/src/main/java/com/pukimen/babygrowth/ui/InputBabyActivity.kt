package com.pukimen.babygrowth.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.pukimen.babygrowth.R
import com.pukimen.babygrowth.databinding.ActivityAboutBinding
import com.pukimen.babygrowth.databinding.ActivityInputBabyBinding
import com.pukimen.babygrowth.ui.auth.AuthViewModel
import com.pukimen.babygrowth.utils.Results
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class InputBabyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputBabyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBabyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, application)
        val viewModel: AuthViewModel by viewModels { factory }
        val genderOptions = arrayOf("Boy", "Girl")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genderInput.adapter = adapter

        binding.dateInput.setOnClickListener {
            datePicker()
        }

        binding.inputButton.setOnClickListener {
            getStarted(viewModel)
        }
    }

    private fun datePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val selectedDate = dateFormat.format(selectedCalendar.time)
                binding.dateInput.setText(selectedDate)
            },
            year, month, day
        )
        datePicker.show()
    }

    private fun getStarted(viewModel: AuthViewModel) {
//        startActivity(Intent(this, HomeActivity::class.java))
//        finish()
        val dateOfBirth = binding.dateInput.text.toString().trim()
        val weight = binding.weightInput.text.toString().toInt()
        val height = binding.heightInput.text.toString().toInt()
        val name = binding.nameInput.text.toString().trim()
        val gender = binding.genderInput.selectedItem.toString().trim()
        var token = ""
        viewModel.getSession().observe(this) { session ->
            token = session.token
            Log.d("TAG", "getStarted: $token")
        }
        viewModel.update(token, name, dateOfBirth, weight, height, gender).observe(this) { result ->
            when (result) {
                is Results.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Results.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is Results.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", result.error)
                }
            }
        }
    }
}
