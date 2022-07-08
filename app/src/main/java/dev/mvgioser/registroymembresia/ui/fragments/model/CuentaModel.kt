package dev.mvgioser.registroymembresia.ui.fragments.model

data class CuentaModel(
    var dni: String,
    var nombres:String,
    var apellidos: String,
    var genero: String,
    var fechanac:String,
    var celular: String,
    var email: String,
    var clave:String,
    var clave2: String
)
