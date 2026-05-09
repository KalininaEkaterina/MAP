package com.example.flightschedule.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.flightschedule.data.AircraftType
import com.example.flightschedule.data.Airport
import com.example.flightschedule.data.FlightListItem

class FlightRepository(context: Context) {

    private val helper = FlightDbHelper(context.applicationContext)

    fun readable(): SQLiteDatabase = helper.readableDatabase

    fun allAirports(): List<Airport> {
        readable().rawQuery(
            "SELECT code, name FROM airports ORDER BY code",
            null
        ).use { c ->
            val out = ArrayList<Airport>()
            while (c.moveToNext()) {
                out.add(Airport(c.getString(0), c.getString(1)))
            }
            return out
        }
    }

    /** (а) Рейсы прибытия в аэропорт на дату */
    fun arrivalsAtAirport(airportCode: String, date: String): List<FlightListItem> {
        val sql = """
            SELECT f.number, t.name, f.dep_airport_code, f.arr_airport_code,
                   f.flight_date, f.dep_time
            FROM flights f
            JOIN aircraft_types t ON t._id = f.aircraft_type_id
            WHERE f.arr_airport_code = ? AND f.flight_date = ?
            ORDER BY f.dep_time
        """.trimIndent()
        readable().rawQuery(sql, arrayOf(airportCode, date)).use { c ->
            return mapFlights(c)
        }
    }

    /** (б) Типы ВС, задействованные в рейсах через выбранный аэропорт */
    fun aircraftTypesForAirport(airportCode: String): List<AircraftType> {
        val sql = """
            SELECT DISTINCT t._id, t.name, t.range_km, t.flight_level_m
            FROM aircraft_types t
            JOIN flights f ON f.aircraft_type_id = t._id
            WHERE f.dep_airport_code = ? OR f.arr_airport_code = ?
            ORDER BY t.name
        """.trimIndent()
        readable().rawQuery(sql, arrayOf(airportCode, airportCode)).use { c ->
            val out = ArrayList<AircraftType>()
            while (c.moveToNext()) {
                out.add(
                    AircraftType(
                        id = c.getLong(0),
                        name = c.getString(1),
                        rangeKm = c.getInt(2),
                        flightLevelM = c.getInt(3)
                    )
                )
            }
            return out
        }
    }

    /** (в) Рейсы по пунктам вылета/прилёта и дате */
    fun flightsByRoute(depCode: String, arrCode: String, date: String): List<FlightListItem> {
        val sql = """
            SELECT f.number, t.name, f.dep_airport_code, f.arr_airport_code,
                   f.flight_date, f.dep_time
            FROM flights f
            JOIN aircraft_types t ON t._id = f.aircraft_type_id
            WHERE f.dep_airport_code = ? AND f.arr_airport_code = ? AND f.flight_date = ?
            ORDER BY f.dep_time
        """.trimIndent()
        readable().rawQuery(sql, arrayOf(depCode, arrCode, date)).use { c ->
            return mapFlights(c)
        }
    }

    private fun mapFlights(c: android.database.Cursor): List<FlightListItem> {
        val out = ArrayList<FlightListItem>()
        while (c.moveToNext()) {
            out.add(
                FlightListItem(
                    flightNumber = c.getString(0),
                    aircraftTypeName = c.getString(1),
                    depCode = c.getString(2),
                    arrCode = c.getString(3),
                    date = c.getString(4),
                    depTime = c.getString(5)
                )
            )
        }
        return out
    }
}
