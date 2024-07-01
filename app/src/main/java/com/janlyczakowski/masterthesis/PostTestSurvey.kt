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

class PostTestSurvey : AppCompatActivity() {
    private lateinit var linearLayout: LinearLayout
    private lateinit var mapScenario: Utils.Properties
    private var familiarity: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_test_survey)

        mapScenario = intent.getSerializableExtra("properties") as Utils.Properties
        linearLayout = findViewById(R.id.post_test_survey_ratingbar_1)

        val text1 = getString(R.string.post_test_survey_question_1)
        val textToModify1 = listOf("1.")
        val text1Modified = Utils().modifyNumberStyle(text1, textToModify1, this)
        val text1View = findViewById<TextView>(R.id.post_test_survey_question_1)
        text1View.text = text1Modified

        val text2 = getString(R.string.post_test_survey_question_2)
        val textToModify2 = listOf("2.")
        val text2Modified = Utils().modifyNumberStyle(text2, textToModify2, this)
        val text2View = findViewById<TextView>(R.id.post_test_survey_question_2)
        text2View.text = text2Modified

        val text3 = getString(R.string.post_test_survey_question_3_1)
        val textToModify3 = listOf("3.")
        val text3Modified = Utils().modifyNumberStyle(text3, textToModify3, this)
        val text3View = findViewById<TextView>(R.id.post_test_survey_question_3_1)
        text3View.text = text3Modified

        val text4 = getString(R.string.post_test_survey_question_4_1)
        val textToModify4 = listOf("4.")
        val text4Modified = Utils().modifyNumberStyle(text4, textToModify4, this)
        val text4View = findViewById<TextView>(R.id.post_test_survey_question_4_1)
        text4View.text = text4Modified
    }

    fun onClickRatingBarPostSurveyBtnListener(view: android.view.View) {
        Utils().changeRatingBarStyling(view, linearLayout, this)
        // save the rating selected
        familiarity = (view as Button).text.toString().toInt()
    }

    fun onClickPosttestSurveyBtnListener(View: android.view.View) {

        val question2Answers = findViewById<RadioGroup>(R.id.post_test_survey_radio_group_2)
        val answer2Id = question2Answers.checkedRadioButtonId

        val question3Answers = findViewById<RadioGroup>(R.id.post_test_survey_radio_group_3)
        val answer3Id = question3Answers.checkedRadioButtonId

        val question4Answers = findViewById<RadioGroup>(R.id.post_test_survey_radio_group_4)
        val answer4Id = question4Answers.checkedRadioButtonId

        // check if all the mandatory answers are selected
        if (familiarity != -1 && answer2Id != -1 && answer3Id != -1 && answer4Id != -1) {
            val selectedAnswer2 = findViewById<RadioButton>(answer2Id)
            val usefulness = selectedAnswer2.text.toString()

            val selectedAnswer3 = findViewById<RadioButton>(answer3Id)
            val difficulties = selectedAnswer3.text.toString()
            val difficultiesText = findViewById<TextView>(R.id.post_test_survey_answer_3_comment).text.toString()
            val difficultiesComment = if(difficultiesText.isNotEmpty()) difficultiesText else "no comment"

            val selectedAnswer4 = findViewById<RadioButton>(answer4Id)
            val issues = selectedAnswer4.text.toString()
            val issuesText = findViewById<TextView>(R.id.post_test_survey_answer_4_comment).text.toString()
            val issuesComment = if(issuesText.isNotEmpty()) issuesText else "no comment"

            // Take the shared preferences from the scenario
            val sharedPreferencesHelper = SharedPreferencesHelper(this)
            val currentUserId = sharedPreferencesHelper.getCurrentUserId()
            val scenarioData = sharedPreferencesHelper.getScenarioData()

            val scenario = DataModel.Scenario(
                currentUserId,
                scenarioData["time_NA"] as Float,
                scenarioData["correct_NA"] as Int,
                scenarioData["confidence_NA"] as Int,
                scenarioData["time_A"] as Float,
                scenarioData["correct_A"] as Int,
                scenarioData["confidence_A"] as Int,
                familiarity,
                usefulness,
                difficulties,
                difficultiesComment,
                issues,
                issuesComment
            )

            // Save all of the results from the scenario to a database
            val db = DatabaseHelper(this)
            when (mapScenario.scenario) {
                1 -> {
                    db.insertScenario1(scenario, currentUserId)
                }
                2 -> {
                    db.insertScenario2(scenario, currentUserId)
                }
                3 -> {
                    db.insertScenario3(scenario, currentUserId)
                }
            }

        }else{
            Toast.makeText(this, "Please answer all questions", Toast.LENGTH_SHORT).show()
            return
        }

        val properties =
            Utils.Properties(
                mapScenario.url,
                mapScenario.poiName,
                mapScenario.scenario,
                mapScenario.task,
                mapScenario.adaptive
            )

        when (mapScenario.scenario) {
            1 -> {
                val intent = Intent(this, ConfirmationPanel1::class.java)
                intent.putExtra("properties", properties)
                startActivity(intent)
            }

            2 -> {
                val intent = Intent(this, ConfirmationPanel2::class.java)
                intent.putExtra("properties", properties)
                startActivity(intent)
            }

            3 -> {
                val intent = Intent(this, ConfirmationPanel3::class.java)
                intent.putExtra("properties", properties)
                startActivity(intent)
            }
        }
    }
}