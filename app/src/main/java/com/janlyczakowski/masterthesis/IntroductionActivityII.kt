package com.janlyczakowski.masterthesis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class IntroductionActivityII : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.introduction_part_two)

        val text1 = getString(R.string.introduction_message_7)
        val textToModify1 = listOf("short survey")
        val text1Modified = Utils().modifyNumberStyle(text1,textToModify1,this)
        val text1View = findViewById<TextView>(R.id.introduction_message_7)
        text1View.text = text1Modified

        val text2 = getString(R.string.introduction_message_8)
        val textToModify2 = listOf("twice one map-related task","one question")
        val text2Modified = Utils().modifyNumberStyle(text2,textToModify2,this)
        val text2View = findViewById<TextView>(R.id.introduction_message_8)
        text2View.text = text2Modified

        val text3 = getString(R.string.introduction_message_9)
        val textToModify3 = listOf("short survey")
        val text3Modified = Utils().modifyNumberStyle(text3,textToModify3,this)
        val text3View = findViewById<TextView>(R.id.introduction_message_9)
        text3View.text = text3Modified

        val text4 = getString(R.string.introduction_message_10)
        val textToModify4 = listOf("three different locations")
        val text4Modified = Utils().modifyNumberStyle(text4,textToModify4,this)
        val text4View = findViewById<TextView>(R.id.introduction_message_10)
        text4View.text = text4Modified

        val text5 = getString(R.string.introduction_message_13)
        val textToModify5 = listOf("before starting the test")
        val text5Modified = Utils().modifyNumberStyle(text5,textToModify5,this)
        val text5View = findViewById<TextView>(R.id.introduction_message_13)
        text5View.text = text5Modified

    }
    fun onClickIntroductionBtnTwoListener(View: android.view.View){

        val intent = Intent(this, PreTestSurvey::class.java)
        startActivity(intent)
    }

}