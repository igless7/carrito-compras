package model


data class Product(val id: Int = ProductoIdGenerator.generarId(),
                   var nombre: String,
                   var precio: Double,
                   var autor: String,
                   var categoria: String,
                   var stock: Int){
    override fun toString(): String {
        return "ID: $id | $nombre - $autor | Categoría: $categoria | Precio: $$precio | Stock: $stock"
    }
}


object ProductoIdGenerator {
    private var currentId = 0
    fun generarId(): Int = ++currentId
}
