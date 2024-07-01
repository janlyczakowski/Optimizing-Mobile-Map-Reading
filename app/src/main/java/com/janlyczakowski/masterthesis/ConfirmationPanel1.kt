package com.janlyczakowski.masterthesis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ConfirmationPanel1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confirmation_panel1)

        val text1 = getString(R.string.confirmation_panel_1_message_3)
        val textToModify1 = listOf("second location")
        val text1Modified = Utils().modifyNumberStyle(text1,textToModify1,this)
        val text1View = findViewById<TextView>(R.id.confirmation_panel_1_message_3)
        text1View.text = text1Modified
    }
    fun onClickConfirmationPanel1BtnListener(View: android.view.View){
        val intent = Intent(this, Scenario2Task1::class.java)
        startActivity(intent)
    }
}