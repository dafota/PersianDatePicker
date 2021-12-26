package com.github.dafota.library.utils

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.dafota.library.model.DayOfWeek
import com.github.dafota.library.model.EnumDay


val Int.dp get() = (this * Resources.getSystem().displayMetrics.density)

val ViewGroup.inflater get() = LayoutInflater.from(context)

val weekDays = arrayListOf<DayOfWeek>().apply {
    add(DayOfWeek(EnumDay.Saturday, false))
    add(DayOfWeek(EnumDay.Sunday, false))
    add(DayOfWeek(EnumDay.Monday, false))
    add(DayOfWeek(EnumDay.Tuesday, false))
    add(DayOfWeek(EnumDay.Wednesday, false))
    add(DayOfWeek(EnumDay.Thursday, false))
    add(DayOfWeek(EnumDay.Friday, false))
}

val EnumDay.faName
    get() = when (this) {
        EnumDay.Saturday -> "ش"
        EnumDay.Sunday -> "ی"
        EnumDay.Monday -> "د"
        EnumDay.Tuesday -> "س"
        EnumDay.Wednesday -> "چ"
        EnumDay.Thursday -> "پ"
        EnumDay.Friday -> "ج"
    }

val Int.asEnum
    get() = when (this) {
        0 -> EnumDay.Saturday
        1 -> EnumDay.Sunday
        2 -> EnumDay.Monday
        3 -> EnumDay.Tuesday
        4 -> EnumDay.Wednesday
        5 -> EnumDay.Thursday
        6 -> EnumDay.Friday
        else -> throw IllegalStateException("Invalid day index($this)")
    }

val EnumDay.next
    get() = when (this) {
        EnumDay.Saturday -> EnumDay.Sunday
        EnumDay.Sunday -> EnumDay.Monday
        EnumDay.Monday -> EnumDay.Tuesday
        EnumDay.Tuesday -> EnumDay.Wednesday
        EnumDay.Wednesday -> EnumDay.Thursday
        EnumDay.Thursday -> EnumDay.Friday
        EnumDay.Friday -> EnumDay.Saturday
    }

val EnumDay.previous
    get() = when (this) {
        EnumDay.Saturday -> EnumDay.Friday
        EnumDay.Sunday -> EnumDay.Saturday
        EnumDay.Monday -> EnumDay.Sunday
        EnumDay.Tuesday -> EnumDay.Monday
        EnumDay.Wednesday -> EnumDay.Tuesday
        EnumDay.Thursday -> EnumDay.Wednesday
        EnumDay.Friday -> EnumDay.Thursday
    }

operator fun EnumDay.inc(): EnumDay {
    return this.next
}

operator fun EnumDay.dec(): EnumDay {
    return this.previous
}