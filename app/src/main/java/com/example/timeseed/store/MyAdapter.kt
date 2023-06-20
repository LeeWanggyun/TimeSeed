package com.example.timeseed.store

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timeseed.R
import com.example.timeseed.databinding.ContentRvItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyViewHolder(val binding: ContentRvItemBinding): RecyclerView.ViewHolder(binding.root){
    val name : TextView = itemView.findViewById(R.id.Fname)




}

class MyAdapter(val itemList:ArrayList<Seed> = arrayListOf(),val context: Context):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    interface  ItemClick{
        fun onClick(view : View, position: Int)
    }
    var itemClick : ItemClick? = null


    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyViewHolder(
            ContentRvItemBinding.inflate(
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



            Log.d("KKang", "item root:$position")
            if(itemClick != null){
                holder.itemView.setOnClickListener{ v ->
                    itemClick?.onClick(v,position)

            }
        }
    }
}