package com.janlyczakowski.masterthesis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class Scenario1Task2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scenario1_task2)

        val text1 = getString(R.string.scenario_1_task_2_message_3)
        val textToModify1 = listOf("Your task:","museum")
        val text1Modified = Utils().modifyNumberStyle(text1,textToModify1,this)
        val text1View = findViewById<TextView>(R.id.scenario_1_task_2_message_3)
        text1View.text = text1Modified

        val text2 = getString(R.string.scenario_1_task_2_message_5)
        val textToModify2 = listOf("Keep on walking")
        val text2Modified = Utils().modifyNumberStyle(text2,textToModify2,this)
        val text2View = findViewById<TextView>(R.id.scenario_1_task_2_message_5)
        text2View.text = text2Modified

        val text3 = getString(R.string.noise_warning_message_2)
        val textToModify3 = listOf("Very loud sound")
        val text3Modified = Utils().modifyNumberStyle(text3,textToModify3,this)
        val text3View = findViewById<TextView>(R.id.noise_warning_message_2)
        text3View.text = text3Modified

        val text4 = getString(R.string.scenario_1_task_2_message_6)
        val textToModify4 = listOf("warning message")
        val text4Modified = Utils().modifyNumberStyle(text4,textToModify4,this)
        val text4View = findViewById<TextView>(R.id.scenario_1_task_2_message_6)
        text4View.text = text4Modified

        val text5 = getString(R.string.scenario_headphones_message)
        val textToModify5 = listOf("headphones")
        val text5Modified = Utils().modifyNumberStyle(text5,textToModify5,this)
        val text5View = findViewById<TextView>(R.id.scenario_headphones_message)
        text5View.text = text5Modified

    }
    fun onClickScenario1Task2BtnListener(view: View) {
        // Open map
        val properties =
            Utils.Properties(Constants.URL_ADAPTIVE_SCENARIO_1, Constants.POI_SCENARIO_I_ADAPTIVE,1,2,true)
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra("properties", properties )
        startActivity(intent)
    }
}