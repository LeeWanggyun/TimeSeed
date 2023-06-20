package com.example.timeseed.latter


import android.app.ProgressDialog.show
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.adapters.ToolbarBindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.timeseed.MainActivity
import com.example.timeseed.R
import com.example.timeseed.databinding.ActivityFlowerBinding
import com.example.timeseed.databinding.ActivityStoreBinding
import com.example.timeseed.databinding.FlowerRvItemBinding
import com.example.timeseed.freinds.FriendsActivity
import com.example.timeseed.myInfo.MyInfoActivity
import com.example.timeseed.store.StoreActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class FlowerActivity : AppCompatActivity() {
    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
            override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityFlowerBinding.inflate(layoutInflater)
        val itemList = arrayListOf<Flower>()
        val adapter = FlowerRVAdapter(itemList,baseContext)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val Uuid = auth.currentUser?.uid
        binding.flowerRv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.flowerRv.adapter=adapter
        firestore = FirebaseFirestore.getInstance()
        //밑에 이거 두개
        findView()
        setListener()


        binding.backBtn.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }




        adapter.itemClick = object : FlowerRVAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val eventHendler = object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        if (p1 == DialogInterface.BUTTON_POSITIVE) {
                                        val intent =  Intent(this@FlowerActivity,ReadFlowerLatterActivity::class.java)
                                        startActivity(intent)
                                        val nextIntent = Intent(this@FlowerActivity, ReadFlowerLatterActivity::class.java)
                                        nextIntent.putExtra("name", "${itemList[position].name}")
                                        nextIntent.putExtra("latter", "${itemList[position].latter}")
                                        nextIntent.putExtra("sendDate", "${itemList[position].date}")
                                        startActivity(nextIntent)
                        } else (p1 == DialogInterface.BUTTON_NEGATIVE)
                    }
                }
                AlertDialog.Builder(this@FlowerActivity).run {
                    setTitle("TimeSeed")
                    setMessage("${itemList[position].name}와(과) 추억을 볼까요?")
                    setPositiveButton("네", eventHendler)
                    setNegativeButton("아니오", null)
                    show()
                }
            }
        }
        firestore.collection("users").document("${Uuid}").collection("씨앗")
            .whereEqualTo("condition","f")
            .get()
            .addOnSuccessListener { document ->
                itemList.clear()
                for (document in document){
                    val item = Flower(document["name"].toString(),document["email"].toString(),document["sendDate"].toString(),document["special"].toString(),document["latter"].toString(),document["seed"].toString())

                    itemList.add(item)
                    Log.d("kkang", "${item}")
                    binding.emptySeedImage.visibility =View.INVISIBLE
                    binding.emptySeedText.visibility =View.INVISIBLE

                }
                adapter.notifyDataSetChanged()
            }




    }
    //밑에 두개
    private fun findView(){
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
    }
    private fun setListener(){
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            val intent = Intent(this@FlowerActivity,FlowerActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0,0)
        }
    }

}