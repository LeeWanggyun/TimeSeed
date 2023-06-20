package com.example.timeseed.latter

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.timeseed.MainActivity
import com.example.timeseed.R
import com.example.timeseed.databinding.ActivityLatterBinding
import com.example.timeseed.freinds.FriendsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.coroutines.EmptyCoroutineContext.plus

class LatterActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        val binding = ActivityLatterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val name = intent.getStringExtra("name")
        val Uuid = auth.currentUser?.uid

        val special = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss"))
        val friendName = findViewById<TextView>(R.id.friendName)
        friendName.setText(name)

        binding.flowerPicBtn.setOnClickListener {
            firestore.collection("users").document("${Uuid}").collection("flowerSeeds")
                .whereEqualTo("condition", "y")
                .get()
                .addOnSuccessListener { document ->
                    var items = arrayOf<String>()
                    for (document in document) {
                        items = items.plus(document["name"].toString())
                    }
                    AlertDialog.Builder(this).run {
                        setTitle("Flower Pic")
                        setSingleChoiceItems(items, 1, object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                Log.d("Lee", "${items[p1]}")
                                binding.flowerName.setText("${items[p1]}")
                            }
                        })
                        setPositiveButton("확인", null)
                        show()
                    }
                }

        }






        binding.sendBtn.setOnClickListener {
            var latter = binding.latter.text.toString()
            var water = binding.waterNumber.text.toString()
            var flowerName = binding.flowerName.text.toString()
            if (latter == "" || water == "" || flowerName == "") {
                Toast.makeText(this@LatterActivity, "빈 내용이 있는지 확인 해 주세요", Toast.LENGTH_LONG).show()

            } else {
                val seed = binding.flowerName.text.toString()
                val latter = binding.latter.text.toString()
                val water = binding.waterNumber.text.toString()
                firestore.collection("users")
                    .whereEqualTo("uid", "${Uuid}")
                    .get()
                    .addOnSuccessListener { document ->
                        for (document in document) {
                            val userName = document["name"].toString()
                            val userEmail = document["email"].toString()
                            val seed = mapOf(
                                "name" to "${userName}",
                                "latter" to "${latter}",
                                "sendDate" to "${LocalDate.now()}",
                                "email" to "${userEmail}",
                                "special" to "${userEmail}${special}",
                                "condition" to "n",
                                "waterNum" to "${water}",
                                "giveWaterNum" to "0",
                                "seed" to "${seed}"
                            )
                            firestore.collection("users")
                                .whereEqualTo("name", "${name}")
                                .get()
                                .addOnSuccessListener { document ->
                                    for (document in document) {

                                        val friendUid = document["uid"].toString()
                                        firestore.collection("users").document("${friendUid}")
                                            .collection("씨앗")
                                            .document()
                                            .set(seed)
                                    }
                                    val intent = Intent(this, FriendsActivity::class.java)
                                    startActivity(intent)
                                }
                        }
                    }
            }
        }
    }
}

