package org.nexivo.kt.reggie.dsl

import org.nexivo.kt.reggie.WindowsRegistry
import org.nexivo.kt.reggie.models.RegistryKey
import org.nexivo.kt.toolbox.process.dsl.startAndCapture

fun String.queryRegistry(vararg parameters: String): String?
    = WindowsRegistry.query(this, *parameters)

fun RegistryKey.query(vararg parameters: String): String?
    = WindowsRegistry.query(this, *parameters)

fun WindowsRegistry.query(key: RegistryKey, vararg parameters: String): String?
    = query(key.toString(), *parameters)

fun WindowsRegistry.query(key: String, vararg parameters: String): String? {

    val processArgs: Array<String> = arrayOf("reg", "query", key, *parameters)

    val (exit: Int, output: String) = processArgs.startAndCapture()

    if (exit == 0) { return output }

    return null
}
