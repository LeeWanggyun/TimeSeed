package com.example.timeseed.latter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.timeseed.R
import com.example.timeseed.databinding.ActivityReadFlowerLatterBinding
import com.example.timeseed.databinding.ActivityReadLatterBinding

class ReadFlowerLatterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityReadFlowerLatterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val latter = intent.getStringExtra("latter")
        val date = intent.getStringExtra("sendDate")

        binding.latter.setText("${latter}")
        binding.name.setText("${name}")
        binding.date.setText("${date}")

        binding.yesBtn.setOnClickListener{
            val intent = Intent(this, FlowerActivity::class.java)
            startActivity(intent)
        }



    }
    override fun onBackPressed() {
        Toast.makeText(this,"확인 버튼을 눌러주세요", Toast.LENGTH_LONG).show()
//        super.onBackPressed()

    }
}