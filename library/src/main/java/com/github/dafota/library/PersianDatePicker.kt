package com.github.dafota.library

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.dafota.library.model.Day
import com.github.dafota.library.model.DayDetail
import com.github.dafota.library.model.EnumDay
import com.github.dafota.library.model.PersianDateDetail
import com.github.dafota.library.utils.asEnum
import com.github.dafota.library.utils.inc
import com.github.dafota.library.utils.inflater
import saman.zamani.persiandate.PersianDate
import java.util.*


class PersianDatePicker : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var persianDate = PersianDate()
    private var todayName = persianDate.dayName()
    private var today = persianDate.shDay
    private var currentMonth = persianDate.shMonth
    private var currentMonthName = persianDate.monthName()
    private var currentYear = persianDate.shYear

    private var todayDetail =
        DayDetail(todayName, today, currentMonthName, currentMonth, currentYear)

    private var selectedDateDetail = todayDetail


    private val content: ConstraintLayout =
        inflater.inflate(R.layout.base_layout, this, false) as ConstraintLayout
    private val rvMonth: RecyclerView = content.findViewById(R.id.rv_month)
    private val rvDaysOfWeek: RecyclerView = content.findViewById(R.id.rv_days)
    private var listener: Listener? = null

    init {
        addView(content)
        notifyListener()
        renderUI()
    }

    fun init(timeInMilliSecond: Long) {
        persianDate = PersianDate(timeInMilliSecond)
        todayName = persianDate.dayName()
        today = persianDate.shDay
        currentMonth = persianDate.shMonth
        currentMonthName = persianDate.monthName()
        currentYear = persianDate.shYear
        todayDetail = DayDetail(todayName, today, currentMonthName, currentMonth, currentYear)
        selectedDateDetail = todayDetail

        notifyListener()
        renderUI()
    }

    fun setListener(listener: Listener) {
        this.listener = listener
        notifyListener()
    }

    fun nextMonth() {
        internalNextMonth()
    }

    fun toDate(): Date {
        return persianDate.toDate()
    }

    private fun internalNextMonth() {
        if (persianDate.shMonth == 12) {
            persianDate.shYear = persianDate.shYear.plus(1)
            persianDate.shMonth = 1
            notifyListener()
        } else {
            persianDate.shMonth = persianDate.shMonth.plus(1)
        }

        notifyListener()
        renderUI()
    }

    fun previousMonth() {
        internalPreviousMonth()
    }

    private fun internalPreviousMonth() = with(persianDate) {
        if (shMonth == 1) {
            shYear = shYear.minus(1)
            shMonth = 12

            notifyListener()
        } else {
            shMonth = shMonth.minus(1)
        }

        notifyListener()
        renderUI()
    }

    private fun renderUI() {

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
                    isCurrentDay = isToday(it.plus(1)),
                    isSelected = isSelected(it.plus(1))
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
        adapter = MonthAdapter(days) { day ->
            days.firstOrNull { it.isSelected }?.let {
                it.isSelected = false
            }

            persianDate.shDay = day.number

            selectedDateDetail = selectedDateDetail.copy(
                dayName = day.name.name,
                dayNumber = day.number,
                monthName = persianDate.monthName(),
                monthNumber = persianDate.shMonth,
                yearNumber = persianDate.shYear
            )

            day.isSelected = true
            notifyListener()

            adapter?.notifyDataSetChanged()
        }


        rvMonth.layoutManager = layoutManager
        rvMonth.adapter = adapter

        rvDaysOfWeek.layoutManager = GridLayoutManager(
            context,
            7,
            GridLayoutManager.VERTICAL,
            false
        )

        rvDaysOfWeek.adapter = DayAdapter(com.github.dafota.library.utils.weekDays)
    }

    private fun notifyListener() {
        with(persianDate) {
            val detail = PersianDateDetail(
                selectedDay = DayDetail(dayName(), shDay, monthName(), shMonth, shYear),
                today = todayDetail
            )

            listener?.onChange(detail)
        }
    }

    private fun isToday(dayNum: Int): Boolean {
        return dayNum == today &&
                currentMonth == persianDate.shMonth &&
                currentYear == persianDate.shYear
    }

    private fun isSelected(dayNum: Int): Boolean {
        return dayNum == selectedDateDetail.dayNumber &&
                selectedDateDetail.monthNumber == persianDate.shMonth &&
                selectedDateDetail.yearNumber == persianDate.shYear
    }

}