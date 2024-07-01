package com.janlyczakowski.masterthesis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Scenario2Task1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scenario2_task1)

        val text1 = getString(R.string.scenario_2_task_1_message_3)
        val textToModify1 = listOf("Your task:","traffic signal")
        val text1Modified = Utils().modifyNumberStyle(text1,textToModify1,this)
        val text1View = findViewById<TextView>(R.id.scenario_2_task_1_message_3)
        text1View.text = text1Modified

        val text2 = getString(R.string.scenario_2_task_1_message_5)
        val textToModify2 = listOf("run")
        val text2Modified = Utils().modifyNumberStyle(text2,textToModify2,this)
        val text2View = findViewById<TextView>(R.id.scenario_2_task_1_message_5)
        text2View.text = text2Modified

        val text3 = getString(R.string.scenario_2_task_1_message_6)
        val textToModify3 = listOf("warning message")
        val text3Modified = Utils().modifyNumberStyle(text3,textToModify3,this)
        val text3View = findViewById<TextView>(R.id.scenario_2_task_1_message_6)
        text3View.text = text3Modified

        val text4 = getString(R.string.scenario_headphones_message)
        val textToModify4 = listOf("headphones")
        val text4Modified = Utils().modifyNumberStyle(text4,textToModify4,this)
        val text4View = findViewById<TextView>(R.id.scenario_headphones_message)
        text4View.text = text4Modified
    }


    fun onClickScenario2Task1ButtonListener(View: android.view.View){

        val properties =
            Utils.Properties(Constants.URL_ADAPTIVE_SCENARIO_2, Constants.POI_SCENARIO_II_ADAPTIVE,2,1,true)
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra("properties", properties )
        startActivity(intent)
    }
}