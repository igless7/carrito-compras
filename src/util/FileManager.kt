package util

import model.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class FileManager(private val nombreArchivo: String) {
    private val gson = Gson()
    private val archivo = File(nombreArchivo)

    fun guardarProductos(productos: List<Product>) {
        try {
            // Convertir lista de productos a JSON
            val json = gson.toJson(productos)

            // Escribir en archivo
            archivo.writeText(json)

            println("✅ Productos guardados en: $nombreArchivo")
        } catch (e: Exception) {
            throw Exception("Error al guardar productos: ${e.message}")
        }
    }

    fun cargarProductos(): List<Product> {
        // Si el archivo no existe, retornar lista vacía
        if (!archivo.exists()) {
            println("⚠️ Archivo $nombreArchivo no existe. Se creará uno nuevo.")
            return emptyList()
        }

        return try {
            // Leer archivo JSON
            val json = archivo.readText()

            // Si el archivo está vacío
            if (json.isBlank()) {
                return emptyList()
            }

            // Convertir JSON a lista de Productos
            val tipo = object : TypeToken<List<Product>>() {}.type
            gson.fromJson<List<Product>>(json, tipo) ?: emptyList()

        } catch (e: Exception) {
            throw Exception("Error al cargar productos: ${e.message}")
        }
    }

    // Método para verificar si el archivo existe
    fun archivoExiste(): Boolean = archivo.exists()

    // Método para borrar archivo (útil para testing)
    fun borrarArchivo(): Boolean = archivo.delete()
}
