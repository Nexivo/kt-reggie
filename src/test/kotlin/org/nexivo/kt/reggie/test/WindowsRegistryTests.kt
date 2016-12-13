package org.nexivo.kt.reggie.test

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.nexivo.kt.reggie.WindowsRegistry
import org.nexivo.kt.reggie.dsl.child
import org.nexivo.kt.reggie.dsl.intOf
import org.nexivo.kt.reggie.models.RegistryEntry
import org.nexivo.kt.reggie.models.RegistryValue
import org.nexivo.kt.reggie.models.RegistryValueType
import org.nexivo.kt.reggie.models.RootKey

class WindowsRegistryTests: Spek({

    describe("\"WindowsRegistry\" behavior") {

        val query: (String) -> String = {
            if (it == "HKEY_CURRENT_USER\\SOFTWARE\\Acme") {
                """

HKEY_CURRENT_USER\SOFTWARE\Acme\Settings
HKEY_CURRENT_USER\SOFTWARE\Acme\LocalSettings

                """
            } else  if (it == "HKEY_CURRENT_USER\\SOFTWARE\\Acme\\Settings") {
                    """

HKEY_CURRENT_USER\SOFTWARE\Acme\Settings
    locale    REG_SZ    en_US
    EnableMayhem    REG_DWORD    0x0
    EnableCollab    REG_DWORD    0x1
    EnableSelectiveSync    REG_DWORD    0x1
    User Login    REG_SZ    SupaGenius69@squashed.pro
    EnableShareLink    REG_DWORD    0x1
    Attempts    REG_DWORD    0x3
    AccountLogin    REG_SZ    wile.e.coyote@AcmeID
     ( :: )     REG_SZ

                """
            } else  if (it == "HKEY_CURRENT_USER\\SOFTWARE\\Acme\\LocalSettings") {
                    """

HKEY_CURRENT_USER\SOFTWARE\Acme\LocalSettings
    AccountLogin    REG_SZ    wile.e.coyote@AcmeID

                """
            } else {
                ""
            }
        }

        val brokenQuery: (String) -> String = {
            """

HKEY_CURRENT_USER\SOFTWARE\Acme\Settings
    locale    REG_SZ    en_US
    EnableMayhem    REG_DWORD    0x0
    EnableCollab    REG_DWORD    0x1
    EnableSelectiveSync    REG_DWORD    0x1
    User Login    REG_SCHNIZZLE    SupaGenius69@squashed.pro
    EnableShareLink    REG_DWORD    0x1
    Attempts    REG_DWORD    0x3
    ActiveFolderPath    REG_SZ    C:\Users\Genius\Active Files
    AccountLogin    REG_SZ    wile.e.coyote@AcmeID
     ( :: )     REG_SZ

                """
        }

        on("querying a broken registry entry") {

            it("should skip broken values") {

                val actual:   Int = WindowsRegistry.Entry("HKEY_CURRENT_USER\\SOFTWARE\\Acme\\Settings", brokenQuery)!!.valueKeys.size
                val expected: Int = 9

                actual.should.be.equal(expected)
            }
        }

        on("querying a registry entry with no children") {

            it("should have no child entries") {

                val actual:   Int = WindowsRegistry.Entry("HKEY_CURRENT_USER\\SOFTWARE\\Acme\\Settings", query)!!.childKeys.size
                val expected: Int = 0

                actual.should.be.equal(expected)
            }
        }

        on("querying a registry entry with children") {

            it("should have child entries") {

                val actual: RegistryEntry? = WindowsRegistry.Entry("HKEY_CURRENT_USER\\SOFTWARE\\Acme", query)!! child "Settings"

                actual.should.not.be.`null`
            }
        }

        on("querying a registry entry with no values") {

            it("should have no value entries") {

                val actual:   Int = WindowsRegistry.Entry("HKEY_CURRENT_USER\\SOFTWARE\\Acme", query)!!.valueKeys.size
                val expected: Int = 0

                actual.should.be.equal(expected)
            }
        }

        on("querying a registry entry with values") {

            it("should have value entries") {

                val actual:   Int = WindowsRegistry.Entry("HKEY_CURRENT_USER\\SOFTWARE\\Acme\\Settings", query)!!.valueKeys.size
                val expected: Int = 9

                actual.should.be.equal(expected)
            }
        }

        on("querying a registry entry with values, using a \"RootKey\" and sub key \"String\"") {

            it("should have value entries") {

                val actual:   Int = WindowsRegistry.Entry(RootKey.HKEY_CURRENT_USER, "SOFTWARE\\Acme\\Settings", null, query).valueKeys.size
                val expected: Int = 9

                actual.should.be.equal(expected)
            }
        }

        on("querying a registry entry with a string value") {

            it("should have a string value") {

                val actual:   RegistryValue? = WindowsRegistry.Entry("HKEY_CURRENT_USER\\SOFTWARE\\Acme\\Settings", query)!!["User Login"]
                val expected: RegistryValue = RegistryValue("User Login", RegistryValueType.REG_SZ, "SupaGenius69@squashed.pro")

                actual.should.be.equal(expected)
            }
        }

        on("querying a registry entry with a numeric value") {

            it("should have a numeric value") {

                val actual:   Int? = WindowsRegistry.Entry("HKEY_CURRENT_USER\\SOFTWARE\\Acme\\Settings", query)!! intOf "Attempts"
                val expected: Int  = 3

                actual.should.be.equal(expected)
            }
        }
    }
})