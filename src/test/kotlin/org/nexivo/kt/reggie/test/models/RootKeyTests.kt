package org.nexivo.kt.reggie.test.models

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.nexivo.kt.reggie.models.RootKey

class RootKeyTests: Spek({

    describe("\"RootKey\" behavior") {

        describe("Windows Registry Root Keys (and its abbreviation) are defined by Windows and are Static") {

            given("a \"RootKey\"") {

                it("should match its abbreviation") {

                    RootKey.valueOf("HKEY_LOCAL_MACHINE").abbreviation.should.be.equal("HKLM")

                    RootKey.valueOf("HKEY_CURRENT_CONFIG").abbreviation.should.be.equal("HKCC")

                    RootKey.valueOf("HKEY_CLASSES_ROOT").abbreviation.should.be.equal("HKCR")

                    RootKey.valueOf("HKEY_CURRENT_USER").abbreviation.should.be.equal("HKCU")

                    RootKey.valueOf("HKEY_USERS").abbreviation.should.be.equal("HKU")
                }
            }
        }
    }
})