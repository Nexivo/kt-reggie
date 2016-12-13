package org.nexivo.kt.reggie.test

inline fun <reified T : Any> className() = T::class.java.simpleName!!

inline fun <reified T> throws(block: () -> Unit)
        where T : Throwable
{
    try {
        block()
    } catch (ex: Throwable) {

        if (ex is T) { return@throws }

        throw ex
    }

    throw AssertionError("Expected ${className<T>()} was NOT Thrown!")
}
