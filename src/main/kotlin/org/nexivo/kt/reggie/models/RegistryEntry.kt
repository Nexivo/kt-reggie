package org.nexivo.kt.reggie.models

class RegistryEntry internal constructor (val key: RegistryKey, val values: RegistryValues, val children: RegistryEntries) {

    private val _childKeys: Array<String> by lazy { children.keys.toTypedArray() }

    private val _valueKeys: Array<String> by lazy { values.keys.toTypedArray() }

    val childKeys: Array<String> get() = _childKeys

    val valueKeys: Array<String> get() = _valueKeys

    operator fun get(valueName: String): RegistryValue? {

        val name: String = valueName.toLowerCase()

        return if (values.contains(name)) values[name] else null
    }

    override fun toString(): String = key.toString()
}
