package com.example.timeseed.latter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.timeseed.MainActivity
import com.example.timeseed.R
import com.example.timeseed.databinding.ActivityReadLatterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ReadLatterActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {

        val binding = ActivityReadLatterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val Uuid = auth.currentUser?.uid


        firestore.collection("users").document("${Uuid}").collection("씨앗")
            .whereEqualTo("condition", "y")
            .get()
            .addOnSuccessListener { document ->
                for (document in document) {
                    val documentId = document.id
                    val latter = document["latter"].toString()
                    val fName = document["name"].toString()
                    val sendDate = document["sendDate"].toString()
                    binding.fName.setText("${fName}")
                    binding.latter.setText("${latter}")
                    binding.sDate.setText("${sendDate}")
                    binding.yesBtn.setOnClickListener{
                        firestore.collection("users").document("${Uuid}")
                            .collection("씨앗").document("${documentId}")
                            .update("condition","f")
                        firestore.collection("users").document("${Uuid}").get()
                            .addOnSuccessListener { document ->
                                val myPoint = document["point"].toString()
                                val myPointInt : Int = myPoint.toInt()
                                firestore.collection("users").document("${Uuid}").update("point","${myPointInt + 1}")

                            }
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(0, 0)

                    }

                }
            }
    }

    override fun onBackPressed() {
        Toast.makeText(this,"확인 버튼을 눌러주세요",Toast.LENGTH_LONG).show()
//        super.onBackPressed()
    }
}