package com.petconnect.app.model

data class Usuario(
    val id: String = "",
    val nombre: String = "",
    val email: String = "",
    val esRefugio: Boolean = false
)