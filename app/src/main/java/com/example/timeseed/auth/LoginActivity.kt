package com.example.timeseed.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.timeseed.MainActivity
import com.example.timeseed.R
import com.example.timeseed.databinding.ActivityLoginBinding
import com.example.timeseed.myInfo.MyInfoActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var firestore: FirebaseFirestore

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        //여기
        var checkEmail: String? = null
        fun checkAuth(): Boolean{
            var currentUser = auth.currentUser
            return currentUser?.let {
                checkEmail = currentUser.email
                currentUser.isEmailVerified
            } ?: let{
                false
            }
        }

        binding.loginBtn.setOnClickListener {

            val email = binding.emailArea.text.toString()
            val password = binding.passwordArea.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        if(checkAuth()){    //여기

                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)

                            firestore = FirebaseFirestore.getInstance()

                        } else {
                            Toast.makeText(this, "전송된 메일로 이메일 인증이 되지 않았습니다.", Toast.LENGTH_LONG).show()
                        }



//                        var userEmail = binding.emailArea.text.toString()
//                         firestore.collection("users")
//                          .whereEqualTo("email","${userEmail}")
//                          .get()
//                          .addOnSuccessListener { documents ->
//                                  for (documents in documents){
//                                      val userName = documents["name"].toString()
//                                      Log.d("kkang", "${userName}")
//                                      val userNameData = Intent(this,MyInfoActivity::class.java)
//                                      userNameData.putExtra("name","${userName}")
//                                      startActivity(userNameData)
//                              }
//
//                          }
//                             .addOnFailureListener{exception ->
//                                 Log.w("kkang", "Error getting")
//                             }



                    } else {

                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_LONG).show()

                    }
                }
        }

    }
}