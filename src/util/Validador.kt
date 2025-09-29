package util

object Validador {
    fun validarEntero(input: String, rango: IntRange? = null): Int? {
        return try {
            val numero = input.toInt()
            if (rango != null && numero !in rango) null else numero
        } catch (e: NumberFormatException) {
            null
        }
    }

    fun validarDouble(input: String): Double? {
        return try {
            input.toDouble()
        } catch (e: NumberFormatException) {
            null
        }
    }

    fun validarTextoNoVacio(input: String): String? {
        return if (input.isNotBlank()) input.trim() else null
    }
}