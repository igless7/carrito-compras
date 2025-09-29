package util

interface Loggable {
    fun log(mensaje: String)
    fun error(mensaje: String, excepcion: Exception? = null)
}