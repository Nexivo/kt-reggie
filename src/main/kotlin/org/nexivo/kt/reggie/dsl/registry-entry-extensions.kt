package org.nexivo.kt.reggie.dsl

import org.nexivo.kt.reggie.models.RegistryEntry
import org.nexivo.kt.reggie.models.RegistryValue
import org.nexivo.kt.reggie.models.RegistryValueType

infix fun RegistryEntry.value     (key: String): RegistryValue = this[key] ?: RegistryValue(key, RegistryValueType.REG_NONE, "")

infix fun RegistryEntry.booleanOf (key: String): Boolean?        = if (this[key] != null) Integer.decode(this[key]!!.value) != 0 else null

infix fun RegistryEntry.stringOf  (key: String): String?         = this[key]?.value

infix fun RegistryEntry.intOf     (key: String): Int?            = if (this[key] != null) Integer.decode(this[key]!!.value) else null

infix fun RegistryEntry.child     (key: String): RegistryEntry?  = this.children[key]
