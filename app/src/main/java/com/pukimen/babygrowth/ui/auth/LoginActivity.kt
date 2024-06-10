package com.pukimen.babygrowth.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pukimen.babygrowth.R
import com.pukimen.babygrowth.data.model.UserModel
import com.pukimen.babygrowth.ui.ViewModelFactory
import com.pukimen.babygrowth.databinding.ActivityLoginBinding
import com.pukimen.babygrowth.ui.HomeActivity
import com.pukimen.babygrowth.ui.InputBabyActivity
import com.pukimen.babygrowth.ui.customView.EditTextPassword
import com.pukimen.babygrowth.utils.Results
import com.pukimen.babygrowth.utils.validation

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this,application)
        val viewModel: AuthViewModel by viewModels { factory }
        binding.signUpText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val isValid = validation("LoginActivity", email, password, this, null)
            if (isValid) {
                viewModel.login(email, password).observe(this@LoginActivity) { result ->
                    if(result != null){
                        when(result){
                            is Results.Loading ->{
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Results.Success<*> -> {
                                val loginResponse = result.data as UserModel
                                if (loginResponse.name.isEmpty()) {
                                    val intent = Intent(this@LoginActivity, InputBabyActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                            is Results.Error -> {
                                binding.progressBar.visibility = View.GONE
                                if (result.error == "Unauthorized"){
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Email atau password salah",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }else{
                                    Log.e("LoginActivity", result.error)
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Terjadi kesalahan",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                        }
                    }
                }
            }
        }

        binding.signUpText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

//        val visibilityToggleButton: EditTextPassword = findViewById(R.id.passwordInput)
//        val visibilityToggleOff = ContextCompat.getDrawable(this, R.drawable.ic_invisibility)!!
//        visibilityToggleOff.setBounds(0, 0, visibilityToggleOff.intrinsicWidth, visibilityToggleOff.intrinsicHeight)
//        visibilityToggleButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, visibilityToggleOff, null)
    }
}