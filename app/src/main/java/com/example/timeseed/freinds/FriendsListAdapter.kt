package com.example.timeseed.freinds

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.timeseed.databinding.FriendsListItemBinding
import com.example.timeseed.latter.LatterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

class MyViewHolder(val binding: FriendsListItemBinding): RecyclerView.ViewHolder(binding.root)


class FriendsListAdapter(val friendsListAdapter: ArrayList<Friend>,val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null
    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth


    override fun getItemCount(): Int = friendsListAdapter.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyViewHolder(
            FriendsListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val Uuid = auth.currentUser?.uid

        if (itemClick != null) {
            holder.itemView.setOnClickListener { v ->
                itemClick?.onClick(v, position)
            }
        }
        var seed = mapOf(
            "name" to "",
            "latter" to "",
            "sendDate" to "",
            "documentId" to ""

        )


        val binding = (holder as MyViewHolder).binding
        val friendName = binding.Fname.text.toString()
        val femail = binding.Femail.text.toString()

        binding.Fname.text = friendsListAdapter[position].name
        binding.Femail.text = friendsListAdapter[position].email




    }
}





