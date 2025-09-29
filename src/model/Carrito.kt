package model

class Carrito {
    private val items: MutableMap<Int, ItemCarrito> = mutableMapOf()

    fun addItem(producto: Product, cantidad: Int) {
        //verificar si ya existe el producto mediante el id
        if (items.containsKey(producto.id)) {
            items.getValue(producto.id).cantidad += cantidad
        } else {
            items[producto.id] = ItemCarrito(producto, cantidad)
        }
    }

    fun eliminarItem(id: Int, cantidad: Int): Boolean {
        // Si no existe, retornar false
        val item = items[id] ?: return false

        if (item.cantidad <= cantidad) {
            items.remove(id)
            return true
        } else {
            item.cantidad -= cantidad
            return true
        }
    }

    fun getItems(): List<ItemCarrito> = items.values.toList()
    fun getTotal(): Double = items.values.sumOf { it.getSubtotal() }
    fun estaVacio(): Boolean = items.isEmpty()
    fun contieneProducto(id: Int): Boolean = items.containsKey(id)
    fun getCantidadProducto(id: Int): Int = items[id]?.cantidad ?: 0
}