package modelo

data class DataClassTickets(
    val uuid: String,
    var nombreAutor: String,
    var correoAutor: String,
    var titulo: String,
    var descripcion: String,
    var fechaCreacion: String,
    var estado: String,
    var fechaFinalizacion: String
)
