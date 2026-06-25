package com.petconnect.app

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.petconnect.app.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        val etName = findViewById<TextInputEditText>(R.id.editTextName)
        val etEmail = findViewById<TextInputEditText>(R.id.editTextEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.editTextPassword)
        val btnRegister = findViewById<MaterialButton>(R.id.buttonRegister)
        val tvGoToLogin = findViewById<TextView>(R.id.textViewGoToLogin)

        btnRegister.setOnClickListener {
            val nombre = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            when {
                nombre.isEmpty() -> {
                    etName.error = "Ingresa tu nombre"
                    etName.requestFocus()
                }
                email.isEmpty() -> {
                    etEmail.error = "Ingresa tu correo"
                    etEmail.requestFocus()
                }
                password.isEmpty() -> {
                    etPassword.error = "Ingresa tu contraseña"
                    etPassword.requestFocus()
                }
                password.length < 6 -> {
                    etPassword.error = "La contraseña debe tener al menos 6 caracteres"
                    etPassword.requestFocus()
                }
                else -> {
                    btnRegister.isEnabled = false
                    btnRegister.text = "Creando cuenta..."
                    viewModel.register(email, password)
                }
            }
        }

        tvGoToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        viewModel.registerResult.observe(this) { result ->
            btnRegister.isEnabled = true
            btnRegister.text = "Registrarse"

            when (result) {
                "success" -> {
                    Toast.makeText(this, "¡Cuenta creada con éxito!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                null -> { }
                else -> {
                    Toast.makeText(this, result, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}