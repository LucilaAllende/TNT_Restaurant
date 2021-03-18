package com.example.appcliente.ui.seguimiento

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SeguimientoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Mi cuenta"
    }
    val text: LiveData<String> = _text
}