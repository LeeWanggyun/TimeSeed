package com.example.timeseed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import android.content.Intent
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.os.HandlerCompat.postDelayed
import com.bumptech.glide.Glide
import com.example.timeseed.freinds.FriendsActivity
import com.example.timeseed.myInfo.MyInfoActivity
import com.example.timeseed.store.StoreActivity
import com.example.timeseed.databinding.ActivityMainBinding
import com.example.timeseed.latter.FlowerActivity
import com.example.timeseed.latter.ReadLatterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var nowDate = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toString().substring(0, 10)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val Uuid = auth.currentUser?.uid

        binding.flower.visibility = View.VISIBLE




        firestore.collection("users").document("${Uuid}").collection("씨앗")
            .whereEqualTo("condition", "y")
            .get()
            .addOnSuccessListener { document ->
                for (document in document) {
                    val beforeWaterDate = document["waterDate"].toString()
                    val documentId = document.id
                    val waterNumber = document["waterNum"].toString()
                    val giveWaterNumber = document["giveWaterNum"].toString()
                    var giveWaterNum: Double = giveWaterNumber.toDouble()
                    val waterNum: Double = waterNumber.toDouble()
                    val flower = document["seed"].toString()
                    binding.wateringCan.visibility = View.VISIBLE
                    binding.wateringCan.setOnClickListener {

                        if ("${beforeWaterDate}" == "${nowDate}") {
                            Log.d("kkang", "${beforeWaterDate} , ${nowDate}")
                            Toast.makeText(this, "이미 물을 주었습니다!", Toast.LENGTH_LONG).show()
                        } else {
                            Glide.with(this).load(R.raw.rain).into(binding.rainDrop)
                            Handler().postDelayed(
                                { binding.rainDrop.visibility = View.INVISIBLE },
                                4000
                            )

                            Toast.makeText(this, "물을 주었습니다!", Toast.LENGTH_LONG).show()
                            val waterDate = "${nowDate}"
                            giveWaterNum += 1
                            firestore.collection("users").document("${Uuid}").collection("씨앗")
                                .document("${documentId}").update("giveWaterNum", "${giveWaterNum}")
                            firestore.collection("users").document("${Uuid}").collection("씨앗")
                                .document("${documentId}").update("waterDate", "${waterDate}")

                            Handler().postDelayed({
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                overridePendingTransition(0, 0)
                                finish()
                            }, 5000)

                        }
                    }




                    if (0.0 == giveWaterNum || waterNum * 1 / 3 >= giveWaterNum) {
                        if (flower == "데이지") {
                            binding.flower.setImageResource(R.drawable.sproutpot)
                        } else if (flower == "튤립") {
                            binding.flower.setImageResource(R.drawable.tulipsproutpot)
                        }else if (flower == "해바라기"){
                            binding.flower.setImageResource(R.drawable.sunflowersproutpot)
                        }


                    } else if (waterNum * 1 / 3 < giveWaterNum && waterNum > giveWaterNum) {

                        if (flower == "데이지") {
                            binding.flower.setImageResource(R.drawable.daysimiddlepot)
                        } else if (flower == "튤립") {
                            binding.flower.setImageResource(R.drawable.tulipmiddlepot)
                        }else if (flower == "해바라기"){
                            binding.flower.setImageResource(R.drawable.sunflowermiddlepot)
                        }

                    } else if (waterNum == giveWaterNum) {
                        Toast.makeText(this, "꽃이 폈습니다! 꽃을 터치해 보세요!", Toast.LENGTH_LONG).show()
                        binding.wateringCan.visibility = View.INVISIBLE

                        if (flower == "데이지") {
                            binding.flower.setImageResource(R.drawable.daysiflowerpot)
                        } else if (flower == "튤립") {
                            binding.flower.setImageResource(R.drawable.tuilpflowerpot)
                        }else if (flower == "해바라기"){
                            binding.flower.setImageResource(R.drawable.sunflowerpot)
                        }


//                       firestore.collection("users").document("${Uuid}").collection("씨앗").whereEqualTo("condition","y")
//                           .get()
//                           .addOnSuccessListener { document ->
//                               for (document in document){
//                                   val flower = document["seed"].toString()
//                                   if (flower == "장미"){
//                                       binding.flower3.setImageResource(R.drawable.rose)
//                                   }else if(flower == "데이지"){
//                                       binding.flower3.setImageResource(R.drawable.daysi)
//                                   }else if(flower == "튤립"){
//                                       binding.flower3.setImageResource(R.drawable.tulip)
//                                   }
//                               }
//
//                           }
                            binding.flower.setOnClickListener {
                                var intent = Intent(this, ReadLatterActivity::class.java)
                                startActivity(intent)
                                overridePendingTransition(0, 0)



                        }
                    }
                }



                    binding.imageButton2.setOnClickListener {
                        val intent = Intent(this, StoreActivity::class.java)
                        startActivity(intent)
                    }
                    binding.imageButton3.setOnClickListener {
                        val intent = Intent(this, FriendsActivity::class.java)
                        startActivity(intent)

                    }
                    binding.imageButton4.setOnClickListener {
                        val intent = Intent(this, MyInfoActivity::class.java)
                        startActivity(intent)

                    }
                    binding.flowerBtn.setOnClickListener {
                        val intent = Intent(this, FlowerActivity::class.java)
                        startActivity(intent)

                    }
                binding.shop.setOnClickListener{
                    val intent = Intent(this, ShopActivity::class.java)
                    startActivity(intent)
                }

                }

            }
        var lastTimeBackPressed: Long = 0
        override fun onBackPressed() {
            if (System.currentTimeMillis() - lastTimeBackPressed >= 1500) {
                lastTimeBackPressed = System.currentTimeMillis()
                Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG).show()
            } else {
                ActivityCompat.finishAffinity(this)
                System.runFinalization()
                exitProcess(0)
            }
        }


}


