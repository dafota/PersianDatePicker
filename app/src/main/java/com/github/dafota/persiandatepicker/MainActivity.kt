package com.github.dafota.persiandatepicker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.dafota.persiandatepicker.databinding.ActivityMainBinding
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        binding.persianDatePicker.init(System.currentTimeMillis())

        with(binding) {
            ivNextMonth.setOnClickListener { persianDatePicker.nextMonth() }
            ivPreviousMonth.setOnClickListener { persianDatePicker.previousMonth() }

            btnLogDate.setOnClickListener {
                val calender = Calendar.getInstance()

                calender.timeInMillis = persianDatePicker.getSelectedDate().time
                calender.set(Calendar.MILLISECOND, 0)
                calender.set(Calendar.SECOND, 0)
                calender.set(Calendar.MINUTE, 0)
                calender.set(Calendar.HOUR_OF_DAY, 0)

                Log.e(TAG, "selectedDate ${persianDatePicker.getSelectedDate()}")
                Log.e(TAG, "selectedDate ${calender.time}")

                calender.timeInMillis = persianDatePicker.getTodayDate().time
                calender.set(Calendar.MILLISECOND, 0)
                calender.set(Calendar.SECOND, 0)
                calender.set(Calendar.MINUTE, 0)
                calender.set(Calendar.HOUR_OF_DAY, 0)

                Log.e(TAG, "today ${persianDatePicker.getTodayDate()}")
                Log.e(TAG, "today ${calender.time}")
            }

            persianDatePicker.setListener { selectedDay, _ ->
                tvSelectedYear.text = selectedDay.yearNumber.toString()
                tvSelectedDay.text = StringBuilder()
                    .append(selectedDay.dayName)
                    .append(", ")
                    .append(selectedDay.dayNumber)
                    .append(" ")
                    .append(selectedDay.monthName)
                    .toString()

                tvCalendarTitle.text = StringBuilder()
                    .append(selectedDay.monthName)
                    .append(" ")
                    .append(selectedDay.yearNumber)
                    .toString()
            }
        }

    }

}