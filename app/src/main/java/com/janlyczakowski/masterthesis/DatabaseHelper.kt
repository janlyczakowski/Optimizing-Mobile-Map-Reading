package com.janlyczakowski.masterthesis

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "MasterThesis.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME_1 = "User"
        const val TABLE_NAME_2 = "Scenario_1"
        const val TABLE_NAME_3 = "Scenario_2"
        const val TABLE_NAME_4 = "Scenario_3"

        // User columns
        const val USER_COL_1 = "ID"
        const val USER_COL_2 = "gender"
        const val USER_COL_3 = "age"
        const val USER_COL_4 = "mobile_maps_experience"

        // Scenario 1 columns
        const val SC1_COL_1 = "user_id"
        const val SC1_COL_2 = "time_NA"
        const val SC1_COL_3 = "correct_NA"
        const val SC1_COL_4 = "confidence_NA"
        const val SC1_COL_5 = "time_A"
        const val SC1_COL_6 = "correct_A"
        const val SC1_COL_7 = "confidence_A"
        const val SC1_COL_8 = "familiarity"
        const val SC1_COL_9 = "usefulness"
        const val SC1_COL_10 = "difficulties"
        const val SC1_COL_11 = "difficulties_comment"
        const val SC1_COL_12 = "issues"
        const val SC1_COL_13 = "issues_comment"

        // Scenario 2 columns
        const val SC2_COL_1 = "user_id"
        const val SC2_COL_2 = "time_NA"
        const val SC2_COL_3 = "correct_NA"
        const val SC2_COL_4 = "confidence_NA"
        const val SC2_COL_5 = "time_A"
        const val SC2_COL_6 = "correct_A"
        const val SC2_COL_7 = "confidence_A"
        const val SC2_COL_8 = "familiarity"
        const val SC2_COL_9 = "usefulness"
        const val SC2_COL_10 = "difficulties"
        const val SC2_COL_11 = "difficulties_comment"
        const val SC2_COL_12 = "issues"
        const val SC2_COL_13 = "issues_comment"

        // Scenario 3 columns
        const val SC3_COL_1 = "user_id"
        const val SC3_COL_2 = "time_NA"
        const val SC3_COL_3 = "correct_NA"
        const val SC3_COL_4 = "confidence_NA"
        const val SC3_COL_5 = "time_A"
        const val SC3_COL_6 = "correct_A"
        const val SC3_COL_7 = "confidence_A"
        const val SC3_COL_8 = "familiarity"
        const val SC3_COL_9 = "usefulness"
        const val SC3_COL_10 = "difficulties"
        const val SC3_COL_11 = "difficulties_comment"
        const val SC3_COL_12 = "issues"
        const val SC3_COL_13 = "issues_comment"


    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery1 =
            "CREATE TABLE $TABLE_NAME_1 (" +
                    "$USER_COL_1 INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," +
                    "$USER_COL_2 TEXT NOT NULL," +
                    "$USER_COL_3 INTEGER NOT NULL," +
                    "$USER_COL_4 INTEGER NOT NULL" +
                    ")"

        val createTableQuery2 =
            "CREATE TABLE $TABLE_NAME_2 (" +
                    "$SC1_COL_1 INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," +
                    "$SC1_COL_2 REAL NOT NULL," +
                    "$SC1_COL_3 INTEGER NOT NULL," +
                    "$SC1_COL_4 INTEGER NOT NULL," +
                    "$SC1_COL_5 REAL NOT NULL," +
                    "$SC1_COL_6 INTEGER NOT NULL," +
                    "$SC1_COL_7 INTEGER NOT NULL," +
                    "$SC1_COL_8 INTEGER NOT NULL," +
                    "$SC1_COL_9 TEXT NOT NULL," +
                    "$SC1_COL_10 TEXT NOT NULL," +
                    "$SC1_COL_11 TEXT," +
                    "$SC1_COL_12 TEXT NOT NULL," +
                    "$SC1_COL_13 TEXT," +
                    "FOREIGN KEY ($SC1_COL_1) REFERENCES $TABLE_NAME_1($USER_COL_1)" +
                    ")"
        val createTableQuery3 =
            "CREATE TABLE $TABLE_NAME_3 (" +
                    "$SC2_COL_1 INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," +
                    "$SC2_COL_2 REAL NOT NULL," +
                    "$SC2_COL_3 INTEGER NOT NULL," +
                    "$SC2_COL_4 INTEGER NOT NULL," +
                    "$SC2_COL_5 REAL NOT NULL," +
                    "$SC2_COL_6 INTEGER NOT NULL," +
                    "$SC2_COL_7 INTEGER NOT NULL," +
                    "$SC2_COL_8 INTEGER NOT NULL," +
                    "$SC2_COL_9 TEXT NOT NULL," +
                    "$SC2_COL_10 TEXT NOT NULL," +
                    "$SC2_COL_11 TEXT," +
                    "$SC2_COL_12 TEXT NOT NULL," +
                    "$SC2_COL_13 TEXT," +
                    "FOREIGN KEY ($SC2_COL_1) REFERENCES $TABLE_NAME_1($USER_COL_1)" +
                    ")"

        val createTableQuery4 =
            "CREATE TABLE $TABLE_NAME_4 (" +
                    "$SC3_COL_1 INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," +
                    "$SC3_COL_2 REAL NOT NULL," +
                    "$SC3_COL_3 INTEGER NOT NULL," +
                    "$SC3_COL_4 INTEGER NOT NULL," +
                    "$SC3_COL_5 REAL NOT NULL," +
                    "$SC3_COL_6 INTEGER NOT NULL," +
                    "$SC3_COL_7 INTEGER NOT NULL," +
                    "$SC3_COL_8 INTEGER NOT NULL," +
                    "$SC3_COL_9 TEXT NOT NULL," +
                    "$SC3_COL_10 TEXT NOT NULL," +
                    "$SC3_COL_11 TEXT," +
                    "$SC3_COL_12 TEXT NOT NULL," +
                    "$SC3_COL_13 TEXT," +
                    "FOREIGN KEY ($SC3_COL_1) REFERENCES $TABLE_NAME_1($USER_COL_1)" +
                    ")"

        db?.execSQL(createTableQuery1)
        db?.execSQL(createTableQuery2)
        db?.execSQL(createTableQuery3)
        db?.execSQL(createTableQuery4)


    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_1")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_2")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_3")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_4")
        onCreate(db)
    }

    fun insertUser(user: DataModel.User) {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(USER_COL_2, user.gender)
            put(USER_COL_3, user.age)
            put(USER_COL_4, user.mobile_maps_experience)
        }

        db.insert(TABLE_NAME_1, null, values)
        db.close()
    }

    fun insertScenario1(scenario1: DataModel.Scenario, userId: Int){
        val db = writableDatabase

        val values = ContentValues().apply {
            put(SC1_COL_1, userId)
            put(SC1_COL_2, scenario1.time_NA)
            put(SC1_COL_3, scenario1.correct_NA)
            put(SC1_COL_4, scenario1.confidence_NA)
            put(SC1_COL_5, scenario1.time_A)
            put(SC1_COL_6, scenario1.correct_A)
            put(SC1_COL_7, scenario1.confidence_A)
            put(SC1_COL_8, scenario1.familiarity)
            put(SC1_COL_9, scenario1.usefulness)
            put(SC1_COL_10, scenario1.difficulties)
            put(SC1_COL_11, scenario1.difficulties_comment)
            put(SC1_COL_12, scenario1.issues)
            put(SC1_COL_13, scenario1.issues_comment)
        }

        db.insert(TABLE_NAME_2, null, values)
        db.close()
    }

    fun insertScenario2(scenario2: DataModel.Scenario,userId: Int){
        val db = writableDatabase

        val values = ContentValues().apply {
            put(SC2_COL_1, userId)
            put(SC2_COL_2, scenario2.time_NA)
            put(SC2_COL_3, scenario2.correct_NA)
            put(SC2_COL_4, scenario2.confidence_NA)
            put(SC2_COL_5, scenario2.time_A)
            put(SC2_COL_6, scenario2.correct_A)
            put(SC2_COL_7, scenario2.confidence_A)
            put(SC2_COL_8, scenario2.familiarity)
            put(SC2_COL_9, scenario2.usefulness)
            put(SC2_COL_10, scenario2.difficulties)
            put(SC2_COL_11, scenario2.difficulties_comment)
            put(SC2_COL_12, scenario2.issues)
            put(SC2_COL_13, scenario2.issues_comment)
        }

        db.insert(TABLE_NAME_3, null, values)
        db.close()
    }

    fun insertScenario3(scenario3: DataModel.Scenario,userId: Int){
        val db = writableDatabase

        val values = ContentValues().apply {
            put(SC3_COL_1, userId)
            put(SC3_COL_2, scenario3.time_NA)
            put(SC3_COL_3, scenario3.correct_NA)
            put(SC3_COL_4, scenario3.confidence_NA)
            put(SC3_COL_5, scenario3.time_A)
            put(SC3_COL_6, scenario3.correct_A)
            put(SC3_COL_7, scenario3.confidence_A)
            put(SC3_COL_8, scenario3.familiarity)
            put(SC3_COL_9, scenario3.usefulness)
            put(SC3_COL_10, scenario3.difficulties)
            put(SC3_COL_11, scenario3.difficulties_comment)
            put(SC3_COL_12, scenario3.issues)
            put(SC3_COL_13, scenario3.issues_comment)
        }

        db.insert(TABLE_NAME_4, null, values)
        db.close()
    }
}