package com.example.faceapitest

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setTitle("")
        }
        val endpoint = "apilink"
        val imageUri = intent.getStringExtra("imageUri")!!.toUri()

        val contentResolver = contentResolver

        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(imageUri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])!!
        val filePath = cursor.getString(columnIndex)
        cursor.close()

        val imageFile = File(filePath)


        val mediaType: MediaType? = "application/json".toMediaTypeOrNull()
        val client = OkHttpClient()
        val body = """
            {
                "requests": [
                    {
                        "image": {
                            "content": "${Base64.encodeToString(imageFile.readBytes(), Base64.DEFAULT)}"
                        },
                        "features": [
                            {
                                "type": "FACE_DETECTION",
                                "maxResults": 1
                            }
                        ]
                    }
                ]
            }
        """.trimIndent().toRequestBody(mediaType)

        val request = okhttp3.Request.Builder()
            .url(endpoint)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                runOnUiThread {
                    Toast.makeText(this@LoadingActivity, "Error: $e", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseBody = response.body?.string()
                val intent = Intent(this@LoadingActivity, ResultActivity::class.java)
                intent.putExtra("responseBody", responseBody)
                intent.putExtra("imageUri", imageUri.toString())
                startActivity(intent)
                finish()
            }
        })
    }
}