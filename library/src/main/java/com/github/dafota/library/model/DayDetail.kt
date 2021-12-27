package com.github.dafota.library.model

data class DayDetail(
    val dayName: String,
    val dayNumber: Int,
    val monthName: String,
    val monthNumber: Int,
    val yearNumber: Int
)


data class PersianDateDetail(
    val selectedDay: DayDetail,
    val today: DayDetail
)