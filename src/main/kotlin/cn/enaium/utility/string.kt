package cn.enaium.utility

import java.util.*

fun String.toUUID(): UUID {
    return UUID.fromString(this)
}