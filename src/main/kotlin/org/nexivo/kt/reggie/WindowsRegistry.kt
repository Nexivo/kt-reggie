package org.nexivo.kt.reggie

import org.nexivo.kt.reggie.dsl.registryKey
import org.nexivo.kt.reggie.models.*
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object WindowsRegistry {

    private const val REGISTRY_VALUE_DEFAULT_KEY: String = "(Default)"
    private const val REGISTRY_VALUE_KEY_NAME:    String = "nam"
    private const val REGISTRY_VALUE_TYPE:        String = "typ"
    private const val REGISTRY_VALUE:             String = "val"

    private val __registryValue: Pattern = Pattern.compile("^\\s{4}(?<$REGISTRY_VALUE_KEY_NAME>.*)\\s{4}(?<$REGISTRY_VALUE_TYPE>REG_(?:${RegistryValueType.values().map{ it.name.substring(4)}.joinToString("|")}))(?:\\s{4}(?<$REGISTRY_VALUE>.*))?$")

    fun Entry(key: String, query: (String) -> String) : RegistryEntry? {

        val registryKey: RegistryKey = key.registryKey() ?: return null

        return Entry(registryKey, query)
    }

    fun Entry(key: RegistryKey, query: (String) -> String?) : RegistryEntry {

        val children:  ArrayList<Pair<String, RegistryEntry>> = ArrayList()
        val values:    ArrayList<Pair<String, RegistryValue>> = ArrayList()
        val keyString: String                                 = key.toString()
        val queried:   List<String>                           = (query(keyString) ?: "").replace("\r", "").split("\n").filter { !it.isBlank() }

        if (queried.isNotEmpty()) {

            var parseValues: Boolean = false

            queried.forEach {
                if (it.equals(keyString, true)) {
                    parseValues = true
                } else {
                    if (parseValues) {
                        val (name: String?, valueType: RegistryValueType, value: String) = parseRegistry(it)

                        if (name != null) {
                            values.add(Pair(name.toLowerCase(), RegistryValue(name, valueType, value)))
                        }
                    }

                    if (!parseValues) {
                        children.add(Pair(it.split("\\").last().toLowerCase(), Entry(it.registryKey()!!, query)))
                    }
                }
            }
        }

        return RegistryEntry(key, RegistryValues(*values.toTypedArray()), RegistryEntries(*children.toTypedArray()))
    }

    fun Entry(root: RootKey, subKey: String, remotePC: String? = null, query: (String) -> String): RegistryEntry = Entry(RegistryKey(root, subKey, remotePC), query)

    private fun parseRegistry(source: String): Triple<String?, RegistryValueType, String> {

        val matched: Matcher = __registryValue.matcher(source)

        if (!matched.find() || matched.group(REGISTRY_VALUE_KEY_NAME) == REGISTRY_VALUE_DEFAULT_KEY) {
            return Triple(null, RegistryValueType.REG_NONE, "")
        }

        return Triple(matched.group(REGISTRY_VALUE_KEY_NAME),
                      RegistryValueType.valueOf(matched.group(REGISTRY_VALUE_TYPE)),
                      matched.group(REGISTRY_VALUE) ?: "")
    }
}
