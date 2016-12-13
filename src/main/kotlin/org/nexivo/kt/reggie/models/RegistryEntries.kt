package org.nexivo.kt.reggie.models

import java.util.*

class RegistryEntries internal constructor(vararg entries: Pair<String, RegistryEntry>): TreeMap<String, RegistryEntry>(String.CASE_INSENSITIVE_ORDER) {

    init {
        putAll(entries)
    }
}