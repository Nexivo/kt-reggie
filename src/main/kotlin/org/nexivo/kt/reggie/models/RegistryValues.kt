package org.nexivo.kt.reggie.models

import java.util.*

class RegistryValues internal constructor(vararg values: Pair<String, RegistryValue>): TreeMap<String, RegistryValue>(String.CASE_INSENSITIVE_ORDER) {

    init {
        putAll(values)
    }
}