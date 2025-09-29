package services

import model.Product
import util.FileManager
import util.Logger

class ProductManager(private val logger: Logger) {
    private val products: MutableList<Product> = mutableListOf()
    private val fileManager = FileManager("products.json")

    init {
        loadProducts()
    }

    private fun loadProducts() {
        try {
            val loadedProducts = fileManager.cargarProductos()
            products.clear()
            products.addAll(loadedProducts)
            logger.log("Productos cargados exitosamente. Total: ${products.size}")
        } catch (e: Exception) {
            logger.error("Error al cargar productos", e)
            initializeDefaultProducts()
        }
    }

    private fun initializeDefaultProducts() {
        products.addAll(
            listOf(
                Product(nombre = "Cien años de soledad", precio = 25.99, autor = "Gabriel García Márquez", categoria = "Novela", stock = 10),
                Product(nombre = "1984", precio = 18.50, autor = "George Orwell", categoria = "Ciencia Ficción", stock = 8),
                Product(nombre = "El Quijote", precio = 35.75, autor = "Miguel de Cervantes", categoria = "Clásico", stock = 5),
                Product(nombre = "Harry Potter y la piedra filosofal", precio = 22.99, autor = "J.K. Rowling", categoria = "Fantasía", stock = 15),
                Product(nombre = "Orgullo y prejuicio", precio = 19.99, autor = "Jane Austen", categoria = "Romance", stock = 7),
                Product(nombre = "El señor de los anillos", precio = 29.99, autor = "J.R.R. Tolkien", categoria = "Fantasía", stock = 6),
                Product(nombre = "Crimen y castigo", precio = 21.50, autor = "Fiódor Dostoyevski", categoria = "Clásico", stock = 4),
                Product(nombre = "El principito", precio = 15.25, autor = "Antoine de Saint-Exupéry", categoria = "Infantil", stock = 12)
            )
        )
        saveProducts()
        logger.log("Productos por defecto inicializados")
    }

    fun saveProducts() {
        try {
            fileManager.guardarProductos(products)
            logger.log("Productos guardados exitosamente")
        } catch (e: Exception) {
            logger.error("Error al guardar productos", e)
        }
    }

    fun getAllProducts(): List<Product> = products.toList()

    fun getProductById(id: Int): Product? {
        return products.find { it.id == id }
    }

    fun updateStock(productId: Int, newStock: Int): Boolean {
        val product = getProductById(productId)
        return if (product != null) {
            product.stock = newStock
            saveProducts()
            true
        } else {
            false
        }
    }

    fun searchProductsByName(name: String): List<Product> {
        return products.filter { it.nombre.contains(name, ignoreCase = true) }
    }

    fun searchProductsByCategory(category: String): List<Product> {
        return products.filter { it.categoria.equals(category, ignoreCase = true) }
    }

    fun getLowStockProducts(threshold: Int = 5): List<Product> {
        return products.filter { it.stock <= threshold }
    }
}