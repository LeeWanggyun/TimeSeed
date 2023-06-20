package com.example.timeseed.store


import android.app.ProgressDialog.show
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.adapters.ToolbarBindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timeseed.MainActivity
import com.example.timeseed.auth.IntroActivity
import com.example.timeseed.databinding.ActivityStoreBinding
import com.example.timeseed.freinds.FriendsActivity
import com.example.timeseed.myInfo.MyInfoActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlin.system.exitProcess


class StoreActivity : AppCompatActivity() {
    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityStoreBinding.inflate(layoutInflater)
        val itemList = arrayListOf<Seed>()
        val adapter = MyAdapter(itemList,baseContext)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val Uuid = auth.currentUser?.uid
        binding.rv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.rv.adapter=adapter
        firestore = FirebaseFirestore.getInstance()



        adapter.itemClick = object : MyAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val eventHendler = object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        if (p1 == DialogInterface.BUTTON_POSITIVE) {
                            firestore.collection("users").document("${Uuid}").collection("씨앗")
                                .get()
                                .addOnSuccessListener {document ->
                                    for (document in document){
                                        var condition = document["condition"].toString()
                                        if(condition == "y"){
                                            Toast.makeText(this@StoreActivity, "이미씨앗", Toast.LENGTH_LONG).show()
                                            break
                                        }else{
                                            firestore.collection("users").document("${Uuid}").collection("씨앗")
                                                .whereEqualTo("special","${itemList[position].special}")
                                                .get()
                                                .addOnSuccessListener{document ->
                                                    for(document in document){
                                                        val documentid = document.id
                                                        firestore.collection("users").document("${Uuid}")
                                                            .collection("씨앗").document("${documentid}")
                                                            .update("condition","y")

                                                    }
                                                }

                                            Handler().postDelayed({
                                                val intent = Intent(this@StoreActivity, MainActivity::class.java)
                                                startActivity(intent)
                                                finish()
                                            }, 1000)

                                        }
                                    }
                                }



                        } else (p1 == DialogInterface.BUTTON_NEGATIVE)
                    }
                }
                AlertDialog.Builder(this@StoreActivity).run {
                    setTitle("TimeSeed")
                    setMessage("${itemList[position].name}의 씨앗을 심을까요?")
                    setPositiveButton("네", eventHendler)
                    setNegativeButton("아니오", null)
                    show()
                }
            }
        }
        firestore.collection("users").document("${Uuid}").collection("씨앗")
            .whereEqualTo("condition","n")
            .get()
            .addOnSuccessListener { document ->
                itemList.clear()
                for (document in document){
                    val item = Seed(document["name"].toString(),document["email"].toString(),document["sendDate"].toString(),document["special"].toString())

                    itemList.add(item)
                    Log.d("kkang", "${item}")

                    binding.emptySeedImage.visibility =View.INVISIBLE
                    binding.emptySeedText.visibility =View.INVISIBLE

                }
                adapter.notifyDataSetChanged()
            }

        binding.imageButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.imageButton4.setOnClickListener{
            val intent = Intent(this, MyInfoActivity::class.java)
            startActivity(intent)
        }
        binding.imageButton3.setOnClickListener{
            val intent = Intent(this, FriendsActivity::class.java)
            startActivity(intent)

        }
    }
    var lastTimeBackPressed : Long = 0
    override fun onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed >= 1500){
            lastTimeBackPressed = System.currentTimeMillis()
            Toast.makeText(this,"'뒤로' 버튼을 한번 더 누르시면 종료됩니다.",Toast.LENGTH_LONG).show() }
        else {
            ActivityCompat.finishAffinity(this)
            System.runFinalization()
            exitProcess(0)
        }
    }
}