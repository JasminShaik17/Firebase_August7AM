package cubex.mahesh.firebase_august7am

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        submit.setOnClickListener {
                var dBase = FirebaseDatabase.getInstance()
                var ref = dBase.getReference("users")
                var uid_ref = ref.child(FirebaseAuth.getInstance().uid)
                uid_ref.child("name").setValue(r_et1.text.toString())
                uid_ref.child("gender").setValue(r_et2.text.toString())
                uid_ref.child("mno").setValue(r_et3.text.toString())
                uid_ref.child("address").setValue(r_et4.text.toString())
                 uid_ref.child("fcm_token").setValue(
                         FirebaseInstanceId.getInstance().getToken())
               var i = Intent(this@RegisterActivity,
                        StorageActivity::class.java)
                startActivity(i)
        }
    }
}
