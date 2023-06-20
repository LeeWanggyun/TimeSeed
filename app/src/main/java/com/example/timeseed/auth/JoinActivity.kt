package com.example.timeseed.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.timeseed.MainActivity
import com.example.timeseed.R
import com.example.timeseed.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private  lateinit var firestore : FirebaseFirestore

    private lateinit var binding: ActivityJoinBinding

    var fbAuth : FirebaseAuth? = null
    var fbFirestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        firestore = FirebaseFirestore.getInstance()

        auth = Firebase.auth

        var db: FirebaseFirestore = FirebaseFirestore.getInstance()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        binding.joinBtn.setOnClickListener {

            var isGoToJoin = true

            val email = binding.emailArea.text.toString()
            val password1 = binding.passwordArea1.text.toString()
            val password2 = binding.passwordArea2.text.toString()


            data class ResultDTO (
                var uid :String? = null,
                var name : String? = null,
                var timestamp : Long? = null
            )



            // 값이 비어있는지 확인
            if (email.isEmpty()) {
                Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }


            if(password1.isEmpty()) {
                Toast.makeText(this, "Password1을 입력해주세요", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(password2.isEmpty()) {
                Toast.makeText(this, "Password2을 입력해주세요", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            // 비밀번호 2개가 같은지 확인
            if(!password1.equals(password2)) {
                Toast.makeText(this, "비밀번호를 똑같이 입력해주세요", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            //비밀번호가 6자 이상인지
            if (password1.length < 6) {
                Toast.makeText(this, "비밀번호를 6자리 이상으로 입력해주세요", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(isGoToJoin) {

                auth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

//                            val intent = Intent(this, MainActivity::class.java)
//                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                            startActivity(intent)
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)


                            var resultDTO = ResultDTO()
                            resultDTO.uid = auth?.currentUser?.uid
                            resultDTO.name = binding.name.text.toString()
                            resultDTO.timestamp = System.currentTimeMillis()
                            auth = FirebaseAuth.getInstance()
                            val Uuid = auth.currentUser?.uid


                            val user = mapOf(
                                "name" to binding.name.text.toString(),
                                "email" to binding.emailArea.text.toString(),
                                "uid" to resultDTO.uid,
                                "point" to "0"
                            )
                            val name  = binding.name.text.toString()


                            val daysi = mapOf(
                                "condition" to "y",
                                "name" to "데이지"

                            )
                            val sunflower = mapOf(
                                "condition" to "n",
                                "name" to "해바라기"
                            )
                            val tulip = mapOf(
                                "condition" to "n",
                                "name" to "튤립"
                            )
                            firestore.collection("users").document("${Uuid}").collection("flowerSeeds").add(daysi)

                            firestore.collection("users").document("${Uuid}").collection("flowerSeeds").add(sunflower)

                            firestore.collection("users").document("${Uuid}").collection("flowerSeeds").add(tulip)





//                            firestore.collection(auth!!.currentUser!!.uid)?.document()?.set(resultDTO)
                            firestore.collection("users").document("${Uuid}").set(user)
                            val nullUser = mapOf(
                                "name" to "",
                                "email" to "",
                                "uid" to "",
                            )
                            firestore.collection("users").document("${Uuid}").collection("친구").add(nullUser)
                            auth.currentUser?.sendEmailVerification()
                                ?.addOnCompleteListener { sendTask ->
                                    if(sendTask.isSuccessful) {
                                        Toast.makeText(this, "인증 메일을 발송 했습니다!", Toast.LENGTH_LONG).show()
                                    } else {
                                        Toast.makeText(this, "이메일인증실패", Toast.LENGTH_LONG).show()
                                    }

                                }


                        } else {
                            Toast.makeText(this, "실패", Toast.LENGTH_LONG).show()
                        }

                    }

                }
        }
    }
}