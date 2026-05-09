package com.example.flightschedule.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FlightDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE airports (
                _id INTEGER PRIMARY KEY AUTOINCREMENT,
                code TEXT NOT NULL UNIQUE,
                name TEXT NOT NULL
            );
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE aircraft_types (
                _id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                range_km INTEGER NOT NULL,
                flight_level_m INTEGER NOT NULL
            );
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE flights (
                _id INTEGER PRIMARY KEY AUTOINCREMENT,
                number TEXT NOT NULL,
                aircraft_type_id INTEGER NOT NULL,
                dep_airport_code TEXT NOT NULL,
                arr_airport_code TEXT NOT NULL,
                flight_date TEXT NOT NULL,
                dep_time TEXT NOT NULL,
                FOREIGN KEY(aircraft_type_id) REFERENCES aircraft_types(_id)
            );
            """.trimIndent()
        )
        seed(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS flights")
        db.execSQL("DROP TABLE IF EXISTS aircraft_types")
        db.execSQL("DROP TABLE IF EXISTS airports")
        onCreate(db)
    }

    private fun seed(db: SQLiteDatabase) {
        fun airport(code: String, name: String) {
            db.insert("airports", null, ContentValues().apply {
                put("code", code)
                put("name", name)
            })
        }
        fun type(name: String, range: Int, level: Int): Long {
            val id = db.insert(
                "aircraft_types",
                null,
                ContentValues().apply {
                    put("name", name)
                    put("range_km", range)
                    put("flight_level_m", level)
                }
            )
            return id
        }
        fun flight(
            number: String,
            typeId: Long,
            dep: String,
            arr: String,
            date: String,
            time: String
        ) {
            db.insert(
                "flights",
                null,
                ContentValues().apply {
                    put("number", number)
                    put("aircraft_type_id", typeId)
                    put("dep_airport_code", dep)
                    put("arr_airport_code", arr)
                    put("flight_date", date)
                    put("dep_time", time)
                }
            )
        }

        airport("SVO", "Шереметьево")
        airport("DME", "Домодедово")
        airport("LED", "Пулково")
        airport("KGD", "Храброво")

        val t737 = type("Boeing 737-800", 5500, 11000)
        val t320 = type("Airbus A320neo", 6300, 11600)
        val t777 = type("Boeing 777-300ER", 13650, 13100)
        val tATR = type("ATR 72-600", 1500, 7600)

        flight("SU1234", t320, "LED", "SVO", "2026-05-05", "08:10")
        flight("SU5678", t737, "KGD", "SVO", "2026-05-05", "14:25")
        flight("FV901", tATR, "KGD", "LED", "2026-05-05", "19:00")
        flight("DP402", t737, "DME", "LED", "2026-05-05", "06:55")
        flight("SU1001", t777, "SVO", "LED", "2026-05-06", "10:00")
        flight("U6123", t320, "SVO", "KGD", "2026-05-04", "22:15")
        flight("SU2468", t737, "LED", "DME", "2026-05-05", "11:40")
        flight("FV210", tATR, "LED", "KGD", "2026-05-06", "07:30")
        flight("SU3000", t320, "LED", "SVO", "2026-05-07", "16:20")
    }

    companion object {
        const val DATABASE_NAME = "flights.db"
        const val DATABASE_VERSION = 1
    }
}
