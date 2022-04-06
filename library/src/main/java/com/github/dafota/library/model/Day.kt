package com.github.dafota.library.model


data class Day(
    val name: EnumDay,
    val number: Int,
    val isToday: Boolean,
    var isSelected: Boolean = false,
    val isForPreviousMonth: Boolean = false,
    val isForNextMonth: Boolean = false
)

enum class EnumDay {
    Saturday, Sunday, Monday, Tuesday, Wednesday, Thursday, Friday;
}