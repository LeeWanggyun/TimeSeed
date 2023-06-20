package com.example.timeseed.freinds

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.timeseed.R
import com.example.timeseed.databinding.FriendRvItemBinding
import com.example.timeseed.latter.FlowerActivity
import com.google.common.base.CharMatcher.invisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_my_info.view.*

class FriendRvViewHolder(val binding: FriendRvItemBinding): RecyclerView.ViewHolder(binding.root){
    val name: TextView = itemView.findViewById(R.id.faName)
}

class FriendRVAdapter(val itemList: ArrayList<User> = arrayListOf(), val context: Context):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth


    override fun getItemCount(): Int {
        return itemList.size

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        FriendRvViewHolder(
            FriendRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val Uuid = auth.currentUser?.uid

        val binding = (holder as FriendRvViewHolder).binding

        binding.faName.text = itemList[position].name
        binding.faEmail.text = itemList[position].email
        val add = itemList[position].email

        firestore.collection("users")
            .whereEqualTo("email", "${add}")
            .get()
            .addOnSuccessListener { documents ->
                for (documents in documents) {
                    val femail = documents["email"].toString()
                    val fuid = documents["uid"].toString()
                    val fName = documents["name"].toString()



                    binding.fabtn.setOnClickListener {
                        Log.d("kkang", "Clikced")
                        firestore.collection("users")
                            .whereEqualTo("email", "${add}")
                            .get()
                            .addOnSuccessListener { documents ->
                                for (documents in documents) {
                                    val femail = documents["email"].toString()
                                    val fuid = documents["uid"].toString()
                                    val fName = documents["name"].toString()
                                    firestore.collection("users").document("${fuid}")
                                        .collection("친구").get()
                                        .addOnSuccessListener { documents ->
                                            for (documents in documents) {
                                                val userInfo = documents["uid"].toString()

                                                firestore.collection("users")
                                                    .whereEqualTo("uid", "${Uuid}")
                                                    .get()
                                                    .addOnSuccessListener { documents ->
                                                        for (documents in documents) {
                                                            val user = mapOf(
                                                                "name" to documents["name"].toString(),
                                                                "email" to documents["email"].toString(),
                                                                "uid" to documents["uid"].toString(),
                                                                "condition" to "n"
                                                            )
                                                            firestore.collection("users")
                                                                .document("${fuid}")
                                                                .collection("친구")
                                                                .document("${Uuid}").set(user)
                                                        }
                                                    }
                                            }
                                        }
                                }
                            }
                       Toast.makeText(context,"친구 요청을 보냈습니다!",Toast.LENGTH_LONG).show()

                    }
                }
            }
    }
}











