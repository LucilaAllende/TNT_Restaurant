package com.example.appempleado

import java.io.Serializable

//ACA agregue inicializaciones "por defecto" a los atributos sino pincha el programa porque la clase PlatoDia no tiene un contructor "por defecto" que acepte valores vacios
//ahora si acepta valores por defecto
data class Pedido (var id: String = "",
                   var clienteId: String = "",
                   var direccionEnvio: String = "",
                   var estado: String = "",
                   var platoId: String = "",
                   var timestamp: String = "",
                   var nombrePlato: String = "",
                   var precioPlato: String = "",
                   var tipo: String = "",
                   var notificarme: Boolean = false): Serializable
