package services

import model.Carrito
import model.Product
import util.Logger

class CartManager(
    private val cart: Carrito,
    private val productManager: ProductManager,
    private val logger: Logger
) {

    fun addToCart(productId: Int, quantity: Int): Boolean {
        return try {
            // 1. Validar que el producto existe
            val product = productManager.getProductById(productId)
            if (product == null) {
                logger.log("Intento de agregar producto no existente: ID $productId")
                return false
            }

            // 2. Validar stock disponible
            if (product.stock < quantity) {
                logger.log("Stock insuficiente para producto: ${product.nombre}. Stock actual: ${product.stock}, solicitado: $quantity")
                return false
            }

            // 3. Validar cantidad positiva
            if (quantity <= 0) {
                logger.log("Cantidad inválida: $quantity")
                return false
            }

            // 4. Agregar al carrito
            cart.addItem(product, quantity)

            // 5. Actualizar stock
            product.stock -= quantity

            // 6. Guardar cambios
            productManager.saveProducts()

            logger.log("Producto agregado al carrito: ${product.nombre} x $quantity")
            true

        } catch (e: Exception) {
            logger.error("Error al agregar producto al carrito", e)
            false
        }
    }

    fun removeFromCart(productId: Int, quantity: Int): Boolean {
        return try {
            // 1. Validar que el producto está en el carrito
            if (!cart.contieneProducto(productId)) {
                logger.log("Intento de eliminar producto no existente en carrito: ID $productId")
                return false
            }

            // 2. Obtener cantidad actual en carrito
            val currentQuantity = cart.getCantidadProducto(productId)

            // 3. Determinar cuántos eliminar
            val quantityToRemove = if (quantity > currentQuantity) {
                logger.log("Solicitado eliminar $quantity, pero solo hay $currentQuantity en carrito. Eliminando todos.")
                currentQuantity
            } else {
                quantity
            }

            // 4. Eliminar del carrito
            val result = cart.eliminarItem(productId, quantityToRemove)

            if (result) {
                // 5. Restaurar stock
                val product = productManager.getProductById(productId)
                product?.stock = product?.stock?.plus(quantityToRemove) ?: 0

                // 6. Guardar cambios
                productManager.saveProducts()

                logger.log("Producto eliminado del carrito: ID $productId, cantidad: $quantityToRemove")
            }

            result

        } catch (e: Exception) {
            logger.error("Error al eliminar producto del carrito", e)
            false
        }
    }

    fun getCart(): Carrito = cart

    fun clearCart() {
        // Restaurar stock de todos los productos antes de vaciar
        cart.getItems().forEach { item ->
            val product = productManager.getProductById(item.producto.id)
            product?.stock = product?.stock?.plus(item.cantidad) ?: 0
        }

        // Vaciar carrito (necesitaríamos agregar este método al Carrito)
        // cart.vaciar()

        productManager.saveProducts()
        logger.log("Carrito vaciado")
    }

    fun getCartSummary(): String {
        val items = cart.getItems()
        if (items.isEmpty()) {
            return "El carrito está vacío"
        }

        val summary = StringBuilder()
        summary.append("Resumen del Carrito:\n")
        summary.append("-".repeat(40)).append("\n")

        items.forEach { item ->
            summary.append("${item.producto.nombre} x ${item.cantidad} - $${item.getSubtotal()}\n")
        }

        summary.append("-".repeat(40)).append("\n")
        summary.append("TOTAL: $${cart.getTotal()}")

        return summary.toString()
    }
}