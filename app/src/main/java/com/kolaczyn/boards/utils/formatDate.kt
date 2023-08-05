package com.kolaczyn.boards.utils

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun formatDate(dateString: String): String {
    // it crashes on some date, so it's a placeholder for now
    return "03-08 at 21:37"
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX")
    val parsedDate = ZonedDateTime.parse(dateString, formatter)

    val displayFormatter = DateTimeFormatter.ofPattern("MM-dd 'at' HH:mm")
    return parsedDate.withZoneSameInstant(ZoneId.systemDefault()).format(displayFormatter)
}
