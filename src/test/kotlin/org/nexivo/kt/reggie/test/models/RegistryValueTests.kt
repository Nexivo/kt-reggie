package org.nexivo.kt.reggie.test.models

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.nexivo.kt.reggie.models.RegistryValue
import org.nexivo.kt.reggie.models.RegistryValueType

class RegistryValueTests : Spek({

    describe("\"RegistryValue\" behavior") {

        describe("Registry Keys and Values are allowed to have leading, trailing and interspersed spaces") {

            given("a key with a leading space") {

                it("should not remove the space") {

                    val actual:   String = RegistryValue(" CiceroLatin", RegistryValueType.REG_SZ, "Lorem ipsum dolor sit amet.").toString()
                    val expected: String = " CiceroLatin:REG_SZ=Lorem ipsum dolor sit amet."

                    actual.should.be.equal(expected)
                }
            }

            given("a key with a trailing space") {

                it("should not remove the space") {

                    val actual:   String = RegistryValue("CiceroLatin ", RegistryValueType.REG_SZ, "Lorem ipsum dolor sit amet.").toString()
                    val expected: String = "CiceroLatin :REG_SZ=Lorem ipsum dolor sit amet."

                    actual.should.be.equal(expected)
                }
            }

            given("a key with interspersed spaces") {

                it("should not remove the spaces") {

                    val actual:   String = RegistryValue("Cicero Latin", RegistryValueType.REG_SZ, "Lorem ipsum dolor sit amet.").toString()
                    val expected: String = "Cicero Latin:REG_SZ=Lorem ipsum dolor sit amet."

                    actual.should.be.equal(expected)
                }
            }

            given("a key with leading, trailing and interspersed spaces") {

                it("should not remove the spaces") {

                    val actual:   String = RegistryValue(" Cicero Latin ", RegistryValueType.REG_SZ, "Lorem ipsum dolor sit amet.").toString()
                    val expected: String = " Cicero Latin :REG_SZ=Lorem ipsum dolor sit amet."

                    actual.should.be.equal(expected)
                }
            }

            given("a value with a leading space") {

                it("should not remove the space") {

                    val actual:   String = RegistryValue("Cicero Latin", RegistryValueType.REG_SZ, " Lorem_ipsum_dolor_sit_amet.").toString()
                    val expected: String = "Cicero Latin:REG_SZ= Lorem_ipsum_dolor_sit_amet."

                    actual.should.be.equal(expected)
                }
            }

            given("a value with a trailing space") {

                it("should not remove the space") {

                    val actual:   String = RegistryValue("Cicero Latin", RegistryValueType.REG_SZ, "Lorem_ipsum_dolor_sit_amet. ").toString()
                    val expected: String = "Cicero Latin:REG_SZ=Lorem_ipsum_dolor_sit_amet. "

                    actual.should.be.equal(expected)
                }
            }

            given("a value with interspersed spaces") {

                it("should not remove the spaces") {

                    val actual:   String = RegistryValue("Cicero Latin", RegistryValueType.REG_SZ, "Lorem ipsum dolor sit amet.").toString()
                    val expected: String = "Cicero Latin:REG_SZ=Lorem ipsum dolor sit amet."

                    actual.should.be.equal(expected)
                }
            }

            given("a value with leading, trailing and interspersed spaces") {

                it("should not remove the spaces") {

                    val actual:   String = RegistryValue("Cicero Latin", RegistryValueType.REG_SZ, " Lorem ipsum dolor sit amet. ").toString()
                    val expected: String = "Cicero Latin:REG_SZ= Lorem ipsum dolor sit amet. "

                    actual.should.be.equal(expected)
                }
            }
        }
    }
})