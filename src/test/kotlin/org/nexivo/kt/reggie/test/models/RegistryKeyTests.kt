package org.nexivo.kt.reggie.test.models

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.nexivo.kt.reggie.dsl.registryKey
import org.nexivo.kt.reggie.models.RegistryKey
import org.nexivo.kt.reggie.models.RootKey
import org.nexivo.kt.reggie.test.throws

class RegistryKeyTests: Spek({

    describe("\"RegistryKey\" behavior") {

        given ("a \"RegistryKey\" without a remote pc") {

            it ("should equate to a valid key") {

                val actual:   String = RegistryKey(RootKey.HKEY_LOCAL_MACHINE, "AppEvents\\Schemes\\Apps\\Explorer").toString()
                val expected: String = "HKEY_LOCAL_MACHINE\\AppEvents\\Schemes\\Apps\\Explorer"

                actual.should.be.equal(expected)
            }
        }

        given ("a \"RegistryKey\" with a blank remote pc") {

            it ("should equate to a valid key") {

                val actual:   String = RegistryKey(RootKey.HKEY_LOCAL_MACHINE, "AppEvents\\Schemes\\Apps\\Explorer", " ").toString()
                val expected: String = "HKEY_LOCAL_MACHINE\\AppEvents\\Schemes\\Apps\\Explorer"

                actual.should.be.equal(expected)
            }
        }

        given ("a \"RegistryKey\" with a remote pc") {

            it ("should equate to a valid key") {

                val actual:   String = RegistryKey(RootKey.HKEY_LOCAL_MACHINE, "AppEvents\\Schemes\\Apps\\Explorer", "REMOTE-SERVER-01").toString()
                val expected: String = "\\\\REMOTE-SERVER-01\\HKEY_LOCAL_MACHINE\\AppEvents\\Schemes\\Apps\\Explorer"

                actual.should.be.equal(expected)
            }
        }

        given ("a \"RegistryKey\" without a sub key") {

            it ("should equate to a valid key") {

                val actual:   String = RegistryKey(RootKey.HKEY_LOCAL_MACHINE).toString()
                val expected: String = "HKEY_LOCAL_MACHINE"

                actual.should.be.equal(expected)
            }

            on ("adding a sub key \"String\"") {

                it ("should a new \"RegistryKey\" with the new sub key added") {

                    val subject: RegistryKey = RegistryKey(RootKey.HKEY_CURRENT_USER)
                    val actual:   String      = (subject + "SOFTWARE").toString()
                    val expected: String      = "HKEY_CURRENT_USER\\SOFTWARE"

                    actual.should.be.equal(expected)
                }
            }

            on ("subtracting a non-existing sub key \"String\"") {

                it ("should a new \"RegistryKey\" with sub key removed") {

                    val subject: RegistryKey = RegistryKey(RootKey.HKEY_CURRENT_USER)
                    val actual:   String      = (subject - "SOFTWARE").toString()
                    val expected: String      = "HKEY_CURRENT_USER"

                    actual.should.be.equal(expected)
                }
            }
        }

        given ("a \"RegistryKey\" with a blank sub key") {

            it ("should equate to a valid key") {

                val actual:   String = RegistryKey(RootKey.HKEY_LOCAL_MACHINE, "   ", "REMOTE-SERVER-01").toString()
                val expected: String = "\\\\REMOTE-SERVER-01\\HKEY_LOCAL_MACHINE"

                actual.should.be.equal(expected)
            }
        }

        given ("a \"RegistryKey\" with a sub key") {

            it ("should equate to a valid key") {

                val actual:   String = RegistryKey(RootKey.HKEY_LOCAL_MACHINE, "AppEvents\\EventLabels").toString()
                val expected: String = "HKEY_LOCAL_MACHINE\\AppEvents\\EventLabels"

                actual.should.be.equal(expected)
            }
        }

        given ("a \"RegistryKey\" with a root key \"String\"") {

            it ("should equate to a valid key") {

                val actual:   String = RegistryKey("HKEY_LOCAL_MACHINE", "AppEvents\\EventLabels").toString()
                val expected: String = "HKEY_LOCAL_MACHINE\\AppEvents\\EventLabels"

                actual.should.be.equal(expected)
            }
        }

        given ("a \"RegistryKey\" with a root key abbreviation \"String\"") {

            it ("should equate to a valid key") {

                val actual:   String = RegistryKey("HKLM", "AppEvents\\EventLabels").toString()
                val expected: String = "HKEY_LOCAL_MACHINE\\AppEvents\\EventLabels"

                actual.should.be.equal(expected)
            }
        }

        given ("a \"RegistryKey\" with an invalid root key \"String\"") {

            it ("should throw an IllegalArgumentException") {

                throws<IllegalArgumentException> {

                    RegistryKey("MLKH", "AppEvents\\EventLabels")
                }
            }
        }

        given ("a \"RegistryKey\"") {

            on ("adding a sub key \"String\"") {

                it ("should a new \"RegistryKey\" with the new sub key added") {

                    val subject: RegistryKey = RegistryKey(RootKey.HKEY_CURRENT_USER, "SOFTWARE")
                    val actual:   String      = (subject + "Acme").toString()
                    val expected: String      = "HKEY_CURRENT_USER\\SOFTWARE\\Acme"

                    actual.should.be.equal(expected)
                }
            }

            on ("subtracting an existing sub key \"String\"") {

                it ("should a new \"RegistryKey\" with sub key removed") {

                    val subject: RegistryKey = RegistryKey(RootKey.HKEY_CURRENT_USER, "SOFTWARE\\Acme\\Settings")
                    val actual:   String      = (subject - "Acme").toString()
                    val expected: String      = "HKEY_CURRENT_USER\\SOFTWARE"

                    actual.should.be.equal(expected)
                }
            }

            on ("subtracting a non-existing sub key \"String\"") {

                it ("should result in the original \"RegistryKey\"") {

                    val subject: RegistryKey = RegistryKey(RootKey.HKEY_CURRENT_USER, "SOFTWARE\\Acme\\Settings")
                    val actual:   String      = (subject - "JackO").toString()
                    val expected: String      = "HKEY_CURRENT_USER\\SOFTWARE\\Acme\\Settings"

                    actual.should.be.equal(expected)
                }
            }
        }

        given("a complete registry key \"String\"") {

            it ("should equate to a \"RegistryKey\"") {

                val actual:   String = "\\\\REMOTE-SERVER-01\\HKLM\\AppEvents\\Schemes\\Apps\\Explorer".registryKey().toString()
                val expected: String = "\\\\REMOTE-SERVER-01\\HKEY_LOCAL_MACHINE\\AppEvents\\Schemes\\Apps\\Explorer"

                actual.should.be.equal(expected)
            }
        }

        given("a registry key \"String\" without a remote pc") {

            it ("should equate to a \"RegistryKey\"") {

                val actual:   String = "HKLM\\AppEvents\\Schemes\\Apps\\Explorer".registryKey().toString()
                val expected: String = "HKEY_LOCAL_MACHINE\\AppEvents\\Schemes\\Apps\\Explorer"

                actual.should.be.equal(expected)
            }
        }

        given("a registry key \"String\" without a sub key") {

            it ("should equate to a \"RegistryKey\"") {

                val actual:   String = "\\\\REMOTE-SERVER-01\\HKCC".registryKey().toString()
                val expected: String = "\\\\REMOTE-SERVER-01\\HKEY_CURRENT_CONFIG"

                actual.should.be.equal(expected)
            }
        }

        given("a registry key \"String\" without a sub key nor remote pc") {

            it ("should equate to a \"RegistryKey\"") {

                val actual:   String = "HKCU".registryKey().toString()
                val expected: String = "HKEY_CURRENT_USER"

                actual.should.be.equal(expected)
            }
        }

        given("a registry key \"String\" with an invalid key") {

            it ("should throw an IllegalArgumentException") {

                throws<IllegalArgumentException> {
                    "\\\\REMOTE-SERVER-01\\MLKH\\AppEvents\\Schemes\\Apps\\Explorer".registryKey()
                }
            }
        }

        given("an invalid registry key \"String\"") {

            it ("should return a null value") {

                val actual: RegistryKey? = "\\REMOTE-SERVER-01\\AppEvents\\Schemes\\Apps\\Explorer".registryKey()

                actual.should.be.`null`
            }
        }
    }
})