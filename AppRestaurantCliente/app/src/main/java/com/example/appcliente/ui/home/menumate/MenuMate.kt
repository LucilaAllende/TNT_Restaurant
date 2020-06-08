package com.example.appcliente.ui.home.menumate

import java.io.Serializable

//ACA agregue inicializaciones "por defecto" a los atributos sino pincha el programa porque la clase PlatoDia no tiene un contructor "por defecto" que acepte valores vacios
//ahora si acepta valores por defecto
class MenuMate (var nombre: String="", var ingredientes: String="", var imagen: Int=0, var sabor: String="", var precio:String="") :
    Serializable