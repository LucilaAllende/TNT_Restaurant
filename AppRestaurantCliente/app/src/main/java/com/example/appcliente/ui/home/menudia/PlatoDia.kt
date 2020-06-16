package com.example.appcliente.ui.home.menudia

import java.io.Serializable

//ACA agregue inicializaciones "por defecto" a los atributos sino pincha el programa porque la clase PlatoDia no tiene un contructor "por defecto" que acepte valores vacios
//ahora si acepta valores por defecto
data class PlatoDia (var id:String="", var nombre: String ="", var ingredientes: String="", var imagenUrl: String="", var categoria: String="", var precio:String="")
    : Serializable
