package com.example.imgtotxtdemo

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText

class MainActivity : AppCompatActivity() {

    private var cam: Button? = null
    private var txt: TextView? = null
    private var img: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cam = findViewById(R.id.cameraBtn)
        txt = findViewById(R.id.textValue)
        img = findViewById(R.id.capturedImg)

        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(listOf(android.Manifest.permission.CAMERA).toTypedArray(), 123)
        }
    }

    fun onBtnClick(view: View) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 123)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val bitMap: Bitmap = data?.extras?.get("data") as Bitmap

        //ste image in image view
        img?.setImageBitmap(bitMap)

        val firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitMap)
        val firebaseVision = FirebaseVision.getInstance()
        val firebaseVisionTextRecognizer = firebaseVision.onDeviceTextRecognizer

        val tasks = firebaseVisionTextRecognizer.processImage(firebaseVisionImage)

        tasks.addOnSuccessListener {
            OnSuccessListener<FirebaseVisionText> { firebaseVisionText ->
                val s = firebaseVisionText?.text
                txt?.text = s
            }
        }

    }
}