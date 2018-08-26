package cubex.mahesh.firebase_august7am

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_storage.*
import java.io.ByteArrayOutputStream
import java.io.File

class StorageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)

        camera.setOnClickListener {
            var i = Intent("android.media.action.IMAGE_CAPTURE")
            startActivityForResult(i,123)
        }

        gallery.setOnClickListener {
            var i = Intent( )
            i.action = Intent.ACTION_GET_CONTENT
            i.type = "image/*"
            startActivityForResult(i,124)
        }
    } // onCreate


    var uri_obj: Uri? = null

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==123 && resultCode== Activity.RESULT_OK){
            var bmp: Bitmap = data!!.extras.get("data") as Bitmap
            uri_obj = getImageUri(this@StorageActivity,bmp)
        }else if(requestCode==124 && resultCode== Activity.RESULT_OK){
            uri_obj = data!!.data
        }
                    // File Uploading Logic  into Firebase Storage ....
        var sRef = FirebaseStorage.getInstance().
                                            getReference("profile_pics")
        var file_name = File(uri_obj!!.path).name
        sRef.child(file_name).putFile(uri_obj!!).
                addOnCompleteListener({
                    if(it.isSuccessful) {
                            var url = it.result.downloadUrl.toString()
                        var dBase = FirebaseDatabase.getInstance()
                        var ref = dBase.getReference("users")
                        var uid_ref = ref.child(FirebaseAuth.getInstance().uid)
                        uid_ref.child("profile_pic").setValue(url)
                    }
                })



    } // onActivityResult

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }


}
