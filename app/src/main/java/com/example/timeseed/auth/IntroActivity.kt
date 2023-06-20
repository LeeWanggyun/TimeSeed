package com.example.timeseed.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.example.timeseed.MainActivity
import com.example.timeseed.R
import com.example.timeseed.databinding.ActivityIntroBinding
import com.google.firebase.auth.FirebaseAuth

class IntroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        var currentUser = auth?.currentUser

        if(currentUser == null) {

            binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)

            binding.loginBtn.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

            binding.joinBtn.setOnClickListener {
                val intent = Intent(this, JoinActivity::class.java)
                startActivity(intent)
            }

        } else {

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)

        }

    }
}