package com.pukimen.babygrowth.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.pukimen.babygrowth.databinding.ActivityMainBinding
import com.pukimen.babygrowth.ui.auth.AuthViewModel
import com.pukimen.babygrowth.ui.auth.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val factory: ViewModelFactory = ViewModelFactory.getInstance(this,application)
        val viewModel: AuthViewModel by viewModels {
            factory
        }


        binding.iv.alpha = 0f
        binding.iv.animate().setDuration(1000).alpha(1f).withEndAction {
            viewModel.getSession().observe(this) { user ->
                if (user.name.isEmpty() && user.isLogin) {
                    startActivity(Intent(this, InputBabyActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                    finish()
                }else if(user.isLogin){
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                    finish()
                }
                else{
                    startActivity(Intent(this, LoginActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                    finish()
                }
            }
        }
    }
}