package com.pukimen.babygrowth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pukimen.babygrowth.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val factory: ViewModelFactoryFactory = ViewModelFactory.getInstance(this)
//        val viewModel: AuthViewModel AuthViewModelby viewModels { factory }

//        binding.btnLogin.setOnClickListener {
//            val email = binding.edLoginEmail.text.toString()
//            val password = binding.edLoginPassword.text.toString()
//            val isValid = validation("LoginActivity", email, password, this, null)
//            if (isValid) {
//                viewModel.login(email, password).observe(this@LoginActivity) { result ->
//                    if(result != null){
//                        when(result){
//                            is Results.Loading ->{
//                                binding.progressBar.visibility = View.VISIBLE
//                            }
//                            is Results.Success<*> -> {
//                                binding.progressBar.visibility = View.GONE
//                                val intent = Intent(this, HomeActivity::class.java)
//                                startActivity(intent)
//                                finish()
//                            }
//                            is Results.Error -> {
//                                binding.progressBar.visibility = View.GONE
//                                Toast.makeText(
//                                    this@LoginActivity,
//                                    "Terjadi kesalahan" + result.error,
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        binding.register.setOnClickListener {
//            val intent = Intent(this, RegisterActivity::class.java)
//            startActivity(intent)
//        }

        val visibilityToggleButton: EditTextPassword = findViewById(R.id.passwordInput)
        val visibilityToggleOff = ContextCompat.getDrawable(this, R.drawable.ic_invisibility)!!
        visibilityToggleOff.setBounds(0, 0, visibilityToggleOff.intrinsicWidth, visibilityToggleOff.intrinsicHeight)
        visibilityToggleButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, visibilityToggleOff, null)
    }
}