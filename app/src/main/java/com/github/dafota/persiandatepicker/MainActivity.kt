package com.github.dafota.persiandatepicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.dafota.persiandatepicker.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        with(binding) {
            ivNextMonth.setOnClickListener { persianDatePicker.nextMonth() }
            ivPreviousMonth.setOnClickListener { persianDatePicker.previousMonth() }

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

    }


}