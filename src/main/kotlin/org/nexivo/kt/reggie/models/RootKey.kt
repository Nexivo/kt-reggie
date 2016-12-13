package org.nexivo.kt.reggie.models

enum class RootKey(val abbreviation: String) {
    HKEY_LOCAL_MACHINE("HKLM"),
    HKEY_CURRENT_CONFIG("HKCC"),
    HKEY_CLASSES_ROOT("HKCR"),
    HKEY_CURRENT_USER("HKCU"),
    HKEY_USERS("HKU");

    operator fun plus(subKey: String): RegistryKey = RegistryKey(this, subKey)
}