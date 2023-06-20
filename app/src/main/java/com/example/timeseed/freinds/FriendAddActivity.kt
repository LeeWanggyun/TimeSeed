package com.example.timeseed.freinds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timeseed.databinding.ActivityFriendAddBinding
import com.example.timeseed.databinding.FriendRvItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FriendAddActivity : AppCompatActivity() {
    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        var binding = ActivityFriendAddBinding.inflate(layoutInflater)
        var binding2 = FriendRvItemBinding.inflate(layoutInflater)
        val itemList = arrayListOf<User>()
        val adapter = FriendRVAdapter(itemList, baseContext)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val Uuid = auth.currentUser?.uid

//        val adapter = binding.Frv.adapter
        firestore = FirebaseFirestore.getInstance()
        binding.Frv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.Frv.adapter = adapter

        binding.backBtn.setOnClickListener{
            val intent = Intent(this, FriendsActivity::class.java)
            startActivity(intent)
        }



        binding.serachBtn.setOnClickListener {

            var friendName = binding.searchUserEmail.text.toString()

            Log.d("kkang", "${friendName}")
            firestore.collection("users").whereEqualTo("email", "${friendName}")
                .get()
                .addOnSuccessListener { document ->
                    for (document in document) {
                        val friendUid = document["uid"].toString()
                        val userEmail = document["email"].toString()
                        Log.w("kkang", "${friendUid}${userEmail}")
                        firestore.collection("users").document("${friendUid}").collection("친구")
                            .get()
                            .addOnSuccessListener { document ->
                                val arr = arrayListOf<String>()
                                for (document in document) {
                                    arr.add(document["uid"].toString())
                                }
                                val boolean_pandan = arr.contains("${Uuid}")
                                Log.w("bool", "${boolean_pandan}")
                                if (boolean_pandan == true) {
                                    Toast.makeText(this, "이미 친구 요청 중 입니다", Toast.LENGTH_LONG)
                                        .show()
                                }else if(userEmail == friendName) {
                                    Toast.makeText(this, "자신은 추가 할 수 없습니다", Toast.LENGTH_LONG)
                                        .show()
                                } else {
                                    firestore.collection("users")
                                        .whereEqualTo("email", "${userEmail}")
                                        .get()
                                        .addOnSuccessListener { result ->
                                            itemList.clear()
                                            for (documents in result) {
                                                Log.w("kkang", "${userEmail}")
                                                val item = User(
                                                    documents["name"].toString(),
                                                    documents["email"].toString()
                                                )
                                                Log.w("User", "${item}")
                                                itemList.add(item)

                                            }
                                            adapter.notifyDataSetChanged()
                                        }



                                }


                            }


                    }
                }

        }
    }


}