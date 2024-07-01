package com.janlyczakowski.masterthesis

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class IntroductionActivityI : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.introduction_part_one)

        val text1 = getString(R.string.introduction_message_3)
        val textToModify1 = listOf("“Optimizing mobile map reading: An adaptive map approach in response to dynamic pedestrian context”")
        val text1Modified = Utils().modifyNumberStyle(text1,textToModify1,this)
        val text1View = findViewById<TextView>(R.id.introduction_message_3)
        text1View.text = text1Modified
    }
    fun onClickIntroductionBtnOneListener(View: android.view.View) {

        val intent = Intent(this, IntroductionActivityII::class.java)
        startActivity(intent)
    }

}