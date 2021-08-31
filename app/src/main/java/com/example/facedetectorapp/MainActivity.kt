package com.example.facedetectorapp

import android.R.attr
import android.accessibilityservice.GestureDescription
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.FaceDetector
import com.google.mlkit.vision.face.*


class MainActivity : AppCompatActivity() {
    lateinit var button: Button
    lateinit var up : ImageView
    lateinit var imageView: ImageView
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.imageView)
        button = findViewById(R.id.button)
        up = findViewById(R.id.upload)
        up.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 123)
        }


        button.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 123)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==123){
            imageView.setImageURI(data?.data)
            val options = BitmapFactory.Options()
            options.inMutable = true
            button.setText("Select another image")
            val bit = MediaStore.Images.Media.getBitmap(contentResolver,data?.data)
            val bitmap = bit.copy(Bitmap.Config.ARGB_8888,true)
            val paint = Paint()
            paint.strokeWidth = 6f
            paint.color = Color.GREEN
            paint.style = Paint.Style.STROKE
            val tempBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.RGB_565)
            val canvas = Canvas(tempBitmap)
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            val faceDetector = FaceDetector.Builder(this).setTrackingEnabled(false)
                .build()
            if (!faceDetector.isOperational) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    GestureDescription.Builder()
                }
                return
            }
            val frame = Frame.Builder().setBitmap(bitmap).build()
            val faces = faceDetector.detect(frame)
            for (i in 0 until faces.size()) {
                val thisFace = faces.valueAt(i)
                val x1 = thisFace.position.x
                val y1 = thisFace.position.y
                val x2 = x1 + thisFace.width
                val y2 = y1 + thisFace.height
                canvas.drawRoundRect(RectF(x1, y1, x2, y2), 2f, 2f, paint)
            }
            imageView!!.setImageDrawable(BitmapDrawable(resources, tempBitmap))

        }

    }

}