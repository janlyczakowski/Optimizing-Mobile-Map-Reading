package com.janlyczakowski.masterthesis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast

class PreTestSurvey : AppCompatActivity() {

    private lateinit var linearLayout: LinearLayout
    private lateinit var db: DatabaseHelper
    private var experience: Int = -1
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pre_test_survey)

        // Initialize the SharedPreferencesHelpe
        sharedPreferencesHelper = SharedPreferencesHelper(this)

        // Initialize your LinearLayout
        linearLayout = findViewById(R.id.pretest_survey_ratingbar_1)

        val text1 = getString(R.string.pretest_survey_question_1)
        val textToModify1 = listOf("1.")
        val text1Modified = Utils().modifyNumberStyle(text1, textToModify1, this)
        val text1View = findViewById<TextView>(R.id.pretest_survey_question_1)
        text1View.text = text1Modified

        val text2 = getString(R.string.pretest_survey_question_2)
        val textToModify2 = listOf("2.")
        val text2Modified = Utils().modifyNumberStyle(text2, textToModify2, this)
        val text2View = findViewById<TextView>(R.id.pretest_survey_question_2)
        text2View.text = text2Modified

        val text3 = getString(R.string.pretest_survey_question_3)
        val textToModify3 = listOf("3.")
        val text3Modified = Utils().modifyNumberStyle(text3, textToModify3, this)
        val text3View = findViewById<TextView>(R.id.pretest_survey_question_3)
        text3View.text = text3Modified

        db = DatabaseHelper(this)
    }

    fun onClickRatingBarBtnListener(view: android.view.View) {
        Utils().changeRatingBarStyling(view, linearLayout, this)
        // save the rating selected
        experience = (view as Button).text.toString().toInt()

    }

    fun onClickPretestSurveyBtnListener(View: android.view.View) {

        val question1Answers = findViewById<RadioGroup>(R.id.pretest_survey_radio_group)
        val answer1Id = question1Answers.checkedRadioButtonId
        val ageText = findViewById<TextView>(R.id.pretest_survey_answer_2).text.toString()

        if (answer1Id != -1 && ageText.isNotEmpty() && experience != -1) {
            val selectedAnswer1 = findViewById<RadioButton>(answer1Id)
            val gender = selectedAnswer1.text.toString()

            val user = DataModel.User(0, gender, ageText.toInt(), experience)
            db.insertUser(user)

            // increment and store the user ID after the user was added to databse
            sharedPreferencesHelper.incrementAndStoreUserId()



            // start another activity
            val intent = Intent(this, Scenario1Task1::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Please answer all questions", Toast.LENGTH_SHORT).show()

        }


    }
}