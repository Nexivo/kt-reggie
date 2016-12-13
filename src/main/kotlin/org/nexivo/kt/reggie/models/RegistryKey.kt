package org.nexivo.kt.reggie.models

import org.nexivo.kt.reggie.dsl.expandAbbreviation
import org.nexivo.kt.specifics.string.notBlank

data class RegistryKey(val root: RootKey, val subKey: String? = null, val remotePC: String? = null) {

    companion object {
        private val __trailingSlash: Regex = Regex("\\\\$")
    }

    constructor(root: String, subKey: String, remotePC: String? = null): this(RootKey.valueOf(root.expandAbbreviation()), subKey, remotePC)

    override fun toString(): String = "${remotePC notBlank { "\\\\$remotePC\\" }}${root.name}${subKey notBlank { "\\$subKey" }}"

    operator fun plus(subKey: String): RegistryKey = RegistryKey(this.root, "${this.subKey notBlank { "${this.subKey}\\" }}$subKey", remotePC)

    operator fun minus(subKey: String): RegistryKey {

        val index: Int? = this.subKey?.indexOf(subKey, 0, true)

        if (index == null || index < 0) {
            return this
        }

        return RegistryKey(this.root, this.subKey.substring(0, index).replace(__trailingSlash, ""), remotePC)
    }
}