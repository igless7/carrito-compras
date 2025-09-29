package util

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Logger : Loggable {
    private val archivoLog = File("libreria.log")
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    init {
        // Crear el archivo si no existe
        if (!archivoLog.exists()) {
            archivoLog.createNewFile()
            log("Archivo de log creado - Sistema de Librería iniciado")
        }
    }

    override fun log(mensaje: String) {
        val timestamp = LocalDateTime.now().format(formatter)
        val logEntry = "[$timestamp] INFO: $mensaje"

        // Mostrar en consola
        println(logEntry)
        // Guardar en archivo
        archivoLog.appendText("$logEntry\n")
    }

    override fun error(mensaje: String, excepcion: Exception?) {
        val timestamp = LocalDateTime.now().format(formatter)
        var logEntry = "[$timestamp] ERROR: $mensaje"

        excepcion?.let {
            logEntry += "\nExcepción: ${it.message}"
            logEntry += "\nStack Trace: ${it.stackTrace.joinToString("\n")}"
        }

        // Mostrar en consola (en rojo si tu terminal lo soporta)
        println("\u001B[31m$logEntry\u001B[0m")  // Código para color rojo
        // Guardar en archivo
        archivoLog.appendText("$logEntry\n")
    }

    // Método adicional para warnings
    fun warning(mensaje: String) {
        val timestamp = LocalDateTime.now().format(formatter)
        val logEntry = "[$timestamp] WARNING: $mensaje"

        println("\u001B[33m$logEntry\u001B[0m")  // Color amarillo
        archivoLog.appendText("$logEntry\n")
    }
}