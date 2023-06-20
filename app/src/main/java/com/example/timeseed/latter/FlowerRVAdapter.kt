package com.example.timeseed.latter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.timeseed.R
import com.example.timeseed.databinding.FlowerRvItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MyViewHolder(val binding: FlowerRvItemBinding): RecyclerView.ViewHolder(binding.root){
    val name : TextView = itemView.findViewById(R.id.Fname)




}

class FlowerRVAdapter(val itemList:ArrayList<Flower> = arrayListOf(),val context: Context):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null


    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =

        MyViewHolder(

            FlowerRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false

            )

        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        Log.d("KKang", "qwe : $position")
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val Uuid = auth.currentUser?.uid


        val binding = (holder as MyViewHolder).binding

        binding.Fname.text = itemList[position].name
        binding.date.text = itemList[position].date
        binding.Femail.text = itemList[position].email
        binding.fspecial.text = itemList[position].special
        if (itemList[position].flower == "데이지"){
            binding.flowerImage.setImageResource(R.drawable.daysiflowerpot)
        }else if (itemList[position].flower == "해바라기") {
            binding.flowerImage.setImageResource(R.drawable.sunflowerpot)
        }else if (itemList[position].flower == "튤립") {
            binding.flowerImage.setImageResource(R.drawable.tuilpflowerpot)
        }

        binding.deleteBtn.setOnClickListener {
            firestore.collection("users").document("${Uuid}").collection("씨앗")
                .whereEqualTo("condition", "f")
                .get()
                .addOnSuccessListener { document ->
                    for (document in document) {
                        val documents = document.id
                        val ffspecial = binding.fspecial.text.toString()
                        val Fspesial = document["special"].toString()
                        if (Fspesial == ffspecial) {
                            firestore.collection("users").document("${Uuid}").collection("씨앗")
                                .document("${documents}").delete()
                            Toast.makeText(context,"삭제 하였습니다.", Toast.LENGTH_SHORT).show()

                        }

                    }
                }



        }

        Log.d("KKang", "item root:$position")
        if (itemClick != null) {
            holder.itemView.setOnClickListener { v ->
                itemClick?.onClick(v, position)

            }
        }
    }
}