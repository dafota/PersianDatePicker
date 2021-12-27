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

        with(binding) {
            ivNextMonth.setOnClickListener { persianDatePicker.nextMonth() }
            ivPreviousMonth.setOnClickListener { persianDatePicker.previousMonth() }
            btnLogDate.setOnClickListener {
                val calender = Calendar.getInstance()
                calender.timeInMillis = persianDatePicker.toDate().time
                calender.set(Calendar.MILLISECOND, 0)
                calender.set(Calendar.SECOND, 0)
                calender.set(Calendar.MINUTE, 0)
                calender.set(Calendar.HOUR_OF_DAY, 0)

                Log.e(TAG, "onCreate: ${persianDatePicker.toDate()}")
                Log.e(TAG, "onCreate: ${calender.time}")
            }

            persianDatePicker.setListener {
                tvSelectedYear.text = it.selectedDay.yearNumber.toString()
                tvSelectedDay.text = StringBuilder()
                    .append(it.selectedDay.dayName)
                    .append(", ")
                    .append(it.selectedDay.dayNumber)
                    .append(" ")
                    .append(it.selectedDay.monthName)
                    .toString()

                tvCalendarTitle.text = StringBuilder()
                    .append(it.selectedDay.monthName)
                    .append(" ")
                    .append(it.selectedDay.yearNumber)
                    .toString()
            }
        }

        binding.persianDatePicker.init(System.currentTimeMillis())
    }


}