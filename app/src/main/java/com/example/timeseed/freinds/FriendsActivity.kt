package com.example.timeseed.freinds

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.adapters.ViewGroupBindingAdapter.setListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.timeseed.MainActivity
import com.example.timeseed.R
import com.example.timeseed.databinding.ActivityFriendsBinding
import com.example.timeseed.latter.LatterActivity
import com.example.timeseed.myInfo.MyInfoActivity
import com.example.timeseed.store.StoreActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.system.exitProcess


class FriendsActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var firestore:FirebaseFirestore
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityFriendsBinding.inflate(layoutInflater)
        val friendlist = arrayListOf<Friend>()
        val friendlistadapter = FriendsListAdapter(friendlist, baseContext)


        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val Uuid = auth.currentUser?.uid

        findView()
        setListener()

        firestore.collection("users").document("${Uuid}").collection("친구")
            .get()
            .addOnSuccessListener { document ->
                val fRList = arrayListOf<String>()
                for (document in document) {
                    fRList.add(document["condition"].toString())
                }
                val boolean_pandan = fRList.contains("n")
                if (boolean_pandan){
                    binding.requestBtn.setImageResource(R.drawable.notification2)
                }else{
                    binding.requestBtn.setImageResource(R.drawable.notification)
                }
            }

        binding.imageButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.imageButton4.setOnClickListener {
            val intent = Intent(this, MyInfoActivity::class.java)
            startActivity(intent)
        }
        binding.imageButton2.setOnClickListener {
            val intent = Intent(this, StoreActivity::class.java)
            startActivity(intent)
        }
        binding.friendAddBtn.setOnClickListener{
            val intent = Intent(this, FriendAddActivity::class.java)
            startActivity(intent)
        }
        binding.requestBtn.setOnClickListener{
            val intent = Intent(this, FriendRiquestActivity::class.java)
            startActivity(intent)
        }



        firestore.collection("users").document("${Uuid}").collection("친구").whereEqualTo("condition","y")
            .get()
            .addOnSuccessListener { documents ->
                friendlist.clear()
                for(documents in documents){
                    val item = Friend(documents["name"].toString(),documents["email"].toString())
                    friendlist.add(item)
                    binding.emptySeedImage.visibility =View.INVISIBLE
                    binding.emptySeedText.visibility =View.INVISIBLE
                }
                friendlistadapter.notifyDataSetChanged()
            }



        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = friendlistadapter
        binding.recyclerview.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))


        friendlistadapter.itemClick = object : FriendsListAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                val eventHendler = object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int){
                        if (p1 == DialogInterface.BUTTON_POSITIVE){
                            val intent =  Intent(this@FriendsActivity,LatterActivity::class.java)
                            startActivity(intent)
                            val nextIntent = Intent(this@FriendsActivity, LatterActivity::class.java)
                            nextIntent.putExtra("name", "${friendlist[position].name}")
                            startActivity(nextIntent)
                        }else (p1 == DialogInterface.BUTTON_NEGATIVE)
                    }
                }
                AlertDialog.Builder(this@FriendsActivity).run{
                    setTitle("TimeSeed")
                    setMessage("${friendlist[position].name} 님에게 씨앗을 보내시겠습니까?")
                    setPositiveButton("네",eventHendler)
                    setNegativeButton("아니오", null)
                    show()

                }
            }
        }

    }
    private fun findView(){
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
    }
    private fun setListener(){
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            val intent = Intent(this@FriendsActivity,FriendsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0,0)
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
//프랜드엑티비티