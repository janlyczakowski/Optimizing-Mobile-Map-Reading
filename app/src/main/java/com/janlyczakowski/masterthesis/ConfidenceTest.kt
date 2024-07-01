package com.janlyczakowski.masterthesis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast

class ConfidenceTest : AppCompatActivity() {
    private lateinit var linearLayout: LinearLayout
    private lateinit var mapScenario: Utils.Properties
    private var confidence: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confidence_test)
        mapScenario = intent.getSerializableExtra("properties") as Utils.Properties

        linearLayout = findViewById(R.id.confidence_test_ratingbar_1)
    }

    fun onClickRatingBarBtnConfidenceListener(view: View) {
        Utils().changeRatingBarStyling(view, linearLayout, this)
        // save the rating selected
        confidence = (view as Button).text.toString().toInt()
    }

    fun onClickConfidenceTestBtnListener(view: View) {
        // check if all questions have been answered
        val allQuestionsAnswered = confidence != -1
        if (!allQuestionsAnswered) {
            Toast.makeText(this, "Please answer the question", Toast.LENGTH_SHORT).show()
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

        // save confidence in shared preferences
        val sharedPreferencesHelper = SharedPreferencesHelper(this@ConfidenceTest)
        if (mapScenario.adaptive) {
            sharedPreferencesHelper.updateConfidenceA(confidence)
        } else {
            sharedPreferencesHelper.updateConfidenceNA(confidence)
        }


        when (mapScenario.scenario) {
            1 -> {
                if (mapScenario.task == 1) {
                    val intent = Intent(this, Scenario1Task2::class.java)
                    startActivity(intent)
                } else if (mapScenario.task == 2) {
                    val intent = Intent(this, PostTestSurvey::class.java)
                    intent.putExtra("properties", properties)
                    startActivity(intent)
                }
            }

            2 -> {
                if (mapScenario.task == 1) {
                    val intent = Intent(this, Scenario2Task2::class.java)
                    startActivity(intent)
                } else if (mapScenario.task == 2) {
                    val intent = Intent(this, PostTestSurvey::class.java)
                    intent.putExtra("properties", properties)
                    startActivity(intent)
                }
            }

            3 -> {
                if (mapScenario.task == 1) {
                    val intent = Intent(this, Scenario3Task2::class.java)
                    startActivity(intent)
                } else if (mapScenario.task == 2) {
                    val intent = Intent(this, PostTestSurvey::class.java)
                    intent.putExtra("properties", properties)
                    startActivity(intent)
                }
            }


        }

    }
}