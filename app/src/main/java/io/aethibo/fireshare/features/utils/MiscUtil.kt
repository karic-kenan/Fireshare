package io.aethibo.fireshare.features.utils

fun formatLargeNumber(number: Int) = when {
    number > 100000 -> "${number.toString().substring(0, 3)}k"
    number > 10000 -> "${number.toString().substring(0, 2)}k"
    number > 1000 -> "${number.toString().substring(0, 1)}k"
    else -> number.toString()
}

fun formatLargeNumberToCommas(number: Int) = String.format("%,d", number)