package com.example.travelblog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.travelblog.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val preferences: BlogPreferences by lazy {
        BlogPreferences(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if(preferences.isLoggedIn()) {
//            startMainActivity()
//            finish()
//            return
//        }
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                onLoginClicked()
            }
        })
        binding.textUsernameLayout.editText?.addTextChangedListener(createTextWatcher(binding.textUsernameLayout))
        binding.textPasswordInput.editText?.addTextChangedListener(createTextWatcher(binding.textPasswordInput))
    }

    private fun createTextWatcher(watchedText: TextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                watchedText.error = null
            }

            override fun afterTextChanged(p0: Editable?) {}

        }
    }

    private fun onLoginClicked() {
        val username = binding.textUsernameLayout.editText?.text.toString()
        val password = binding.textPasswordInput.editText?.text.toString()
        if (username.isEmpty()) {
            binding.textUsernameLayout.error = "The username can't be empty"
        }
        if (password.isEmpty()) {
            binding.textPasswordInput.error = "The password can't be empty"
        } else if (username != "admin" && password != "admin") {
            showErrorDialog()
        } else {
            performLogin()
        }
    }

    private fun performLogin() {
        preferences.setLoggedIn(true)
        binding.loginButton.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE
        binding.textUsernameLayout.isEnabled = false
        binding.textPasswordInput.isEnabled = false
        Handler().postDelayed({
            startMainActivity()
            finish()
        }, 2000)
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(this)
            .setTitle("Login Failed")
            .setMessage("The username or password is invalid, please try again")
            .setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
            .show()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}