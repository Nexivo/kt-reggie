package org.nexivo.kt.reggie.dsl

import org.nexivo.kt.reggie.models.RegistryKey
import org.nexivo.kt.reggie.models.RootKey
import java.util.regex.Matcher
import java.util.regex.Pattern

private const val REGISTRY_REMOTE_PC: String = "pc"
private const val REGISTRY_ROOT_KEY:  String = "rt"
private const val REGISTRY_SUB_KEY:   String = "ky"

private val _matcher:       Pattern = Pattern.compile("^(?:\\\\\\\\(?<$REGISTRY_REMOTE_PC>[^\\\\]+)\\\\)?(?<$REGISTRY_ROOT_KEY>[^\\\\]+){1}(?:\\\\(?<$REGISTRY_SUB_KEY>.+))?$")

private val _abbreviations: Pattern = Pattern.compile("(${RootKey.values().map { it.abbreviation } .joinToString("|")})")

fun String.registryKey (): RegistryKey? {

    val matched: Matcher = _matcher.matcher(this)

    if (!matched.matches()) { return null }

    return RegistryKey(RootKey.valueOf(matched.group(REGISTRY_ROOT_KEY).expandAbbreviation()), matched.group(REGISTRY_SUB_KEY), matched.group(REGISTRY_REMOTE_PC))
}

fun String.expandAbbreviation(): String {

    val match: Matcher = _abbreviations.matcher(this)

    if (!match.find()) { return this }

    val key: RootKey = RootKey.values().first { it.abbreviation == match.group(1) }

    return this.replace(key.abbreviation, key.name)
}
