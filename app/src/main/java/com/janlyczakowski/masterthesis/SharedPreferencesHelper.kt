package com.janlyczakowski.masterthesis


import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val PREFS_NAME = "current_scenario_data"
    private val USER_ID_KEY = "user_id"
    // Scenario values
    val TIME_NA = "time_NA"
    val CORRECT_NA = "correct_NA"
    val CONFIDENCE_NA = "confidence_NA"
    val TIME_A = "time_A"
    val CORRECT_A = "correct_A"
    val CONFIDENCE_A = "confidence_A"
    val WARNING_MSG_START_TIME = "warning_msg_start_time"
    val WARNING_MSG_END_TIME = "warning_msg_end_time"

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getCurrentUserId(): Int {
        return sharedPreferences.getInt(USER_ID_KEY, 0)
    }
    fun getScenarioData(): Map<String, Any> {
        return mapOf(
            TIME_NA to sharedPreferences.getFloat(TIME_NA, 0.0f),
            CORRECT_NA to sharedPreferences.getInt(CORRECT_NA, 0),
            CONFIDENCE_NA to sharedPreferences.getInt(CONFIDENCE_NA, 0),
            TIME_A to sharedPreferences.getFloat(TIME_A, 0.0f),
            CORRECT_A to sharedPreferences.getInt(CORRECT_A, 0),
            CONFIDENCE_A to sharedPreferences.getInt(CONFIDENCE_A, 0)
        )
    }

    fun incrementAndStoreUserId() {
        val currentUserId = getCurrentUserId()
        sharedPreferences.edit().putInt(USER_ID_KEY, currentUserId + 1).apply()
    }
    fun updateMapDataNA(time: Float, correct: Int) {
        sharedPreferences.edit().putFloat(TIME_NA, time).apply()
        sharedPreferences.edit().putInt(CORRECT_NA, correct).apply()
    }
    fun updateMapDataA(time: Float, correct: Int) {
        sharedPreferences.edit().putFloat(TIME_A, time).apply()
        sharedPreferences.edit().putInt(CORRECT_A, correct).apply()
    }

    fun updateConfidenceNA(confidence: Int) {
        sharedPreferences.edit().putInt(CONFIDENCE_NA, confidence).apply()
    }
    fun updateConfidenceA(confidence: Int) {
        sharedPreferences.edit().putInt(CONFIDENCE_A, confidence).apply()
    }

    fun updateWarningMsgStartTime(time: Long) {
        sharedPreferences.edit().putLong(WARNING_MSG_START_TIME, time).apply()
    }
    fun getWarningMsgStartTime(): Long {
        return sharedPreferences.getLong(WARNING_MSG_START_TIME, 0)
    }
    fun updateWarningMsgEndTime(time: Long) {
        sharedPreferences.edit().putLong(WARNING_MSG_END_TIME, time).apply()
    }
    fun getWarningMsgEndTime(): Long {
        return sharedPreferences.getLong(WARNING_MSG_END_TIME, 0)
    }

}