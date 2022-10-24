package com.example.travelblog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.travelblog.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        binding.loginButton.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE
        binding.textUsernameLayout.isEnabled = false
        binding.textPasswordInput.isEnabled = false
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(this)
            .setTitle("Login Failed")
            .setMessage("The username or password is invalid, please try again")
            .setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
            .show()
    }
}