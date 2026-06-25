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

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Conectamos la pantalla con el ViewModel
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        val etEmail = findViewById<TextInputEditText>(R.id.editTextEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.editTextPassword)
        val btnLogin = findViewById<MaterialButton>(R.id.buttonLogin)
        val tvGoToRegister = findViewById<TextView>(R.id.textViewGoToRegister)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            when {
                email.isEmpty() -> {
                    etEmail.error = "Ingresa tu correo"
                    etEmail.requestFocus()
                }
                password.isEmpty() -> {
                    etPassword.error = "Ingresa tu contraseña"
                    etPassword.requestFocus()
                }
                else -> {
                    btnLogin.isEnabled = false
                    btnLogin.text = "Iniciando sesión..."
                    viewModel.login(email, password)
                }
            }
        }

        tvGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Observamos la respuesta del ViewModel
        viewModel.loginResult.observe(this) { result ->
            // Restaurar botón siempre
            btnLogin.isEnabled = true
            btnLogin.text = "Iniciar sesión"

            when (result) {
                "success" -> {
                    Toast.makeText(this, "¡Bienvenido a PetConnect!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                null -> { /* Cargando, no hacer nada */ }
                else -> {
                    Toast.makeText(this, result, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}