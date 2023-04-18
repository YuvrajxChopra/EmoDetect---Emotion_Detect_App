package com.example.faceapitest

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.core.net.toUri
import org.json.JSONArray
import org.json.JSONObject
import androidx.core.content.ContextCompat

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setTitle("")
        }
        val responseBody = intent.getStringExtra("responseBody")
        val imageUri = intent.getStringExtra("imageUri")!!.toUri()

        findViewById<ImageView>(R.id.imageView).setImageURI(imageUri)


        val angerLikelihood = JSONObject(responseBody).getJSONArray("responses").getJSONObject(0).getJSONArray("faceAnnotations").getJSONObject(0).getString("angerLikelihood")
        val joyLikelihood = JSONObject(responseBody).getJSONArray("responses").getJSONObject(0).getJSONArray("faceAnnotations").getJSONObject(0).getString("joyLikelihood")
        val sorrowLikelihood = JSONObject(responseBody).getJSONArray("responses").getJSONObject(0).getJSONArray("faceAnnotations").getJSONObject(0).getString("sorrowLikelihood")
        val surpriseLikelihood = JSONObject(responseBody).getJSONArray("responses").getJSONObject(0).getJSONArray("faceAnnotations").getJSONObject(0).getString("surpriseLikelihood")

        //UNKNOWN, VERY_UNLIKELY, UNLIKELY, POSSIBLE, LIKELY, or VERY_LIKELY
        val angerLikelihoodValue = when (angerLikelihood) {
            "VERY_LIKELY" -> 100
            "LIKELY" -> 75
            "POSSIBLE" -> 50
            "UNLIKELY" -> 25
            "VERY_UNLIKELY" -> 0
            else -> 0
        }

        val joyLikelihoodValue = when (joyLikelihood) {
            "VERY_LIKELY" -> 100
            "LIKELY" -> 75
            "POSSIBLE" -> 50
            "UNLIKELY" -> 25
            "VERY_UNLIKELY" -> 0
            else -> 0
        }

        val sorrowLikelihoodValue = when (sorrowLikelihood) {
            "VERY_LIKELY" -> 100
            "LIKELY" -> 75
            "POSSIBLE" -> 50
            "UNLIKELY" -> 25
            "VERY_UNLIKELY" -> 0
            else -> 0
        }

        val surpriseLikelihoodValue = when (surpriseLikelihood) {
            "VERY_LIKELY" -> 100
            "LIKELY" -> 75
            "POSSIBLE" -> 50
            "UNLIKELY" -> 25
            "VERY_UNLIKELY" -> 0
            else -> 0
        }

        findViewById<TextView>(R.id.angerTxt).text = "$angerLikelihoodValue% Anger"
        findViewById<TextView>(R.id.joyTxt).text = "$joyLikelihoodValue% Joy"
        findViewById<TextView>(R.id.sorrowTxt).text = "$sorrowLikelihoodValue% Sorrow"
        findViewById<TextView>(R.id.surpriseTxt).text = "$surpriseLikelihoodValue% Surprise"

        findViewById<ProgressBar>(R.id.anger).progress = angerLikelihoodValue
        findViewById<ProgressBar>(R.id.joy).progress = joyLikelihoodValue
        findViewById<ProgressBar>(R.id.sorrow).progress = sorrowLikelihoodValue
        findViewById<ProgressBar>(R.id.surprise).progress = surpriseLikelihoodValue

        val backButton = findViewById<Button>(R.id.back_button)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}