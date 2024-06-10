package com.pukimen.babygrowth.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pukimen.babygrowth.R
import com.pukimen.babygrowth.databinding.ActivityEditBinding
import com.pukimen.babygrowth.ui.auth.AuthViewModel
import com.pukimen.babygrowth.ui.auth.LoginActivity
import com.pukimen.babygrowth.ui.bottomNav.FoodViewModel
import com.pukimen.babygrowth.utils.Results

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, application)
        val viewModel: AuthViewModel by viewModels { factory }
        var token = ""
        var birthDay = ""
        var gender = ""
        viewModel.getSession().observe(this) {
                token = it.token
                birthDay = it.birthDay
                gender = it.gender
                Log.d("EditActivity", "onCreate: Name: ${it.name}, Weight: ${it.weight}, Height: ${it.height}")
                binding.nameEditTextLayout.editText?.setText(it.name)
                binding.heightEditTextLayout.editText?.setText(it.height.toString())
                binding.weightEditTextLayout.editText?.setText(it.weight.toString())
        }
        binding.editButton.setOnClickListener {
            val name = binding.nameEditTextLayout.editText?.text.toString()
            val height = binding.heightEditTextLayout.editText?.text.toString().toInt()
            val weight = binding.weightEditTextLayout.editText?.text.toString().toInt()

            viewModel.update(token,name,birthDay,height,weight,gender).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Results.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Results.Success -> {
                            binding.progressBar.visibility = View.GONE
                            AlertDialog.Builder(this).apply {
                                setTitle("Yeah!")
                                setMessage("Edit Successfully")
                                setPositiveButton("Ok") { dialog, _ ->
                                    val intent = Intent(context, HomeActivity::class.java)
                                    context.startActivity(intent)
                                    dialog.dismiss()
                                }
                                create().show()
                            }
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
    }
}
