package com.janlyczakowski.masterthesis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Scenario3Task2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scenario3_task2)

        val text1 = getString(R.string.scenario_3_task_2_message_3)
        val textToModify1 = listOf("Your task:","area to practice sport")
        val text1Modified = Utils().modifyNumberStyle(text1,textToModify1,this)
        val text1View = findViewById<TextView>(R.id.scenario_3_task_2_message_3)
        text1View.text = text1Modified

        val text2 = getString(R.string.scenario_3_task_2_message_5)
        val textToModify2 = listOf("Keep on walking")
        val text2Modified = Utils().modifyNumberStyle(text2,textToModify2,this)
        val text2View = findViewById<TextView>(R.id.scenario_3_task_2_message_5)
        text2View.text = text2Modified

        val text3 = getString(R.string.scenario_3_task_2_message_6)
        val textToModify3 = listOf("warning message")
        val text3Modified = Utils().modifyNumberStyle(text3,textToModify3,this)
        val text3View = findViewById<TextView>(R.id.scenario_3_task_2_message_6)
        text3View.text = text3Modified

        val text4 = getString(R.string.scenario_headphones_message)
        val textToModify4 = listOf("headphones")
        val text4Modified = Utils().modifyNumberStyle(text4,textToModify4,this)
        val text4View = findViewById<TextView>(R.id.scenario_headphones_message)
        text4View.text = text4Modified

    }
    fun onClickScenario3Task2ButtonListener(View: android.view.View){

        val properties =
            Utils.Properties(Constants.URL_ADAPTIVE_SCENARIO_3, Constants.POI_SCENARIO_III_ADAPTIVE,3,2,true)
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra("properties", properties )
        startActivity(intent)
    }
}