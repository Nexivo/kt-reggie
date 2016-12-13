package org.nexivo.kt.reggie.test.models

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.nexivo.kt.reggie.dsl.booleanOf
import org.nexivo.kt.reggie.dsl.intOf
import org.nexivo.kt.reggie.dsl.stringOf
import org.nexivo.kt.reggie.dsl.value
import org.nexivo.kt.reggie.models.*

class RegistryEntryTests: Spek({

    describe("\"RegistryEntry\" behavior") {

        val entry: (Boolean, Boolean) -> RegistryEntry = {

            hasValues, hasChildren ->

            val key: RegistryKey = RegistryKey(RootKey.HKEY_LOCAL_MACHINE, "SOFTWARE\\Acme\\Settings")
            val enabled: Pair<String, RegistryValue> = "Enabled" to RegistryValue("Enabled",     RegistryValueType.REG_DWORD, "0x1")
            val lbs:     Pair<String, RegistryValue> = "Lbs"     to RegistryValue("Lbs",         RegistryValueType.REG_DWORD, "0x20")
            val scene:   Pair<String, RegistryValue> = "Scene"   to RegistryValue("Scene",       RegistryValueType.REG_SZ,    "Desert")
            val plans:   Pair<String, RegistryEntry> = "Plans"   to RegistryEntry(key + "Plans", RegistryValues(), RegistryEntries())

            RegistryEntry(key,
                          if (hasValues)   RegistryValues(enabled, lbs, scene) else RegistryValues(),
                          if (hasChildren) RegistryEntries(plans)              else RegistryEntries())
        }

        given ("a \"RegistryEntry\" with values") {

            it ("should have value keys") {

                val subject: RegistryEntry = entry(HAS_VALUE, NO_CHILDREN)
                val actual:   List<String>  = subject.valueKeys.toList()
                val expected: Array<String> = arrayOf("Enabled", "Lbs", "Scene")

                actual.should.have.all.elements(*expected)
            }
        }

        given ("a \"RegistryEntry\" without values") {

            it ("should not have value keys") {

                val subject: RegistryEntry = entry(NO_VALUE, HAS_CHILDREN)
                val actual:   Int           = subject.valueKeys.size
                val expected: Int           = 0

                actual.should.be.equal(expected)
            }
        }

        given ("a \"RegistryEntry\" with children") {

            it ("should have child keys") {

                val subject: RegistryEntry = entry(NO_VALUE, HAS_CHILDREN)
                val actual:   List<String>  = subject.childKeys.toList()
                val expected: Array<String> = arrayOf("Plans")

                actual.should.have.all.elements(*expected)
            }
        }

        given ("a \"RegistryEntry\" without children") {

            it ("should not have child keys") {

                val subject: RegistryEntry = entry(HAS_VALUE, NO_CHILDREN)
                val actual:   Int           = subject.childKeys.size
                val expected: Int           = 0

                actual.should.be.equal(expected)
            }
        }

        given ("a \"RegistryEntry\" with a missing value") {

            it ("should return a REG_NONE \"RegistryValue\"") {

                val subject: RegistryEntry = entry(HAS_VALUE, NO_CHILDREN)
                val actual:   RegistryValue? = subject value "Bomb"
                val expected: RegistryValue = RegistryValue("Bomb", RegistryValueType.REG_NONE, "")

                actual.should.be.equal(expected)
            }
        }

        given ("a \"RegistryEntry\" with an existing value") {

            it ("should return a \"RegistryValue\"") {

                val subject: RegistryEntry = entry(HAS_VALUE, NO_CHILDREN)
                val actual:   RegistryValue? = subject value "Scene"
                val expected: RegistryValue = RegistryValue("Scene", RegistryValueType.REG_SZ, "Desert")

                actual.should.be.equal(expected)
            }
        }

        given ("a \"RegistryEntry\" with a \"String\" value") {

            it ("should return a \"String\"") {

                val subject: RegistryEntry = entry(HAS_VALUE, NO_CHILDREN)
                val actual:   String?       = subject stringOf "Scene"
                val expected: String        = "Desert"

                actual.should.be.equal(expected)
            }
        }

        given ("a \"RegistryEntry\" with a \"Boolean\" value") {

            it ("should return a \"Boolean\"") {

                val subject: RegistryEntry = entry(HAS_VALUE, NO_CHILDREN)
                val actual:   Boolean?      = subject booleanOf "Enabled"
                val expected: Boolean       = true

                actual.should.be.equal(expected)
            }
        }

        given ("a \"RegistryEntry\" with an \"Int\" value") {

            it ("should return a \"Int\"") {

                val subject: RegistryEntry = entry(HAS_VALUE, NO_CHILDREN)
                val actual:   Int?          = subject intOf "Lbs"
                val expected: Int           = 32

                actual.should.be.equal(expected)
            }
        }
    }
}) {
    companion object {

        private const val HAS_CHILDREN: Boolean = true
        private const val HAS_VALUE:    Boolean = true
        private const val NO_CHILDREN:  Boolean = false
        private const val NO_VALUE:     Boolean = false
    }
}