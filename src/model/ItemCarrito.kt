package model

data class ItemCarrito(
    val producto: Product,
    var cantidad: Int
){
    fun getSubtotal(): Double = producto.precio * cantidad
}
