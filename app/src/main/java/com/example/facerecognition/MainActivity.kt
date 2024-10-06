package com.example.facerecognition

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val layout = findViewById<ConstraintLayout>(R.id.main)
        layout.setBackgroundResource(R.drawable.background_main)
        val buttonCamera = findViewById<Button>(R.id.btnCamera)


        buttonCamera.setOnClickListener{
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(intent.resolveActivity(packageManager) != null){
                startActivityForResult(intent, 123)
            }
            else{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 123 && resultCode == RESULT_OK){
            val extras = data?.extras
            val bitmap = extras?.get("data") as? Bitmap
            if (bitmap != null) {
                detectFace(bitmap)
            }
        }
    }

    private fun detectFace(bitmap: Bitmap){
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()

        val detector = FaceDetection.getClient(options)
        val image = InputImage.fromBitmap(bitmap, 0)

        val result = detector.process(image)
            .addOnSuccessListener { faces ->
                // Task completed successfully, face detected !!
                var resultText = " "
                var i = 1
                for(face in faces){
                    resultText = "Total Faces : $i" +
                                "\n Smile % : ${face.smilingProbability?.times(100)}%" +
                            "\n Left Eye Open % : ${face.leftEyeOpenProbability?.times(100)}%" +
                            "\n Right Eye Open % : ${face.rightEyeOpenProbability?.times(100)}%"+
                            "\n Will add more use cases and features soon in next update"
                    i++
                }

                if(faces.isEmpty()){
                    Toast.makeText(this, "No face detected", Toast.LENGTH_LONG).show()
                }
                else {
                    // Show the result in an AlertDialog
                    AlertDialog.Builder(this)
                        .setTitle("Face Detection Results")
                        .setMessage(resultText)
                        .setPositiveButton("OK", null)
                        .show()
                }

            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                Toast.makeText(this, "Unable to detect", Toast.LENGTH_LONG).show()

            }

    }
}