package com.example.timeseed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.timeseed.databinding.ActivityShopBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ShopActivity : AppCompatActivity() {
    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityShopBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val Uuid = auth.currentUser?.uid
        firestore = FirebaseFirestore.getInstance()

        binding.backBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        firestore.collection("users").document("${Uuid}")
            .collection("flowerSeeds").whereEqualTo("name", "튤립").get()
            .addOnSuccessListener { document ->
                for (document in document){
                    val tulipC = document["condition"].toString()
                    if (tulipC == "y"){
                        binding.tulipBtn.visibility = View.INVISIBLE
                    }
                }
            }

        firestore.collection("users").document("${Uuid}")
            .collection("flowerSeeds").whereEqualTo("name", "해바라기").get()
            .addOnSuccessListener { document ->
                for (document in document){
                    val tulipC = document["condition"].toString()
                    if (tulipC == "y"){
                        binding.sunflowerBtn.visibility = View.INVISIBLE
                    }
                }
            }

        firestore.collection("users").document("${Uuid}").get()
            .addOnSuccessListener { documnet ->
                val myPoint = documnet["point"].toString()
                binding.point.setText(myPoint)
            }

        binding.tulipBtn.setOnClickListener{
           firestore.collection("users").document("${Uuid}").get()
               .addOnSuccessListener { document ->
                   val myPoint = document["point"].toString()
                   val myPointInt : Int = myPoint.toInt()
                   if(myPointInt >= 1 ){
                       firestore.collection("users").document("${Uuid}")
                           .collection("flowerSeeds").whereEqualTo("name", "튤립").get()
                           .addOnSuccessListener { document ->
                             for (document in document){
                                 val documentId = document.id
                                 firestore.collection("users").document("${Uuid}")
                                     .collection("flowerSeeds").document("${documentId}").update("condition","y")

                                 firestore.collection("users").document("${Uuid}").update("point","${myPointInt-1}")
                                 Toast.makeText(this, "구매 성공!",Toast.LENGTH_LONG).show()
                                 val intent = Intent(this, ShopActivity::class.java)
                                 startActivity(intent)
                                 overridePendingTransition(0, 0)
                                 binding.tulipBtn.visibility = View.INVISIBLE

                             }
                           }
                   }else if (myPointInt < 1){
                       Toast.makeText(this, "포인트가 부족합니다",Toast.LENGTH_LONG).show()
                   }
               }
        }

        binding.sunflowerBtn.setOnClickListener {
            firestore.collection("users").document("${Uuid}").get()
                .addOnSuccessListener { document ->
                    val myPoint = document["point"].toString()
                    val myPointInt : Int = myPoint.toInt()
                    if(myPointInt >= 1 ){
                        firestore.collection("users").document("${Uuid}")
                            .collection("flowerSeeds").whereEqualTo("name", "해바라기").get()
                            .addOnSuccessListener { document ->
                                for (document in document){
                                    val documentId = document.id
                                    firestore.collection("users").document("${Uuid}")
                                        .collection("flowerSeeds").document("${documentId}").update("condition","y")

                                    firestore.collection("users").document("${Uuid}").update("point","${myPointInt-1}")
                                    Toast.makeText(this, "구매 성공!",Toast.LENGTH_LONG).show()
                                    binding.sunflowerBtn.visibility = View.INVISIBLE
                                    val intent = Intent(this, ShopActivity::class.java)
                                    startActivity(intent)
                                    overridePendingTransition(0, 0)

                                }
                            }
                    }else if (myPointInt < 1){
                        Toast.makeText(this, "포인트가 부족합니다",Toast.LENGTH_LONG).show()
                    }
                }

        }

    }
    override fun onBackPressed() {
//        super.onBackPressed()

    }
}