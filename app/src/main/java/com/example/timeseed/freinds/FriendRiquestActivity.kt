package com.example.timeseed.freinds

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timeseed.auth.IntroActivity
import com.example.timeseed.databinding.ActivityFriendRiquestBinding
import com.example.timeseed.store.MyAdapter
import com.example.timeseed.store.Seed
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FriendRiquestActivity : AppCompatActivity() {
    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityFriendRiquestBinding.inflate(layoutInflater)
        val itemList2 = arrayListOf<Riquest>()
        val adapter = FriendRiquestAdapter(itemList2,baseContext)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener{
            val intent = Intent(this@FriendRiquestActivity,FriendsActivity::class.java )
            startActivity(intent)
        }
        auth = FirebaseAuth.getInstance()
        val Uuid = auth.currentUser?.uid
        binding.frrv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.frrv.adapter=adapter
        firestore = FirebaseFirestore.getInstance()
        adapter.itemClick = object : FriendRiquestAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val eventHendler = object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        if (p1 == DialogInterface.BUTTON_POSITIVE) {
                            Handler().postDelayed({
                                val intent = Intent(this@FriendRiquestActivity,FriendRiquestActivity::class.java)
                                startActivity(intent)
                                finish()
                                overridePendingTransition(0, 0)
                            }, 1000)
                            firestore.collection("users").document("${Uuid}").collection("친구")
                                .get()
                                .addOnSuccessListener {document ->
                                    for (document in document){
                                        var condition = document["condition"].toString()

                                            firestore.collection("users").document("${Uuid}").collection("친구")
                                                .whereEqualTo("condition","n")
                                                .get()
                                                .addOnSuccessListener{document ->
                                                    for(document in document){
                                                        val documentid = document.id
                                                        firestore.collection("users").document("${Uuid}")
                                                            .collection("친구").document("${documentid}")
                                                            .update("condition","y")
                                                        val friendName = itemList2[position].name
                                                        firestore.collection("users").whereEqualTo("name","${friendName}").get()
                                                            .addOnSuccessListener { document ->
                                                                for (document in document){
                                                                    val friendUid = document["uid"].toString()
                                                                    firestore.collection("users").whereEqualTo("uid","${Uuid}").get()
                                                                        .addOnSuccessListener { document ->
                                                                            for ( document in document){
                                                                                val user = mapOf(
                                                                                    "name" to document["name"].toString(),
                                                                                    "uid" to document["uid"].toString(),
                                                                                    "email" to document["email"].toString(),
                                                                                    "condition" to "y"
                                                                                )
                                                                                firestore.collection("users").document("${friendUid}").collection("친구").document("${Uuid}").set(user)
                                                                            }
                                                                        }




                                                                }
                                                            }


                                                }

                                        }

                                    }
                                }


                        } else (p1 == DialogInterface.BUTTON_NEGATIVE)
                    }
                }
                AlertDialog.Builder(this@FriendRiquestActivity).run {
                    setTitle("TimeSeed")
                    setMessage("${itemList2[position].name}의 친구요청을 받을까요?")
                    setPositiveButton("네", eventHendler)
                    setNegativeButton("아니오", null)
                    show()
                }
            }
        }

        firestore.collection("users").document("${Uuid}").collection("친구")
            .whereEqualTo("condition", "n")
            .get() .addOnSuccessListener { document ->
                itemList2.clear()
                for (document in document){
                    val item = Riquest(document["name"].toString(),document["email"].toString())

                    itemList2.add(item)
                    Log.d("kkang", "${item}")
                    binding.emptySeedImage.visibility =View.INVISIBLE
                    binding.emptySeedText.visibility =View.INVISIBLE

                }
                adapter.notifyDataSetChanged()
            }




    }
}


