package com.github.dafota.library

import com.github.dafota.library.model.PersianDay

fun interface Listener {
    fun onChange(selectedDayInfo: PersianDay, todayInfo: PersianDay)
}