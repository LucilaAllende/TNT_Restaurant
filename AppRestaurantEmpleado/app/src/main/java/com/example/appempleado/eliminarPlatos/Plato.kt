package com.example.appempleado.eliminarPlatos

import java.io.Serializable

//ACA agregue inicializaciones "por defecto" a los atributos sino pincha el programa porque la clase PlatoDia no tiene un contructor "por defecto" que acepte valores vacios
//ahora si acepta valores por defecto
data class Plato (var idPlato: String = "",
                   var nombrePlato: String = "",
                   var categoria: String = "",
                   var imagen: String = ""): Serializable
