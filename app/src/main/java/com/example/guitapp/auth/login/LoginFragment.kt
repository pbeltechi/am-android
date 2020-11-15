package com.example.guitapp.auth.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.guitapp.R
import com.example.guitapp.auth.data.AuthRepository
import com.example.guitapp.auth.data.TokenHolder
import kotlinx.android.synthetic.main.fragment_login.*
import java.lang.Exception

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        setupLoginForm()
    }

    private fun setupLoginForm() {
        viewModel.loginFormState.observe(viewLifecycleOwner, { loginState ->
            login.isEnabled = loginState.isDataValid
            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })
        viewModel.loginResult.observe(viewLifecycleOwner, { loginResult ->
            loading.visibility = View.GONE
            println("algoairmrina rainsin siniani ")
            println(loginResult)
            if (loginResult is Exception) {
                error_text.text = "Login error ${loginResult.message}"
                error_text.visibility = View.VISIBLE
            } else if(loginResult is TokenHolder) {
                findNavController().navigate(R.id.fragment_guitar_list)
            }
        })
        username.afterTextChanged {
            viewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }
        password.afterTextChanged {
            viewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }
        login.setOnClickListener {
            loading.visibility = View.VISIBLE
            error_text.visibility = View.GONE
            viewModel.login(username.text.toString(), password.text.toString())
        }
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
