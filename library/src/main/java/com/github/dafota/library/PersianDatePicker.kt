package com.github.dafota.library

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.dafota.library.model.Day
import com.github.dafota.library.model.EnumDay
import com.github.dafota.library.model.PersianDay
import com.github.dafota.library.utils.asEnum
import com.github.dafota.library.utils.faName
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

    private lateinit var persianDate: PersianDate

    private val content = inflater.inflate(R.layout.base_layout, this, false) as ConstraintLayout
    private val rvMonth: RecyclerView = content.findViewById(R.id.rv_month)
    private val rvWeek: RecyclerView = content.findViewById(R.id.rv_days)

    private var listener: Listener? = null
    private var timeInMilliSecond: Long? = null

    private lateinit var selectedDateDetail: PersianDay

    init {
        addView(content)
    }

    fun init(timeInMilliSecond: Long) {
        this.timeInMilliSecond = timeInMilliSecond

        persianDate = createPersianInstance()
        selectedDateDetail = getToday()

        notifyListener()
        renderUI()
    }

    private fun createPersianInstance(): PersianDate {
        return timeInMilliSecond?.let { PersianDate(it) } ?: PersianDate()
    }

    fun setListener(listener: Listener) {
        this.listener = listener
        notifyListener()
    }

    fun nextMonth() {
        internalNextMonth()
    }

    fun previousMonth() {
        internalPreviousMonth()
    }

    fun getSelectedDate(): Date {
        val p = createPersianInstance()
        p.shDay = selectedDateDetail.dayNumber
        p.shMonth = selectedDateDetail.monthNumber
        p.shYear = selectedDateDetail.yearNumber

        return p.toDate()
    }

    fun getToday(): PersianDay {
        with(createPersianInstance()) {
            return PersianDay(dayName(), shDay, monthName(), shMonth, shYear)
        }
    }

    fun getTodayDate(): Date {
        return createPersianInstance().toDate()
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

        val days = arrayListOf<Day>()

        val daysOfPreviousMonth = persianDate.dayOfWeek()
        val daysOfNextMonth = 35 - persianDate.monthDays - daysOfPreviousMonth

        repeat(daysOfPreviousMonth) {
            days += Day(
                name = EnumDay.Thursday,
                number = -1,
                isToday = false,
                isSelected = false,
                isForPreviousMonth = true,
                isForNextMonth = false
            )
        }

        var indexDay = persianDate.dayOfWeek().asEnum

        repeat(persianDate.monthDays) {
            days += Day(name = indexDay, number = it.plus(1), isToday = isToday(it.plus(1)), isSelected = isSelected(it.plus(1)))
            indexDay++
        }

        repeat(daysOfNextMonth) {
            days += Day(
                name = EnumDay.Thursday,
                number = -1,
                isToday = false,
                isSelected = false,
                isForPreviousMonth = false,
                isForNextMonth = true,
            )
        }

        setupWeekAdapter()
        setupDayAdapter(days)
    }

    private fun setupWeekAdapter() = with(rvWeek) {
        layoutManager = GridLayoutManager(context, 7, GridLayoutManager.VERTICAL, false)
        adapter = DayAdapter(com.github.dafota.library.utils.weekDays)
    }

    private fun setupDayAdapter(days: ArrayList<Day>) {
        var adapter: MonthAdapter? = null

        adapter = MonthAdapter(days) { day, index ->
            val previousSelectedIndex = days.indexOfFirst { it.isSelected }

            if (previousSelectedIndex != -1) {
                days[previousSelectedIndex].isSelected = false
            }

            persianDate.shDay = day.number

            selectedDateDetail = PersianDay(
                dayName = persianDate.dayName(),
                dayNumber = day.number,
                monthName = persianDate.monthName(),
                monthNumber = persianDate.shMonth,
                yearNumber = persianDate.shYear
            )

            day.isSelected = true

            adapter?.notifyItemChanged(previousSelectedIndex)
            adapter?.notifyItemChanged(index)

            notifyListener()
        }

        rvMonth.layoutManager = GridLayoutManager(context, 7, GridLayoutManager.VERTICAL, false)
        rvMonth.adapter = adapter
    }

    private fun notifyListener() {
        listener?.onChange(
            persianDate.monthName(),
            persianDate.shYear,
            selectedDateDetail,
            getToday()
        )
    }

    private fun isToday(dayNum: Int): Boolean {
        val p = createPersianInstance()

        return dayNum == p.shDay &&
                persianDate.shMonth == p.shMonth &&
                persianDate.shYear == p.shYear
    }

    private fun isSelected(dayNum: Int): Boolean {
        return dayNum == selectedDateDetail.dayNumber &&
                selectedDateDetail.monthNumber == persianDate.shMonth &&
                selectedDateDetail.yearNumber == persianDate.shYear
    }

    interface Listener {
        fun onChange(
            visibleMonth: String,
            visibleYear: Int,
            selected: PersianDay,
            today: PersianDay
        )
    }

}