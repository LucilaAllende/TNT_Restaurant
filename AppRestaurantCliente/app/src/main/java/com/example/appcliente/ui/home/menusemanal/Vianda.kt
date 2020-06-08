package com.example.appcliente.ui.home.menusemanal

import java.io.Serializable

//ACA agregue inicializaciones "por defecto" a los atributos sino pincha el programa porque la clase Vianda no tiene un contructor "por defecto" que acepte valores vacios
//ahora si acepta valores por defecto
data class Vianda (var nombre: String="", var ingredientes: String="", var imagen: Int=0, var dia: String="", var precio: String="") :
    Serializable