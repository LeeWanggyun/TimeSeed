
package com.example.timeseed.myInfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.timeseed.freinds.FriendsActivity
import com.example.timeseed.MainActivity
import com.example.timeseed.R
import com.example.timeseed.auth.IntroActivity
import com.example.timeseed.store.StoreActivity
import com.example.timeseed.databinding.ActivityMyInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_my_info.*
import kotlin.system.exitProcess

class MyInfoActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityMyInfoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val Uuid = auth.currentUser?.uid

        setContentView(binding.root)

        findViewById<Button>(R.id.button).setOnClickListener {

            auth.signOut()

            Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, IntroActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }


        firestore = FirebaseFirestore.getInstance()



        firestore.collection("users")
            .whereEqualTo("uid","${Uuid}")
            .get()
            .addOnSuccessListener { documents ->
                for (documents in documents) {
                    val userName = documents["name"].toString()
                    val userEmail = documents["email"].toString()
                    Log.d("kkang", "${userName}")
                    val nameText = findViewById<TextView>(R.id.textView3)
                    val emailText = findViewById<TextView>(R.id.textView4)
                    nameText.setText("${userName}")
                    emailText.setText("${userEmail}")
                }

            }
            .addOnFailureListener{exception ->
                Log.w("kkang", "failde")
            }





        binding.imageButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.imageButton2.setOnClickListener{
            val intent = Intent(this, StoreActivity::class.java)
            startActivity(intent)
        }
        binding.imageButton3.setOnClickListener {
            val intent = Intent(this, FriendsActivity::class.java)
            startActivity(intent)

        }
        firestore.collection("users").document("${Uuid}").collection("씨앗").whereEqualTo("name", "이보성")
            .get()
            .addOnSuccessListener { document ->
                for (document in document){
                    val documentId = document.id
                    Log.d("kkang", "${documentId}" )
                }
            }

        PwChangeBtn.setOnClickListener{
            var editTextNewPassword = EditText(this)
            editTextNewPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            var alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("비밀번호 변경")
            alertDialog.setMessage("새로운 비밀번호를 입력 해주세요")
            alertDialog.setView(editTextNewPassword)
            alertDialog.setPositiveButton("변경", {dialogInterface, i -> changePassword(editTextNewPassword.text.toString()) })
            alertDialog.setNegativeButton("취소", {dialogInterface, i -> dialogInterface.dismiss() })
            alertDialog.show()

        }

    }

    private fun changePassword(password:String) {
        FirebaseAuth.getInstance().currentUser!!.updatePassword(password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "비밀번호가 변경되었습니다.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this,"변경에 실패 했습니다", Toast.LENGTH_LONG).show()

                }
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