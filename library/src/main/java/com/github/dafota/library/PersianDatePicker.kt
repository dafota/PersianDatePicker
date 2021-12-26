package com.github.dafota.library

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.dafota.library.model.Day
import com.github.dafota.library.model.EnumDay
import com.github.dafota.library.utils.asEnum
import com.github.dafota.library.utils.inc
import com.github.dafota.library.utils.inflater
import saman.zamani.persiandate.PersianDate

class PersianDatePicker : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val persianDate = PersianDate()
    private val content: ConstraintLayout =
        inflater.inflate(R.layout.base_layout, this, false) as ConstraintLayout
    private val tvMonth: TextView = content.findViewById(R.id.tv_month) as TextView
    private val tvSelectedDate: TextView = content.findViewById(R.id.tv_selected_date)
    private val rvMonth: RecyclerView = content.findViewById(R.id.rv_month)
    private val rvDaysOfWeeek: RecyclerView = content.findViewById(R.id.rv_days)

    init {
        addView(content)

        val ivNextMonth = content.findViewById<ImageView>(R.id.iv_next_month)
        val ivPreMonth = content.findViewById<ImageView>(R.id.iv_previous_month)

        tvMonth.text = "${persianDate.monthName()} ${persianDate.shYear}"
        tvSelectedDate.text =
            "${persianDate.dayName()} ${persianDate.shDay} ${persianDate.monthName()} ${persianDate.shYear}"

        ivNextMonth.setOnClickListener { nextMonth() }
        ivPreMonth.setOnClickListener { preMonth() }

        renderUI()
    }


    private fun nextMonth() {
        if (persianDate.shMonth == 12) {
            persianDate.shYear = persianDate.shYear.plus(1)
            persianDate.shMonth = 1
        } else {
            persianDate.shMonth = persianDate.shMonth.plus(1)
        }

        renderUI()
    }

    private fun preMonth() {
        if (persianDate.shMonth == 1) {
            persianDate.shYear = persianDate.shYear.minus(1)
            persianDate.shMonth = 12
        } else {
            persianDate.shMonth = persianDate.shMonth.minus(1)
        }

        renderUI()
    }

    private fun renderUI() {
        tvMonth.text = "${persianDate.monthName()} ${persianDate.shYear}"

        val currentDay = persianDate.shDay
        // set calendar to first day of month
        persianDate.shDay = 1

        var indexDay = persianDate.dayOfWeek().asEnum
        val days = arrayListOf<Day>()

        val daysOfPreviousMonth = persianDate.dayOfWeek()
        val daysOfNextMonth = 35 - persianDate.monthDays - daysOfPreviousMonth

        repeat(daysOfPreviousMonth) {
            val dayFromPreviousMonth = Day(
                name = EnumDay.Thursday,
                number = -1,
                isCurrentDay = false,
                isSelected = false,
                isForPreviousMonth = true,
                isForNextMonth = false
            )

            days.add(dayFromPreviousMonth)
        }

        repeat(persianDate.monthDays) {
            days.add(
                Day(
                    name = indexDay,
                    number = it.plus(1),
                    isCurrentDay = it.plus(1) == currentDay
                )
            )

            indexDay++
        }

        repeat(daysOfNextMonth) {
            val dayOfNextMonth = Day(
                name = EnumDay.Thursday,
                number = -1,
                isCurrentDay = false,
                isSelected = false,
                isForPreviousMonth = false,
                isForNextMonth = true,
            )

            days.add(dayOfNextMonth)
        }

        val layoutManager = GridLayoutManager(context, 7, GridLayoutManager.VERTICAL, false)

        var adapter: MonthAdapter? = null
        adapter = MonthAdapter(days) {
            days.firstOrNull { it.isSelected }?.let {
                it.isSelected = false
            }

            onDaySelected(it)

            it.isSelected = true
            adapter?.notifyDataSetChanged()
        }


        rvMonth.layoutManager = layoutManager
        rvMonth.adapter = adapter

        rvDaysOfWeeek.layoutManager = GridLayoutManager(
            context,
            7,
            GridLayoutManager.VERTICAL,
            false
        )

        rvDaysOfWeeek.adapter = DayAdapter(com.github.dafota.library.utils.weekDays)
    }

    private fun onDaySelected(it: Day) {
        persianDate.shDay = it.number
        tvSelectedDate.text =
            "${persianDate.dayName()} ${persianDate.shDay} ${persianDate.monthName()} ${persianDate.shYear}"
    }

}