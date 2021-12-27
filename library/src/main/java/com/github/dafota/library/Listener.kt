package com.github.dafota.library

import com.github.dafota.library.model.PersianDateDetail

fun interface Listener {
    fun onChange(detail: PersianDateDetail)
}