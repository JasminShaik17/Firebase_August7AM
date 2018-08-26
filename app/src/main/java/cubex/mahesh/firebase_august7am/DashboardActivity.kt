package cubex.mahesh.firebase_august7am

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_dashboard.*
import okhttp3.RequestBody
import okhttp3.OkHttpClient
import android.widget.Toast
import okhttp3.MediaType
import okhttp3.Request
import org.json.JSONObject
import org.json.JSONArray
import java.io.IOException


class DashboardActivity : AppCompatActivity() {
    var user_info = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        var ref = FirebaseDatabase.getInstance().
                getReference("users/"+FirebaseAuth.getInstance().uid)
        ref.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }
            override fun onDataChange(p0: DataSnapshot?) {
               var it:Iterable<DataSnapshot> = p0!!.children
                it.forEach {
                        when(it.key){
                            "name"-> user_info.add("Name :"+it.value)
                            "gender"->user_info.add("Gender :"+it.value)
                            "mno"->user_info.add("Mobile No :"+it.value)
                            "address"->user_info.add("Address :"+it.value)
                            "profile_pic"->{
                                  var url = it.value
                                    Glide.with(this@DashboardActivity).
                                            load(url).into(pic)
                            }
                        }
                } // forEach

      var adapter = ArrayAdapter<String>(this@DashboardActivity,
           android.R.layout.simple_list_item_single_choice,user_info)
      lview_details.adapter = adapter
            }
        }) //  displaying individual person info ...

                        // FCM code....
        var list_names = mutableListOf<String>()
        var list_tokens = mutableListOf<String>()

        var dBase_ref = FirebaseDatabase.getInstance().
                getReference("users")
        dBase_ref.addValueEventListener(
                object:ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {

                    }
                    override fun onDataChange(p0: DataSnapshot?) {
                         var childs_users =    p0!!.children
                        childs_users.forEach {
                            var childs_uid = it.children
                            childs_uid.forEach {
                              when(it.key){
                                  "name"-> list_names.add(it.value.toString())
                                  "fcm_token"->list_tokens.add(it.value.toString())
                              }
                            }
                        } // close child_users
                        var adapter = ArrayAdapter<String>(this@DashboardActivity,
                                android.R.layout.simple_list_item_single_choice,
                                list_names)
                        lview_users.adapter = adapter
                        lview_users.setOnItemClickListener { adapterView, view, i, l ->
                                post(list_tokens.get(i))
                        }

                        send_all.setOnClickListener {
                            postAll(list_tokens)
                        }
                    }

                })
    }  // onCreate Methods ...

    fun post(token:String) {
runOnUiThread {
    var jsonObjec: JSONObject? = null
    val bodydata = et_msg.text.toString()
    try {


        val al = mutableListOf<String>()
        al.add(token)
        jsonObjec = JSONObject()

        val jsonArray = JSONArray(al)
        jsonObjec.put("registration_ids", jsonArray)
        val jsonObjec2 = JSONObject()
        jsonObjec2.put("body", bodydata)
        jsonObjec2.put("title", "Android Aug-7AM")
        jsonObjec.put("notification", jsonObjec2)

        jsonObjec.put("time_to_live", 172800)
        jsonObjec.put("priority", "HIGH")

        println("______________________")
        println(jsonObjec)
        println("______________________")
    } catch (e: Exception) {
        Toast.makeText(this@DashboardActivity, "Exception", Toast.LENGTH_SHORT).show()
    }


    val client = OkHttpClient()
    val JSON = MediaType.parse("application/json; charset=utf-8")
    val body = RequestBody.create(JSON, jsonObjec!!.toString())
    val request = Request.Builder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization","AAAAbKH0Cck:APA91bHUyUmmSqLEx4E-WvDFIcQ6BGo6B_V1MkXBCgBmv3R7rSDpR107ppGkwSBhskMKxyW9vpjZWJFzbCgnGIyY_l3JO1RON1NsRCvcvivNRFKzY35RSZQcBPW5r5gzfVL3UgBcpSVg")
            .url("https://fcm.googleapis.com/fcm/send")
            .post(body)
            .build()
    val call = client.newCall(request)
    call.enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: IOException) {

        }

        @Throws(IOException::class)
        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

        }
    })

}

    }

    fun postAll(tokens:MutableList<String>) {

        runOnUiThread {
            var jsonObjec: JSONObject? = null
            val bodydata = et_msg.text.toString()
            try {


                val al = mutableListOf<String>()
                for(token in tokens) {
                    al.add(token)
                }
                jsonObjec = JSONObject()

                val jsonArray = JSONArray(al)
                jsonObjec.put("registration_ids", jsonArray)
                val jsonObjec2 = JSONObject()
                jsonObjec2.put("body", bodydata)
                jsonObjec2.put("title", "Android Aug-7AM")
                jsonObjec.put("notification", jsonObjec2)

                jsonObjec.put("time_to_live", 172800)
                jsonObjec.put("priority", "HIGH")

                println("______________________")
                println(jsonObjec)
                println("______________________")
            } catch (e: Exception) {
                Toast.makeText(this@DashboardActivity, "Exception", Toast.LENGTH_SHORT).show()
            }


            val client = OkHttpClient()
            val JSON = MediaType.parse("application/json; charset=utf-8")
            val body = RequestBody.create(JSON, jsonObjec!!.toString())
            val request = Request.Builder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization","AAAAbKH0Cck:APA91bHUyUmmSqLEx4E-WvDFIcQ6BGo6B_V1MkXBCgBmv3R7rSDpR107ppGkwSBhskMKxyW9vpjZWJFzbCgnGIyY_l3JO1RON1NsRCvcvivNRFKzY35RSZQcBPW5r5gzfVL3UgBcpSVg")
                    .url("https://fcm.googleapis.com/fcm/send")
                    .post(body)
                    .build()
            val call = client.newCall(request)
            call.enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {

                }

                @Throws(IOException::class)
                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                }
            })

        }

    }
}
