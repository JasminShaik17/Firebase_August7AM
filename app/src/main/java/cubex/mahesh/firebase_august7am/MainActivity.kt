package cubex.mahesh.firebase_august7am

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener {
                mAuth!!.signInWithEmailAndPassword(
                        et1.text.toString(),et2.text.toString()).
                        addOnCompleteListener {
                            if(it.isSuccessful){
                                var i = Intent(this@MainActivity,
                                        DashboardActivity::class.java)
                                startActivity(i)
                            }else{
                                Toast.makeText(this@MainActivity,
                                        "Fail to Login",
                                        Toast.LENGTH_LONG).show()
                            }
                        }
        }
        register.setOnClickListener {
            mAuth!!.createUserWithEmailAndPassword(
                    et1.text.toString(),et2.text.toString()).
                    addOnCompleteListener {
                        if(it.isSuccessful){
       var i = Intent(this@MainActivity,
                                              RegisterActivity::class.java)
       startActivity(i)
                        }else{
Toast.makeText(this@MainActivity,
        "Fail to Register",
        Toast.LENGTH_LONG).show()
                        }
                    }
        }
    }
}
