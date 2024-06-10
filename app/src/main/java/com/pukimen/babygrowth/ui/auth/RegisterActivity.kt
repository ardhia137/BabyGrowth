package com.pukimen.babygrowth.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pukimen.babygrowth.R
import com.pukimen.babygrowth.databinding.ActivityRegisterBinding
import com.pukimen.babygrowth.ui.ViewModelFactory
import com.pukimen.babygrowth.utils.Results
import com.pukimen.babygrowth.utils.validation

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel: AuthViewModel by viewModels { ViewModelFactory.getInstance(this,application) }


        binding.retypeInput.setUserPasswordField(binding.passwordInput)

        binding.registerButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val retypePassword = binding.retypeInput.text.toString()
            val username = binding.nameInput.text.toString()
            val isValid = validation("RegisterActivity", email, password, this, username, retypePassword)
            if (isValid) {
                viewModel.register(username,email, password).observe(this@RegisterActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is Results.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Results.Success<*> -> {
                                binding.progressBar.visibility = View.GONE
                                AlertDialog.Builder(this).apply {
                                    setTitle("Yeah!")
                                    setMessage("Registration Berhasil")
                                    setPositiveButton("Next") { dialog, _ ->
                                        val intent = Intent(context, LoginActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        context.startActivity(intent)
                                        dialog.dismiss()
                                    }
                                    create().show()
                                }
                            }
                            is Results.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Terjadi kesalahan" + result.error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}