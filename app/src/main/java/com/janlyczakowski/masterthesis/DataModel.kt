package com.janlyczakowski.masterthesis

class DataModel {
    data class User(
        val ID: Int,
        val gender: String,
        val age: Int,
        val mobile_maps_experience: Int)

    data class Scenario(
        val user_id: Int,
        val time_NA: Float,
        val correct_NA: Int,
        val confidence_NA: Int,
        val time_A: Float,
        val correct_A: Int,
        val confidence_A: Int,
        val familiarity: Int,
        val usefulness: String,
        val difficulties: String,
        val difficulties_comment: String,
        val issues: String,
        val issues_comment: String
    )

}