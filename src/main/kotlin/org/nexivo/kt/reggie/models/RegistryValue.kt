package org.nexivo.kt.reggie.models

data class RegistryValue(val key: String, val type: RegistryValueType, val value: String) {

    override fun toString(): String = if (type != RegistryValueType.REG_NONE) "$key:$type=$value" else "$key:$type"
}